package com.devtonix.amerricard.model;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {

    public String type;

    public List<Item> data;

    public long id;

    public String name;

    public String image;

    public String author;

    public String owner;

    public Integer price;

    public Integer likes;

    public Integer downloads;

    public String cardType;

    public boolean isItemCategory() {
        if (type != null && type.equals("category")) return true;
        return false;
    }
}
