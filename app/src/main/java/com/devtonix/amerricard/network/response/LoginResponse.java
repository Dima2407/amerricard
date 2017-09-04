package com.devtonix.amerricard.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("data")
    @Expose
    private DataResponse data;

    public DataResponse getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }
}
