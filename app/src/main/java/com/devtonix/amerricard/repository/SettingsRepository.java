package com.devtonix.amerricard.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.Setting;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.response.SettingsResponse;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.SettingsGetCallback;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRepository {

    @Inject
    API api;
    @Inject
    SharedHelper sharedHelper;

    private static final String TAG = SettingsRepository.class.getSimpleName();

    private Context context;

    public SettingsRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void getSettings(final SettingsGetCallback callback){
        Call<SettingsResponse> call = api.getSettings();
        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                if (response.isSuccessful()) {
                    final Setting setting = response.body().getData();
                    saveSettingsToStorage(setting);
                    callback.onSucces(setting);
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {
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

    private void saveSettingsToStorage(Setting setting) {
        if (!TextUtils.isEmpty(setting.getBirthdayCategoryId())) {
            sharedHelper.saveBithrayCategoryId(Integer.parseInt(setting.getBirthdayCategoryId()));
        }
    }

    public int getCategoryIdFromStorage() {
        return sharedHelper.getBithrayCategoryId();
    }
}
