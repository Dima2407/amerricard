
package com.devtonix.amerricard.model;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.utils.AmazonUtils.GetS3Object;
import com.devtonix.amerricard.utils.RegexDateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class EventItem extends BaseEvent {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("dates")
    @Expose
    private List<Long> dates = null;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnail [] thumbnails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getDates() {
        return dates;
    }

    public void setDates(List<Long> dates) {
        this.dates = dates;
    }

    public String getFormattedDate() {
        if (dates == null || dates.size() == 0) {
            return "";
        }

        long d = dates.get(0);

        return RegexDateUtils.GODLIKE_APPLICATION_DATE_FORMAT.format(new Date(d));
    }

    @Override
    public String getEventDate() {
        return getFormattedDate();
    }

    @Override
    public Name getEventName() {
        return getName();
    }

    @Override
    public int getEventType() {
        return TYPE_EVENT;
    }

    @Override
    public GlideUrl getEventImage() {
        return GetS3Object.fromS3toUrl(imageUrl);
    }

    @Override
    public GlideUrl getThumbImageUrl(){
        if(thumbnails == null || thumbnails.length == 0 || thumbnails[0] == null){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventItem eventItem = (EventItem) o;

        return id.equals(eventItem.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
