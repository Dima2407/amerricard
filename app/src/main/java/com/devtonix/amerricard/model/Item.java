package com.devtonix.amerricard.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.devtonix.amerricard.api.NetworkServiceProvider;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Item implements Serializable {

    @SuppressLint("SimpleDateFormat")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (type != null ? !type.equals(item.type) : item.type != null) return false;
        if (data != null ? !data.equals(item.data) : item.data != null) return false;
        if (dates != null ? !dates.equals(item.dates) : item.dates != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (image != null ? !image.equals(item.image) : item.image != null) return false;
        if (author != null ? !author.equals(item.author) : item.author != null) return false;
        if (owner != null ? !owner.equals(item.owner) : item.owner != null) return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (likes != null ? !likes.equals(item.likes) : item.likes != null) return false;
        if (downloads != null ? !downloads.equals(item.downloads) : item.downloads != null)
            return false;
        return cardType != null ? cardType.equals(item.cardType) : item.cardType == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (dates != null ? dates.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        result = 31 * result + (downloads != null ? downloads.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        return result;
    }
}
