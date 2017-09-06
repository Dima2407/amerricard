package com.devtonix.amerricard.ui.callback;

public interface ForgotPasswordCallback {
    void onSuccess(String status);
    void onError();
    void onRetrofitError(String message);
}
