package com.devtonix.amerricard.core;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.devtonix.amerricard.utils.LanguageUtils;

import io.fabric.sdk.android.Fabric;

public class ACApplication extends Application {

    private static MainComponent mainComponent;

    public static MainComponent getMainComponent() {
        return mainComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        mainComponent = DaggerMainComponent.builder().appModule(new AppModule(this)).build();

        // Facebook init
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    }
}
