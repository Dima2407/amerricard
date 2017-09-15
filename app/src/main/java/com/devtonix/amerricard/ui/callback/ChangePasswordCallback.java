package com.devtonix.amerricard.ui.callback;

public interface ChangePasswordCallback {
    void onSuccess(String status);
    void onError();
    void onRetrofitError(String message);
}
