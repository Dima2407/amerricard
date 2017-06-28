
package com.devtonix.amerricard.model;

import com.devtonix.amerricard.utils.RegexDateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class EventItem implements BaseEvent {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("dates")
    @Expose
    private List<Long> dates = null;

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

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
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
    public String getEventName() {
        return getName().getEn(); //todo fix Name !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}
