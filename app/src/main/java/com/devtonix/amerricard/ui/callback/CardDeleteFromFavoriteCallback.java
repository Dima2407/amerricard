package com.devtonix.amerricard.ui.callback;

public interface CardDeleteFromFavoriteCallback {
    void onSuccess();
    void onError();
    void onRetrofitError(String message);
}
