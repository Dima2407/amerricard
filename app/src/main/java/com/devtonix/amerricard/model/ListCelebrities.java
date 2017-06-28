package com.devtonix.amerricard.model;

import java.util.List;

public class ListCelebrities {

    private List<Celebrity> celebrities;

    public ListCelebrities(List<Celebrity> celebrities) {
        this.celebrities = celebrities;
    }

    public List<Celebrity> getCelebrities() {
        return celebrities;
    }
}
