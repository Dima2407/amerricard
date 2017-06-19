
package com.devtonix.amerricard.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryItem {

    @SerializedName("type")
    @Expose
    private String type;
    //-------------------------------
    @SerializedName("data")
    @Expose
    private List<JsonElement> data = null;
    //-------------------------------


    //-------------------------------
    private List<CategoryItem> categoryItems = null;
    //-------------------------------
    private List<CardItem> cardItems = null;
    //-------------------------------

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;

    public String getType() {
        return type;
    }

    public List<JsonElement> getData() {
        return data;
    }

    public Name getName() {
        return name;
    }

    public Integer getOrder() {
        return order;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}
