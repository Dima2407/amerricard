package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.Credit;

public interface CardShareCallback {
    void onSuccess(Credit credit);
    void onError();
    void onRetrofitError(String message);
}
