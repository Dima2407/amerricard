package com.devtonix.amerricard.model;

import android.text.TextUtils;

import com.devtonix.amerricard.api.NetworkServiceProvider;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Item implements Serializable {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

    public String type;

    public List<Item> data;

    public List<Long> dates;

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

    public String getUrlByType() {
        if (type.equals("category")) {
            return NetworkServiceProvider.CATEGORY_SUFFIX;
        } else if (type.equals("event")) {
            return NetworkServiceProvider.EVENT_SUFFIX;
        } else {
            return NetworkServiceProvider.CARD_SUFFIX;
        }
    }

    public String getDate() {
        if (dates == null || dates.size() == 0) {
            return "";
        }

        long d = dates.get(0);

        return dateFormat.format(new Date(d));
    }

    public boolean isCardFree() {
        if (TextUtils.isEmpty(cardType)) return true;

        if (cardType.toLowerCase().equals("free")) {
            return true;
        }

        return false;
    }
}
