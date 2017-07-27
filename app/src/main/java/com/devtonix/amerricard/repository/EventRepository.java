package com.devtonix.amerricard.repository;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.network.request.EditEventRequest;
import com.devtonix.amerricard.network.response.EventResponse;
import com.devtonix.amerricard.network.response.SimpleResponse;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.ui.callback.EventCreateCallback;
import com.devtonix.amerricard.ui.callback.EventGetCallback;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {

    private static final String TAG = EventRepository.class.getSimpleName();

    @Inject
    API api;
    @Inject
    SharedHelper sharedHelper;

    private Context context;

    public EventRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void createEvent(CreateEventRequest createEventRequest, final EventCreateCallback callback) {
        Call<SimpleResponse> call = api.createEvent(createEventRequest);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    callback.onRetrofitError(t.getMessage());
                } else {
                    Log.d(TAG, "onFailure: " + NetworkModule.UNKNOWN_ERROR);
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void editEvent(EditEventRequest editEventRequest) {

    }

    public void deleteEvent(long id) {

    }

    public void getEvents(final EventGetCallback callback) {
        Call<EventResponse> call = api.getEvents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final List<EventItem> events = response.body().getData();

                    saveEventToStorage(events);
                    callback.onSuccess(events);

                } else {
                    Log.d(TAG, "onResponse: error");
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    callback.onRetrofitError(t.getMessage());
                } else {
                    Log.d(TAG, "onFailure: " + NetworkModule.UNKNOWN_ERROR);
                    callback.onRetrofitError(NetworkModule.UNKNOWN_ERROR);
                }
            }
        });
    }

    public void saveEventToStorage(final List<EventItem> data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedHelper.saveEvents(data);
            }
        }).start();
    }

    public List<EventItem> getEventFromStorage() {
        return sharedHelper.getEvents();
    }


}
