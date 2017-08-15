package com.devtonix.amerricard.ui.callback;

public interface ForgotPasswordCallback {
    void onSuccess();
    void onError();
    void onRetrofitError(String message);
}
