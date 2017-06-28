package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.Celebrity;

import java.util.List;

public interface CelebritiesGetCallback {
    void onSuccess(List<Celebrity> celebrities);
    void onError();
    void onRetrofitError(String message);
}
