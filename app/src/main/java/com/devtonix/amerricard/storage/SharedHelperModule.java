package com.devtonix.amerricard.storage;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedHelperModule {

    @Singleton
    @Provides
    public SharedHelper getSharedHelper(Context context){
        return new SharedHelper(context);
    }

}
