package com.devtonix.amerricard.ui.callback;

public interface EventCreateCallback {
    void onSuccess();
    void onError();
    void onRetrofitError(String message);
}
