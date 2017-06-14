package com.devtonix.amerricard.repository;

import android.content.Context;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.response.CardResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.ui.callback.CardGetCallback;
import com.devtonix.amerricard.ui.callback.CardShareCallback;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardRepository {

    @Inject
    API api;

    private Context context;

    public CardRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void addFavoriteCard(long id) {

    }

    public void deleteFavoriteCard(long id) {

    }

    public void shareCard(long cardId, final CardShareCallback callback) {
        Call<SimpleResponse> call = api.shareCard(cardId);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()){
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
                if (response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.onRetrofitError(t.getMessage());
                } else {
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }
}
