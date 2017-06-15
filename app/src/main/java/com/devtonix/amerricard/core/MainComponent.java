package com.devtonix.amerricard.core;

import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.repository.RepositoryModule;
import com.devtonix.amerricard.storage.SharedHelperModule;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.activity.DetailActivity;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.ui.fragment.CardFragment;
import com.devtonix.amerricard.storage.SharedHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, SharedHelperModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);
    void inject(CardRepository cardRepository);
    void inject(EventRepository eventRepository);
    void inject(DetailActivity detailActivity);
    void inject(CardFragment cardFragment);
    void inject(SharedHelper sharedHelper);
    void inject(CategoryActivity categoryActivity);
}
