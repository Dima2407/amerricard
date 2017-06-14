
package com.devtonix.amerricard.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItemFirstLevel {

    @SerializedName("type")
    @Expose
    private String type;
//    @SerializedName("data")
//    @Expose
//    private List<CategoryItemSecondLevel> data = null;
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

    public void setType(String type) {
        this.type = type;
    }

//    public List<CategoryItemSecondLevel> getData() {
//        return data;
//    }
//
//    public void setData(List<CategoryItemSecondLevel> data) {
//        this.data = data;
//    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
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
