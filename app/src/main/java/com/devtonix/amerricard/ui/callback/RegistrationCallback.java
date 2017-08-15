package com.devtonix.amerricard.ui.callback;

public interface RegistrationCallback {
    void onSuccess(String token);
    void onError();
    void onRetrofitError(String message);
}
