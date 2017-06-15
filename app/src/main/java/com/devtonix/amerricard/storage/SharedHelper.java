package com.devtonix.amerricard.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.ListCategoryItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedHelper {

    private SharedPreferences sharedPreferences;

    public interface Fields {
        String FIRST_TIME = "first_time";
        String LOGGED_IN = "loggedIn";
        String USER_ID = "userId";
        String TOKEN = "token";
        String CARDS = "cards";
        String EVENTS = "events";
        String EVENTS_FOR_HIDE = "events_for_hide";
        String FAVORITES = "favorites";
        String LANGUAGE = "language";
        String NOTIFICATION = "notification";
        String NOTIFICATION_TIME = "notification_time";
        String CONTACTS = "contacts";
        String CELEBRITIES = "celebrities";
    }

    public SharedHelper(Context context) {
        ACApplication.getMainComponent().inject(this);

        final String name = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void saveCards(List<CategoryItemFirstLevel> items) {
        sharedPreferences.edit().putString(Fields.CARDS, new Gson().toJson(new ListCategoryItem(items))).apply();
    }

    public List<CategoryItemFirstLevel> getCards() {
        ListCategoryItem listCategoryItem = new Gson().fromJson(sharedPreferences.getString(Fields.CARDS, ""), ListCategoryItem.class);
        return listCategoryItem == null ? new ArrayList<CategoryItemFirstLevel>() : listCategoryItem.data;
    }
//
//    public void saveContacts(List<Contact> contacts){
//        set(Fields.CONTACTS, new Gson().toJson(new ListContact(contacts)));
//    }
//
//    public List<Contact> getContacts(){
//        ListContact listContact = new Gson().fromJson(getString(Fields.CONTACTS), ListContact.class);
//        return listContact == null ? new ArrayList<Contact>() : listContact.contacts;
//    }
//

//
//    public void saveEvents(List<Item> items) {
//        set(Fields.EVENTS, new Gson().toJson(new ListCategoryItem(items)));
//    }
//
//    public List<Item> getEvents() {
//        ListCategoryItem li = new Gson().fromJson(getString(Fields.EVENTS), ListCategoryItem.class);
//        return li == null ? new ArrayList<Item>() : li.data;
//    }
//
//    public void saveEventsForHide(List<Item> items) {
//        setForHide(Fields.EVENTS_FOR_HIDE, new Gson().toJson(new ListCategoryItem(items)));
//    }
//
//    public List<Item> getEventsForHide() {
//        ListCategoryItem listCategoryItem = new Gson().fromJson(getString(Fields.EVENTS_FOR_HIDE), ListCategoryItem.class);
//        return listCategoryItem == null ? new ArrayList<Item>() : listCategoryItem.data;
//    }
//
//    public void saveFavorites(List<Item> items) {
//        set(Fields.FAVORITES, new Gson().toJson(new ListCategoryItem(items)));
//    }
//
//    public List<Item> getFavorites() {
//        ListCategoryItem li = new Gson().fromJson(getString(Fields.FAVORITES), ListCategoryItem.class);
//        return li == null ? new ArrayList<Item>() : li.data;
//    }

    public void saveNotificationsTime(String time) {
        sharedPreferences.edit().putString(Fields.NOTIFICATION_TIME, time).apply();
    }

    public String getNotificationsTime() {
        return sharedPreferences.getString(Fields.NOTIFICATION_TIME, "08:00");
    }

    public void setNotification(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(Fields.NOTIFICATION, isEnabled).apply();
    }

    public boolean getNotification() {
        return sharedPreferences.getBoolean(Fields.NOTIFICATION, true);
    }

    public void setCelebrities(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(Fields.CELEBRITIES, isEnabled).apply();
    }

    public boolean getCelebrities() {
        return sharedPreferences.getBoolean(Fields.CELEBRITIES, true);
    }

    public void setLanguage(String token) {
        sharedPreferences.edit().putString(Fields.LANGUAGE, token).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(Fields.LANGUAGE, "");
    }


}


