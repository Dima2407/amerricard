package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.CategoryItemFirstLevel;

import java.util.List;

public interface CardGetCallback {

    void onSuccess(List<CategoryItemFirstLevel> data);
    void onError();
    void onRetrofitError(String message);
}
