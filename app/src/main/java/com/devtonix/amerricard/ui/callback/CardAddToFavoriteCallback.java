package com.devtonix.amerricard.ui.callback;

public interface CardAddToFavoriteCallback {
    void onSuccess();
    void onError();
    void onRetrofitError(String message);
}
