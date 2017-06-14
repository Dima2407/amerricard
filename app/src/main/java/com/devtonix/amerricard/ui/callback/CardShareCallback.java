package com.devtonix.amerricard.ui.callback;

public interface CardShareCallback {
    void onSuccess();
    void onError();
    void onRetrofitError(String message);
}
