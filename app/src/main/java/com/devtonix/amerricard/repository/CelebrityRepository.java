package com.devtonix.amerricard.repository;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Celebrity;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.response.CelebrityResponse;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.CelebritiesGetCallback;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CelebrityRepository {

    private static final String TAG = CelebrityRepository.class.getSimpleName();
    @Inject
    API api;
    @Inject
    SharedHelper sharedHelper;

    private Context context;

    public CelebrityRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void getCelebrities(final CelebritiesGetCallback callback) {
        Call<CelebrityResponse> call = api.getCelebrities();
        call.enqueue(new Callback<CelebrityResponse>() {
            @Override
            public void onResponse(Call<CelebrityResponse> call, Response<CelebrityResponse> response) {
                if (response.isSuccessful()) {

                    final List<Celebrity> celebrities = response.body().getData();

                    saveCelebritiesToStorage(celebrities);

                    callback.onSuccess(celebrities);
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<CelebrityResponse> call, Throwable t) {
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

    public void saveCelebritiesToStorage(List<Celebrity> celebrities) {
        sharedHelper.saveCelebrities(celebrities);
    }

    public List<Celebrity> getCelebritiesFromStorage() {
        return sharedHelper.getCelebrities();
    }

}
