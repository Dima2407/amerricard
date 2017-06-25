package com.devtonix.amerricard.repository;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.response.CardResponse;
import com.devtonix.amerricard.network.response.CardResponseNew;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.CardAddToFavoriteCallback;
import com.devtonix.amerricard.ui.callback.CardDeleteFromFavoriteCallback;
import com.devtonix.amerricard.ui.callback.CardGetCallback;
import com.devtonix.amerricard.ui.callback.CardShareCallback;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardRepository {

    private static final String TAG = CardRepository.class.getSimpleName();

    @Inject
    API api;
    @Inject
    SharedHelper sharedHelper;

    private Context context;

    public CardRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void sendAddFavoriteCardRequest(long id, final CardAddToFavoriteCallback callback) {
        Call<SimpleResponse> call = api.addFavoriteCard(id);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void sendDeleteFavoriteCardRequest(long id, final CardDeleteFromFavoriteCallback callback) {
        Call<SimpleResponse> call = api.deleteFavoriteCard(id);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void sendShareCardRequest(long cardId, final CardShareCallback callback) {
        Call<SimpleResponse> call = api.shareCard(cardId);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void getCards(final CardGetCallback callback) {
        Call<CardResponseNew> call = api.getCard();
        call.enqueue(new Callback<CardResponseNew>() {
            @Override
            public void onResponse(Call<CardResponseNew> call, Response<CardResponseNew> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final List<CategoryItem> categories = response.body().getCategories();

                    for (CategoryItem mainCategory: categories){
                        for (CategoryItem category: mainCategory.getCategoryItems()){
                            Log.d(TAG, "["+mainCategory.getName().getEn()+"]"+"category=" + category.getName().getEn());
                        }
                        for (CardItem card : mainCategory.getCardItems()){
                            Log.d(TAG, "["+mainCategory.getName().getEn()+"]"+"card="+card.getName());
                        }
                    }
                    saveCardsToStorage(categories);

                    callback.onSuccess(categories);
                } else {
                    Log.d(TAG, "getCards() onResponse: error");
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<CardResponseNew> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    Log.d(TAG, "getCards() onFailure: " + t.getMessage());
                    callback.onRetrofitError(t.getMessage());
                } else {
                    Log.d(TAG, "getCards() onFailure: " + NetworkModule.UNKNOWN_ERROR);
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public List<CategoryItem> getCardsFromStorage() {

        return sharedHelper.getCards();
    }

    public void saveCardsToStorage(List<CategoryItem> itemFirstLevels) {

        sharedHelper.saveCards(itemFirstLevels);
    }


    public List<CardItem> getFavoriteCardsFromStorage() {
        return sharedHelper.getFavorites();
    }

    public void addCardToFavorites(CardItem item) {
        List<CardItem> oldItems = sharedHelper.getFavorites();
        oldItems.add(item);
        sharedHelper.saveFavorites(oldItems);
    }

    public void removeCardFromFavorites(CardItem item) {
        List<CardItem> oldItems = sharedHelper.getFavorites();
        int position = -1;
        for (int i = 0; i < oldItems.size(); i++) {
            if ((int) oldItems.get(i).getId() == (int) item.getId()) {
                position = i;
            }
        }
        if (position != -1) {
            oldItems.remove(position);
            sharedHelper.saveFavorites(oldItems);
        }
    }
}
