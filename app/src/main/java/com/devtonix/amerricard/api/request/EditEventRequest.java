package com.devtonix.amerricard.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditEventRequest {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dates")
    @Expose
    private List<Integer> dates = null;
    @SerializedName("cards")
    @Expose
    private List<Integer> cards = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getDates() {
        return dates;
    }

    public void setDates(List<Integer> dates) {
        this.dates = dates;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

}
