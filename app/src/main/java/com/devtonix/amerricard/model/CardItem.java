
package com.devtonix.amerricard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.utils.AmazonUtils.GetS3Object;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.devtonix.amerricard.model.CardResponseItem.Thumbnail;

public class CardItem implements Parcelable {

    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };
    public static final CardItem EMPTY = new CardItem();

    private static final String TYPE_VIP = "VIP";
    private static final String TYPE_PREMIUM = "PREMIUM";

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("favorite")
    @Expose
    private Integer favorite;
    @SerializedName("shared")
    @Expose
    private Integer shared;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;
    @Expose
    @SerializedName("thumbnails")
    private Thumbnail[] thumbnails;

    public CardItem(CardResponseItem cardResponseItem) {
        type = cardResponseItem.type;
        name = cardResponseItem.getCardName();
        author = cardResponseItem.author;
        owner = cardResponseItem.owner;
        price = cardResponseItem.price;
        favorite = cardResponseItem.favorite;
        shared = cardResponseItem.shared;
        cardType = cardResponseItem.cardType;
        id = cardResponseItem.id;
        image = cardResponseItem.image;
        imageUrl = cardResponseItem.imageUrl;
        thumbnails = cardResponseItem.thumbnails;
    }


    private CardItem(Parcel in) {
        type = in.readString();
        name = in.readString();
        author = in.readString();
        owner = in.readString();
        price = in.readInt();
        favorite = in.readInt();
        shared = in.readInt();
        cardType = in.readString();
        id = in.readInt();
        image = in.readString();
        imageUrl = in.readString();
    }

    public CardItem() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getShared() {
        return shared;
    }

    public void setShared(Integer shared) {
        this.shared = shared;
    }

    public String getCardType() {
        return cardType;
    }

    public boolean isVip(){
        return TextUtils.equals(getCardType(), TYPE_VIP);
    }

    public boolean isPremium(){
        return TextUtils.equals(getCardType(), TYPE_PREMIUM);
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(owner);
        dest.writeInt(price);
        dest.writeInt(favorite);
        dest.writeInt(shared);
        dest.writeString(cardType);
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(imageUrl);
    }

    public GlideUrl getGlideImageUrl() {
        return GetS3Object.fromS3toUrl(imageUrl);
    }

    public GlideUrl getThumbImageUrl(int width) {
        if (thumbnails == null || thumbnails.length == 0 && thumbnails[0] == null) {
            return getGlideImageUrl();
        } else {
            Thumbnail readyThumbnail = thumbnails[0];
            for (Thumbnail thumbnail : thumbnails) {
                if (Math.abs(thumbnail.width - width) < Math.abs(readyThumbnail.width - width)) {
                    readyThumbnail = thumbnail;
                }
            }
            return GetS3Object.fromS3toUrl(readyThumbnail.imageUrl);
        }
    }

    public boolean isFree() {
        return !(isPremium() || isVip());
    }
}
