package com.devtonix.amerricard.api.event;

import com.devtonix.amerricard.model.Item;

import java.util.List;


public class EventSuccessEvent {

    private List<Item> item;
    public EventSuccessEvent(List<Item> item) {
        this.item = item;
    }

    public List<Item> getItem() {
        return item;
    }
}
