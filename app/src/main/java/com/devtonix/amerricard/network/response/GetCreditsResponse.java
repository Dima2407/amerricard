package com.devtonix.amerricard.network.response;

import com.devtonix.amerricard.model.Credit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCreditsResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Credit data;

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

    public Credit getData() {
        return data;
    }

    public void setData(Credit data) {
        this.data = data;
    }
}
