package com.devtonix.amerricard.core;

import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.network.NetworkModule;
import com.devtonix.amerricard.receivers.HolidaysBroadcastReceiver;
import com.devtonix.amerricard.repository.CardRepository;
import com.devtonix.amerricard.repository.CelebrityRepository;
import com.devtonix.amerricard.repository.ContactRepository;
import com.devtonix.amerricard.repository.EventRepository;
import com.devtonix.amerricard.repository.RepositoryModule;
import com.devtonix.amerricard.repository.SettingsRepository;
import com.devtonix.amerricard.repository.UserRepository;
import com.devtonix.amerricard.services.HolidaysNotificationService;
import com.devtonix.amerricard.storage.SharedHelper;
import com.devtonix.amerricard.storage.SharedHelperModule;
import com.devtonix.amerricard.ui.activity.BaseActivity;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.activity.CreateBirthdayActivity;
import com.devtonix.amerricard.ui.activity.DetailActivity;
import com.devtonix.amerricard.ui.activity.FavoriteActivity;
import com.devtonix.amerricard.ui.activity.MainActivity;
import com.devtonix.amerricard.ui.activity.ManageActivity;
import com.devtonix.amerricard.ui.activity.SettingsActivity;
import com.devtonix.amerricard.ui.fragment.BaseFragment;
import com.devtonix.amerricard.ui.fragment.CalendarFragment;
import com.devtonix.amerricard.ui.fragment.CardFragment;
import com.devtonix.amerricard.ui.fragment.CategoryFragment;
import com.devtonix.amerricard.ui.fragment.PremiumFragment;
import com.devtonix.amerricard.ui.fragment.SelecteCategoryFragment;
import com.devtonix.amerricard.ui.fragment.VipAndPremiumAbstractFragment;
import com.devtonix.amerricard.ui.fragment.VipFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, SharedHelperModule.class})
public interface MainComponent {

    /**
     * Activity
     */
    void inject(BaseActivity baseActivity);
    void inject(DetailActivity detailActivity);
    void inject(CategoryActivity categoryActivity);
    void inject(FavoriteActivity favoriteActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(CreateBirthdayActivity createBirthdayActivity);
    void inject(ManageActivity manageActivity);

    /**
     * Fragment
     */
    void inject(BaseFragment baseFragment);
    void inject(CategoryFragment categoryFragment);
    void inject(CalendarFragment calendarFragment);
    void inject(CardFragment cardFragment);
    void inject(VipAndPremiumAbstractFragment vipAndPremiumAbstractFragment);
    void inject(SelecteCategoryFragment selecteCategoryFragment);

    /**
     * Other
     */
    void inject(CardRepository cardRepository);
    void inject(EventRepository eventRepository);
    void inject(CelebrityRepository celebrityRepository);
    void inject(SharedHelper sharedHelper);
    void inject(HolidaysNotificationService holidaysNotificationService);
    void inject(HolidaysBroadcastReceiver holidaysBroadcastReceiver);
    void inject(ContactRepository contactRepository);
    void inject(NetworkModule module);
    void inject(SettingsRepository settingsRepository);
    void inject(UserRepository userRepository);
}
