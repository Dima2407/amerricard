package com.devtonix.amerricard.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateEventRequest {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dates")
    @Expose
    private List<Long> dates = null;
    @SerializedName("cards")
    @Expose
    private List<Long> cards = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getDates() {
        return dates;
    }

    public void setDates(List<Long> dates) {
        this.dates = dates;
    }

    public List<Long> getCards() {
        return cards;
    }

    public void setCards(List<Long> cards) {
        this.cards = cards;
    }

}
