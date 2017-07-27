package com.devtonix.amerricard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class CardResponseItem {
    @SerializedName("type")
    @Expose
    protected String type;
    @SerializedName("name")
    @Expose
    protected Object name;
    @SerializedName("author")
    @Expose
    protected String author;
    @SerializedName("owner")
    @Expose
    protected String owner;
    @SerializedName("price")
    @Expose
    protected Integer price;
    @SerializedName("favorite")
    @Expose
    protected Integer favorite;
    @SerializedName("shared")
    @Expose
    protected Integer shared;
    @SerializedName("cardType")
    @Expose
    protected String cardType;
    @SerializedName("id")
    @Expose
    protected Integer id;
    @SerializedName("image")
    @Expose
    protected String image;
    @Expose
    @SerializedName("imageUrl")
    protected String imageUrl;
    @Expose
    @SerializedName("thumbnails")
    protected Thumbnail[] thumbnails;
    // category specific
    @SerializedName("order")
    @Expose
    protected Integer order;
    @SerializedName("data")
    @Expose
    protected List<CardItem> data = null;

    public String getCardName() {
        return String.valueOf(name);
    }

    public Name getCategoryName() {
        return new Name((Map<String , String>) name);
    }

    public String getType() {
        return type;
    }

    protected static class Thumbnail {
        @Expose
        @SerializedName("width")
        protected int width;
        @Expose
        @SerializedName("height")
        protected int height;
        @Expose
        @SerializedName("imageUrl")
        protected String imageUrl;
    }

}
