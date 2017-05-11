package com.devtonix.amerricard.api.event;

import com.devtonix.amerricard.model.EventItem;

import java.util.List;


public class EventSuccessEvent {

    private List<EventItem> item;
    public EventSuccessEvent(List<EventItem> item) {
        this.item = item;
    }

    public List<EventItem> getItem() {
        return item;
    }
}
