package com.devtonix.amerricards;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }
}
