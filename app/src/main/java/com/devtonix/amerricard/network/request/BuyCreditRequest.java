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
    @SerializedName("purchaseTransactionId")
    @Expose
    private String purchaseTransactionId;
    @SerializedName("appType")
    @Expose
    private String appType;

    public BuyCreditRequest(String creditType, int credits, String purchaseTransactionId, String appType) {
        this.creditType = creditType;
        this.credits = credits;
        this.purchaseTransactionId = purchaseTransactionId;
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

    public String getPurchaseTransactionId() {
        return purchaseTransactionId;
    }

    public void setPurchaseTransactionId(String purchaseTransactionId) {
        this.purchaseTransactionId = purchaseTransactionId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}
