package com.devtonix.amerricard.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("data")
    @Expose
    private DataResponse data;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private Integer code;

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


