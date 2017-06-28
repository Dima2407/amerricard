package com.devtonix.amerricard.network.response;

import com.devtonix.amerricard.model.Celebrity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CelebrityResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Celebrity> data = null;

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public List<Celebrity> getData() {
        return data;
    }
}
