package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;

import java.util.List;

public interface CardGetCallback {

    void onSuccess(List<CategoryItem> data);
    void onError();
    void onRetrofitError(String message);
}
