package com.devtonix.amerricard;

import android.app.Application;

public class AmerriCardsApp extends Application {

    private static AmerriCardsApp instance;

    public AmerriCardsApp() {
        instance = this;
    }

    public static AmerriCardsApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Facebook init
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

    }
}
