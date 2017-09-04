package com.devtonix.amerricard.ui.callback;

public interface LoginCallback {
    void onSuccess(String token, String status);
    void onError();
    void onRetrofitError(String message);
}
