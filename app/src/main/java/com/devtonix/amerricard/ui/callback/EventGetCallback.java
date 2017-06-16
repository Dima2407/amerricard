package com.devtonix.amerricard.ui.callback;

import com.devtonix.amerricard.model.EventItem;

import java.util.List;

public interface EventGetCallback {
    void onSuccess(List<EventItem> events);
    void onError();
    void onRetrofitError(String message);
}
