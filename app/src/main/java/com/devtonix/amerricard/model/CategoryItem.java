
package com.devtonix.amerricard.model;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.utils.AmazonUtils.GetS3Object;
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
//    @Expose(deserialize = false, serialize = false)
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
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @Expose
    @SerializedName("thumbnails")
    private Thumbnail[] thumbnails;

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



    public List<CategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(List<CategoryItem> categoryItems) {
        this.categoryItems = categoryItems;
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    public GlideUrl getGlideImageUrl() {
        return GetS3Object.fromS3toUrl(imageUrl);
    }

    public GlideUrl getThumbImageUrl(){
        if(thumbnails == null || thumbnails.length == 0 && thumbnails[0] == null){
            return getGlideImageUrl();
        }else {
            return GetS3Object.fromS3toUrl(thumbnails[0].imageUrl);
        }
    }

    private static class Thumbnail {
        @Expose
        @SerializedName("width")
        private int width;
        @Expose
        @SerializedName("height")
        private int height;
        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;
    }
}
