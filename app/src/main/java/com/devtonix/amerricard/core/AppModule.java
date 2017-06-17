package com.devtonix.amerricard.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.devtonix.amerricard.storage.SharedHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Context context() {
        return application.getApplicationContext();
    }
}
