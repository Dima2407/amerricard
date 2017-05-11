package com.devtonix.amerricard.api.event;

import com.devtonix.amerricard.model.Item;

import java.util.List;

public class CardSuccessEvent extends SuccessEvent {

    private List<Item> item;
    public CardSuccessEvent(List<Item> item) {
        this.item = item;
    }

    public List<Item> getItem() {
        return item;
    }
}
