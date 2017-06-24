
package com.devtonix.amerricard.model;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class CategoryItemSecondLevel {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private List<CardItem> data = null;
    //----------------------
    @SerializedName("name")
    @Expose
    private JsonElement name; //or JsonElement
    //----------------------
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

    public void setType(String type) {
        this.type = type;
    }

    public List<CardItem> getData() {
        return data;
    }

    public void setData(List<CardItem> data) {
        this.data = data;
    }

    public JsonElement getNameJsonEl() {
        return name;
    }

    public void setName(JsonElement name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
