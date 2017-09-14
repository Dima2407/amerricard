package com.devtonix.amerricard.utils;

/**
 * Created by nikolay on 14.09.17.
 */

public class BillingUtils {

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;


    public static String getError(int code){
        switch (code){
            case BILLING_RESPONSE_RESULT_USER_CANCELED:
                return "User pressed back or canceled a dialog";
            case BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE:
                return "Network connection is down";
            case BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE:
                return "Billing API version is not supported for the type requested";
            case BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:
                return "Requested product is not available for purchase";
            case BILLING_RESPONSE_RESULT_DEVELOPER_ERROR:
                return "Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed or properly set up for In-app Billing in Google Play, or does not have the necessary permissions in its manifest";
            case BILLING_RESPONSE_RESULT_ERROR:
                return "Fatal error during the API action";
            case BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
                return "Failure to purchase since item is already owned";
            case BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED:
                return "Failure to consume since item is not owned";

        }
        return "OK";

    }
}
