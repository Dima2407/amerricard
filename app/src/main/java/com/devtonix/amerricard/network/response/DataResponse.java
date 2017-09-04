package com.devtonix.amerricard.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponse {

    @SerializedName("authToken")
    @Expose
    private String token;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
