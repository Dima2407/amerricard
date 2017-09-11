package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.DataCreditResponse;

public interface CardShareCallback {
    void onSuccess(DataCreditResponse dataCreditResponse);
    void onError();
    void onRetrofitError(String message);
}
