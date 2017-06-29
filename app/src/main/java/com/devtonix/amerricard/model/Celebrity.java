package com.devtonix.amerricard.model;

import com.devtonix.amerricard.network.NetworkModule;
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
    private Object imageUrl;

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
    public String getEventImage() {
        return NetworkModule.BASE_URL + "celebrity/" + getId() + "/image?width=100&height=200&type=fit";
    }
}
