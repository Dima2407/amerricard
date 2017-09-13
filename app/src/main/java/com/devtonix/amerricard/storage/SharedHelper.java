package com.devtonix.amerricard.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.Celebrity;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.model.ListCardItem;
import com.devtonix.amerricard.model.ListCategoryItem;
import com.devtonix.amerricard.model.ListCelebrities;
import com.devtonix.amerricard.model.ListContact;
import com.devtonix.amerricard.model.ListEventItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SharedHelper{

    private static final String USER_STATUS_VIP = "VIP";
    private static final String USER_STATUS_PREMIUM = "PREMIUM";
    private static final String USER_STATUS_NORMAL = "NORMAL";
    private static final String TAG = SharedHelper.class.getSimpleName();
    private File cacheDirectory;
    private List<CategoryItem> categoryItemList = null;
    private List<Contact> contactsList = null;
    private List<EventItem> eventItemList = null;
    private List<Celebrity> celebrityList = null;
    private List<CardItem> favoritesList = null;

    private SharedPreferences sharedPreferences;

    public interface Fields {
        String FIRST_TIME = "first_time";
        String FIRST_LAUNCH_APPLICATION = "first_launch_application";
        String LOGGED_IN = "loggedIn";
        String USER_ID = "userId";
        String USER_STATUS = "NORMAL";
        String CARDS = "cards";
        String EVENTS = "events";
        String EVENTS_FOR_HIDE = "events_for_hide";
        String FAVORITES = "favorites";
        String LANGUAGE = "language";
        String NOTIFICATION = "notification";
        String NOTIFICATION_TIME = "notification_time";
        String CONTACTS = "contacts";
        String CELEBRITIES = "celebrities";
        String LIST_OF_CELEBRITIES = "list_of_celebrities";
        String CONTACS_FOR_HIDE = "contacs_for_hide";
        String USE_FIRST_SERVER = "use_first_server";
        String DISPLAY_WIDTH = "display_width";
        String DISPLAY_HIGHT = "display_hight";
        String BIRTHDAY_CATEGORY_ID = "birthday_category_id";
        String ACCESS_TOKEN = "access_token";
        String NAME = "name";
        String EMAIL = "email";
        /*private int valueVipCoin;
        private int valuePremiumCoin;*/

        String VALUE_VIP_COINS = "value_vip_coin";
        String VALUE_PREMIUM_COINS = "value_premium_coin";

    }

    public SharedHelper(Context context) {
        ACApplication.getMainComponent().inject(this);

        final String name = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        cacheDirectory = context.getCacheDir();
    }

    private void fromGsonToCacheFile(Object items, String fileName) {
        File f = new File(cacheDirectory, fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(f);
            Gson gson = new Gson();
            gson.toJson(items, fileWriter);

        } catch (Exception e) {
            Log.w(TAG, "saveCards: ", e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                Log.w(TAG, "saveCards: ", e);
            }
        }
    }

    public void setValueVipCoins(int vipCoins) {
        sharedPreferences.edit().putInt(Fields.VALUE_VIP_COINS, vipCoins).apply();
    }

    public void setValuePremiumCoins(int premiumCoins) {
        sharedPreferences.edit().putInt(Fields.VALUE_PREMIUM_COINS, premiumCoins).apply();
    }

    public int getValuePremiumCoins() {
        return sharedPreferences.getInt(Fields.VALUE_PREMIUM_COINS, 0);
    }

    public int getValueVipCoins() {
        return sharedPreferences.getInt(Fields.VALUE_VIP_COINS, 0);
    }

    public void saveCards(List<CategoryItem> items) {
        categoryItemList = new ArrayList<>(items);
        fromGsonToCacheFile(new ListCategoryItem(items), Fields.CARDS);
    }

    public List<CategoryItem> getCards() {
        if (categoryItemList != null) {
            return categoryItemList;
        }
        File f = new File(cacheDirectory, Fields.CARDS);
        if (f.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                Gson gson = new Gson();
                ListCategoryItem categoryItem = gson.fromJson(fr, ListCategoryItem.class);
                if (categoryItem != null) {
                    categoryItemList = new ArrayList<>(categoryItem.data);
                    return categoryItemList;
                }
            } catch (Exception e) {
                Log.w(TAG, "getCards: ", e);
            } finally {
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (Exception e) {
                    Log.w(TAG, "getCards: ", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public void saveContacts(List<Contact> contacts) {
        contactsList = new ArrayList<>(contacts);
        fromGsonToCacheFile(new ListContact(contacts), Fields.CONTACTS);
    }

    public List<Contact> getContacts() {
        if (contactsList != null) {
            return contactsList;
        }

        File f = new File(cacheDirectory, Fields.CONTACTS);
        if (f.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                Gson gson = new Gson();
                ListContact listContact = gson.fromJson(fr, ListContact.class);
                if (listContact != null) {
                    contactsList = new ArrayList<>(listContact.getListContacs());
                    return contactsList;
                }
            } catch (FileNotFoundException e) {
                Log.w(TAG, "getContacts: ", e);
            } finally {
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException e) {
                    Log.w(TAG, "getContacts: ", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public void saveEvents(List<EventItem> items) {
        eventItemList = new ArrayList<>(items);
        fromGsonToCacheFile(new ListEventItem(items), Fields.EVENTS);
    }

    public List<EventItem> getEvents() {
        if (eventItemList != null) {
            return eventItemList;
        }

        File f = new File(cacheDirectory, Fields.EVENTS);
        if (f.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                Gson gson = new Gson();
                ListEventItem listEventItem = gson.fromJson(fr, ListEventItem.class);
                if (listEventItem != null) {
                    eventItemList = new ArrayList<>(listEventItem.getEvents());
                    return eventItemList;
                }
            } catch (FileNotFoundException e) {
                Log.w(TAG, "getEvents: ", e);
            } finally {
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException e) {
                    Log.w(TAG, "getEvents: ", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public void saveCelebrities(List<Celebrity> celebrities) {
        celebrityList = new ArrayList<>(celebrities);
        fromGsonToCacheFile(new ListCelebrities(celebrities), Fields.CELEBRITIES);
    }

    public List<Celebrity> getCelebrities() {
        if (celebrityList != null) {
            return celebrityList;
        }

        File f = new File(cacheDirectory, Fields.CELEBRITIES);
        if (f.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                Gson gson = new Gson();
                ListCelebrities listCelebrities = gson.fromJson(fr, ListCelebrities.class);
                if (listCelebrities != null) {
                    celebrityList = new ArrayList<>(listCelebrities.getCelebrities());
                    return celebrityList;
                }
            } catch (FileNotFoundException e) {
                Log.w(TAG, "getCelebrities: ", e);
            } finally {
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException e) {
                    Log.w(TAG, "getCelebrities: ", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public void saveEventsForHide(List<EventItem> items) {
        sharedPreferences.edit().putString(Fields.EVENTS_FOR_HIDE, new Gson().toJson(new ListEventItem(items))).apply();
    }

    public List<EventItem> getEventsForHide() {
        ListEventItem events = new Gson().fromJson(sharedPreferences.getString(Fields.EVENTS_FOR_HIDE, ""), ListEventItem.class);
        return events == null ? new ArrayList<EventItem>() : events.getEvents();
    }

    public void saveContacsForHide(List<Contact> contacts) {
        sharedPreferences.edit().putString(Fields.CONTACS_FOR_HIDE, new Gson().toJson(new ListContact(contacts))).apply();
    }

    public void addContactForHide(Contact contact) {
        List<Contact> contacts = getContactsForHide();
        contacts.add(contact);
        saveContacsForHide(contacts);
    }

    public void removeContactFromHidden(Contact contact) {
        List<Contact> contacts = getContactsForHide();
        for (Contact c : contacts) {
            if (c.equals(contact)) {
                contacts.remove(contact);
                break;
            }
        }
        saveContacsForHide(contacts);
    }

    public List<Contact> getContactsForHide() {
        ListContact contacts = new Gson().fromJson(sharedPreferences.getString(Fields.CONTACS_FOR_HIDE, ""), ListContact.class);
        return contacts == null ? new ArrayList<Contact>() : contacts.getListContacs();
    }

    public void saveFavorites(List<CardItem> items) {
        favoritesList = new ArrayList<>(items);
        fromGsonToCacheFile(new ListCardItem(items), Fields.FAVORITES);
    }

    public List<CardItem> getFavorites() {
        if (favoritesList != null) {
            return favoritesList;
        }

        File f = new File(cacheDirectory, Fields.FAVORITES);
        if (f.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                Gson gson = new Gson();
                ListCardItem listCardItem = gson.fromJson(fr, ListCardItem.class);
                if (listCardItem != null) {
                    favoritesList = new ArrayList<>(listCardItem.getCards());
                    return favoritesList;
                }
            } catch (FileNotFoundException e) {
                Log.w(TAG, "getFavorites: ", e);
            } finally {
                try {
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException e) {
                    Log.w(TAG, "getFavorites: ", e);
                }
            }
        }
        return new ArrayList<>();
    }

    public void saveNotificationsTime(String time) {
        sharedPreferences.edit().putString(Fields.NOTIFICATION_TIME, time).apply();
    }

    public String getNotificationsTime() {
        return sharedPreferences.getString(Fields.NOTIFICATION_TIME, "19:06");
    }

    public void setNotification(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(Fields.NOTIFICATION, isEnabled).apply();
    }

    public boolean getNotification() {
        return sharedPreferences.getBoolean(Fields.NOTIFICATION, true);
    }

    public void setCelebritiesInSettings(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(Fields.CELEBRITIES, isEnabled).apply();
    }

    public boolean getCelebritiesInSettings() {
        return sharedPreferences.getBoolean(Fields.CELEBRITIES, false);
    }

    public void setLanguage(String token) {
        sharedPreferences.edit().putString(Fields.LANGUAGE, token).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(Fields.LANGUAGE, Locale.getDefault().getLanguage());
    }

    public void setFirstLaunchApplication(boolean isFirstLaunch) {
        sharedPreferences.edit().putBoolean(Fields.FIRST_LAUNCH_APPLICATION, isFirstLaunch).apply();
    }

    public boolean isFirstLaunchApplication() {
        return sharedPreferences.getBoolean(Fields.FIRST_LAUNCH_APPLICATION, true);
    }

    public boolean isVipOrPremium() {
        return isPremium() || isVip();
    }

    public boolean isPremium() {
        return getValuePremiumCoins() > 0;
    }

    public boolean isVip() {
        return getValueVipCoins() > 0;
    }

    public boolean getCurrentServer() {
        return sharedPreferences.getBoolean(Fields.USE_FIRST_SERVER, true);
    }

    public void setServer(boolean isFirst) {
        sharedPreferences.edit().putBoolean(Fields.USE_FIRST_SERVER, isFirst).apply();
    }

    public void saveDisplaySize(int width, int hight) {
        sharedPreferences.edit().putInt(Fields.DISPLAY_WIDTH, width).apply();
        sharedPreferences.edit().putInt(Fields.DISPLAY_HIGHT, hight).apply();
    }

    public int getDisplayWidth() {
        return sharedPreferences.getInt(Fields.DISPLAY_WIDTH, 0);
    }

    public int getDisplayHight() {
        return sharedPreferences.getInt(Fields.DISPLAY_HIGHT, 0);
    }


    public void saveBithrayCategoryId(int id) {
        sharedPreferences.edit().putInt(Fields.BIRTHDAY_CATEGORY_ID, id).apply();
    }

    public int getBithrayCategoryId() {
        return sharedPreferences.getInt(Fields.BIRTHDAY_CATEGORY_ID, 0);
    }

    public void setAccessToken(String token) {
        sharedPreferences.edit().putString(Fields.ACCESS_TOKEN, token).apply();
        Log.d("MyLog", "setAccessToken: ");
    }

    public String getAccessToken() {
        return sharedPreferences.getString(Fields.ACCESS_TOKEN, "");
    }

    public void cleanAccessToken() {
        sharedPreferences.edit().putString(Fields.ACCESS_TOKEN, "").apply();
        sharedPreferences.edit().putString(Fields.NAME, "").apply();
        sharedPreferences.edit().putString(Fields.EMAIL, "").apply();
        sharedPreferences.edit().putInt(Fields.VALUE_VIP_COINS, 0).apply();
        sharedPreferences.edit().putInt(Fields.VALUE_PREMIUM_COINS, 0).apply();
    }

    public String getName() {
        return sharedPreferences.getString(Fields.NAME, "");
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(Fields.NAME, name).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Fields.EMAIL, "");
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(Fields.EMAIL, email).apply();
    }


}


