package com.devtonix.amerricard.model;

import java.util.List;

public class ListCardItem {

    private List<CardItem> cards;

    public ListCardItem(List<CardItem> cards) {
        this.cards = cards;
    }

    public List<CardItem> getCards() {
        return cards;
    }
}
