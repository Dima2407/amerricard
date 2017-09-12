package com.devtonix.amerricard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCreditResponse {

    @SerializedName("credit")
    @Expose
    private Credit credit;

    public Credit getCredit() {
        return credit;
    }

    public int getVipCoins(){
        return getCredit().getVip();
    }

    public int getPremiumCoins(){
        return getCredit().getPremium();
    }

    public class Credit {

        @SerializedName("vip")
        @Expose
        private int vip;
        @SerializedName("premium")
        @Expose
        private int premium;

        public int getVip() {
            return vip;
        }

        public int getPremium() {
            return premium;
        }
    }
}
