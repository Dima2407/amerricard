package com.devtonix.amerricard.repository;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.CategoryItemSecondLevel;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.response.CardResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.ui.callback.CardGetCallback;
import com.devtonix.amerricard.ui.callback.CardShareCallback;
import com.devtonix.amerricard.storage.SharedHelper;

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

    public void sendAddFavoriteCardRequest(long id) {

    }

    public void sendDeleteFavoriteCardRequest(long id) {

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
        Call<CardResponse> call = api.getCard();
        call.enqueue(new Callback<CardResponse>() {
            @Override
            public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final List<CategoryItemFirstLevel> firstLevels = response.body().getData();

                    //for testing
                    for (CategoryItemFirstLevel itemFirstLevel : firstLevels) {
                        final List<CategoryItemSecondLevel> categoryItemSecondLevels = itemFirstLevel.getData();
                        for (CategoryItemSecondLevel secondLevel : categoryItemSecondLevels) {
                            Log.d(TAG, "onResponse: name = " + secondLevel.getNameJsonEl().toString());
                        }
                    }
                    Log.d(TAG, "onResponse: success   card = " + response.body().getData().get(0).getData().get(0).getData().get(0).getName());


                    saveCardsToStorage(firstLevels);

                    callback.onSuccess(response.body().getData());
                } else {
                    Log.d(TAG, "onResponse: error");
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    callback.onRetrofitError(t.getMessage());
                } else {
                    Log.d(TAG, "onFailure: " + NetworkModule.UNKNOWN_ERROR);
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public List<CategoryItemFirstLevel> getCardsFromStorage(){
        return sharedHelper.getCards();
    }

    public void saveCardsToStorage(List<CategoryItemFirstLevel> itemFirstLevels) {

        sharedHelper.saveCards(itemFirstLevels);
    }


    public List<CardItem> getFavoriteCardsFromStorage(){
        return sharedHelper.getFavorites();
    }

    public void addCardToFavorites(CardItem item){
        List<CardItem> oldItems = sharedHelper.getFavorites();
        oldItems.add(item);
        sharedHelper.saveFavorites(oldItems);
    }

    public void removeCardFromFavorites(CardItem item){
        List<CardItem> oldItems = sharedHelper.getFavorites();
        int position = -1;
        for (int i=0;i<oldItems.size();i++) {
            if ((int)oldItems.get(i).getId()== (int)item.getId()) {
                position = i;
            }
        }
        if (position != -1) {
            oldItems.remove(position);
            sharedHelper.saveFavorites(oldItems);
        }
    }
}

