package com.devtonix.amerricard.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyCreditRequest {
    @SerializedName("creditType")
    @Expose
    private String creditType;
    @SerializedName("credit")
    @Expose
    private int credits;
    @SerializedName("purchaseTrasactionId")
    @Expose
    private String purchaseTrasactionId;
    @SerializedName("appType")
    @Expose
    private String appType;

    public BuyCreditRequest(String creditType, int credits, String purchaseTrasactionId, String appType) {
        this.creditType = creditType;
        this.credits = credits;
        this.purchaseTrasactionId = purchaseTrasactionId;
        this.appType = appType;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getPurchaseTrasactionId() {
        return purchaseTrasactionId;
    }

    public void setPurchaseTrasactionId(String purchaseTrasactionId) {
        this.purchaseTrasactionId = purchaseTrasactionId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}
