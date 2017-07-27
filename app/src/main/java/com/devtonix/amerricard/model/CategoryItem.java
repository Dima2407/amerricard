
package com.devtonix.amerricard.model;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.utils.AmazonUtils.GetS3Object;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.devtonix.amerricard.model.CardResponseItem.Thumbnail;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem {

    @SerializedName("type")
    @Expose
    private String type;
    //-------------------------------
    @SerializedName("data")
    @Expose
    private JsonElement data = null;
    //-------------------------------


    //-------------------------------
//    @Expose(deserialize = false, serialize = false)
    private List<CategoryItem> categoryItems = new ArrayList<>();
    //-------------------------------
    private List<CardItem> cardItems = new ArrayList<>();
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

    public CategoryItem(CardResponseItem cardResponseItem) {
        name = cardResponseItem.getCategoryName();
        order = cardResponseItem.order;
        id = cardResponseItem.id;
        image = cardResponseItem.image;
        imageUrl = cardResponseItem.imageUrl;
        thumbnails = cardResponseItem.thumbnails;
        cardItems = new ArrayList<>(cardResponseItem.data);
    }

    public String getType() {
        return type;
    }

    public JsonElement getData() {
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
        if (categoryItems == null) {
            categoryItems = new ArrayList<>();
        }
        return categoryItems;
    }

    public void addCategoryItem(CategoryItem categoryItem) {
        if (categoryItems == null) {
            categoryItems = new ArrayList<>();
        }
        categoryItems.add(categoryItem);
    }

    public List<CardItem> getCardItems() {
        if (cardItems == null) {
            cardItems = new ArrayList<>();
        }
        return cardItems;
    }

    public void addCardItem(CardItem cardItem) {
        if (cardItems == null) {
            cardItems = new ArrayList<>();
        }
        this.cardItems.add(cardItem);
    }

    public GlideUrl getGlideImageUrl() {
        return GetS3Object.fromS3toUrl(imageUrl);
    }

    public GlideUrl getThumbImageUrl() {
        if (thumbnails == null || thumbnails.length == 0 && thumbnails[0] == null) {
            return getGlideImageUrl();
        } else {
            return GetS3Object.fromS3toUrl(thumbnails[0].imageUrl);
        }
    }

}
