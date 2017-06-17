
package com.devtonix.amerricard.network.response;

import java.util.List;

import com.devtonix.amerricard.model.EventItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<EventItem> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<EventItem> getData() {
        return data;
    }

    public void setData(List<EventItem> data) {
        this.data = data;
    }

}
