package com.devtonix.amerricard.model;

import java.util.List;

public class ListEventItem {

    private List<EventItem> events;

    public ListEventItem(List<EventItem> events) {
        this.events = events;
    }

    public List<EventItem> getEvents() {
        return events;
    }
}
