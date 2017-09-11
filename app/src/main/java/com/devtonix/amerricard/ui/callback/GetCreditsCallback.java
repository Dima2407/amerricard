package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.network.response.CreditsResponse;

public interface GetCreditsCallback {
    void onSuccess(CreditsResponse creditsResponse);
    void onError();
    void onRetrofitError(String message);
}
