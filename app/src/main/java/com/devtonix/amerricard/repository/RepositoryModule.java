package com.devtonix.amerricard.repository;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    public EventRepository getEventRepository(Context context) {
        return new EventRepository(context);
    }

    @Singleton
    @Provides
    public CardRepository getCardRepository(Context context) {
        return new CardRepository(context);
    }
}