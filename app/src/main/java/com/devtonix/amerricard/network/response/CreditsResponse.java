package com.devtonix.amerricard.network.response;

import com.devtonix.amerricard.model.DataCreditResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class
CreditsResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private DataCreditResponse data;

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public DataCreditResponse getData() {
        return data;
    }
}
