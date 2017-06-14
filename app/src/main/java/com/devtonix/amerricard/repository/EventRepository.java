package com.devtonix.amerricard.repository;

import android.content.Context;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.network.API;
import com.devtonix.amerricard.network.request.CreateEventRequest;
import com.devtonix.amerricard.network.request.EditEventRequest;

import javax.inject.Inject;

public class EventRepository {

    @Inject
    API api;

    private Context context;

    public EventRepository(Context context) {
        ACApplication.getMainComponent().inject(this);
        this.context = context;
    }

    public void createEvent(CreateEventRequest createEventRequest) {

    }

    public void editEvent(EditEventRequest editEventRequest) {

    }

    public void deleteEvent(long id) {

    }

    public void getEvents() {

    }
}
