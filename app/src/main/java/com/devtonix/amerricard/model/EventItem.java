package com.devtonix.amerricard.model;

import java.io.Serializable;
import java.util.List;

public class EventItem implements Serializable {
    public String type;
    public long id;
    public String name;
    public String image;
    public List<Long> dates;
    public List<Item> data;
}
