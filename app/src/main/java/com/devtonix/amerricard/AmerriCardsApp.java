package com.devtonix.amerricard;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.devtonix.amerricard.utils.LanguageUtils;

import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());

        LanguageUtils.setupLanguage(this);

        // Facebook init
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

    }
}
