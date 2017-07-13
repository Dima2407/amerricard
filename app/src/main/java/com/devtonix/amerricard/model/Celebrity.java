package com.devtonix.amerricard.model;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.utils.AmazonUtils.GetS3Object;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Celebrity implements BaseEvent{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("facebookProfile")
    @Expose
    private String facebookProfile;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("image")
    @Expose
    private Integer image;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnail [] thumbnails;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFacebookProfile() {
        return facebookProfile;
    }

    public Long getDate() {
        return date;
    }

    public Integer getImage() {
        return image;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public String getFormattedDate() {
        return RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.format(new Date(date));
    }

    @Override
    public String getEventDate() {
        return getFormattedDate();
    }

    @Override
    public Name getEventName() {
        Name name = new Name();
        name.setBaseName(getName());
        return name;
    }

    @Override
    public int getEventType() {
        return TYPE_CELEBRITY;
    }

    @Override
    public GlideUrl getEventImage() {
        return GetS3Object.fromS3toUrl(imageUrl);
    }

    @Override
    public GlideUrl getThumbImageUrl(){
        if(thumbnails == null || thumbnails.length == 0 && thumbnails[0] == null){
            return getEventImage();
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
