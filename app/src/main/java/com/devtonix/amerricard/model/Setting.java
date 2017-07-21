package com.devtonix.amerricard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("birthdayCategoryId")
    @Expose
    private String birthdayCategoryId;

    public String getBirthdayCategoryId() {
        return birthdayCategoryId;
    }

    public void setBirthdayCategoryId(String birthdayCategoryId) {
        this.birthdayCategoryId = birthdayCategoryId;
    }
}
