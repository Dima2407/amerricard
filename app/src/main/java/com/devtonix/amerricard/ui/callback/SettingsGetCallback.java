package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.Setting;

public interface SettingsGetCallback {

    void onSucces(Setting settings);

    void onError();

    void onRetrofitError(String message);
}
