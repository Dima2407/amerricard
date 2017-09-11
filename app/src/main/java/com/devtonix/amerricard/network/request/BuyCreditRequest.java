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
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("purchaseToken")
    @Expose
    private String purchaseToken;
    @SerializedName("purchaseTransactionId")
    @Expose
    private String purchaseTransactionId;
    @SerializedName("appType")
    @Expose
    private String appType;

    public BuyCreditRequest() {
        this.appType = "android";
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public void setPurchaseTransactionId(String purchaseTransactionId) {
        this.purchaseTransactionId = purchaseTransactionId;
    }
}
