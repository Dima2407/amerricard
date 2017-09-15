package com.devtonix.amerricard.model;

public enum BillingProductType {
    VIP_1("vip", 1, "vip_1"),
    VIP_3("vip", 3, "vip_3"),
    VIP_5("vip", 5, "vip_5"),
    VIP_10("vip", 10, "vip_10"),
    PREMIUM_1("premium", 1, "premium_1"),
    PREMIUM_3("premium", 3, "premium_3"),
    PREMIUM_5("premium", 5, "premium_5"),
    PREMIUM_10("premium", 10, "premium_10_2");
    private String productId;
    private int credits;
    private String type;

    BillingProductType(String type, int credits, String productId) {
        this.type = type;
        this.credits = credits;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public int getCredits() {
        return credits;
    }

    public String getType() {
        return type;
    }
}
