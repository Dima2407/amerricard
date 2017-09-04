package com.devtonix.amerricard.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    private DataResponse data;

    public DataResponse getData() {
        return data;
    }

    /*@SerializedName("data")
    @Expose
    private String[] data;

    @SerializedName("authToken")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }*/


}
