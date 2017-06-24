package com.devtonix.amerricard.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.devtonix.amerricard.core.ACApplication;
import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.devtonix.amerricard.model.CategoryItemFirstLevel;
import com.devtonix.amerricard.model.Contact;
import com.devtonix.amerricard.model.EventItem;
import com.devtonix.amerricard.model.ListCardItem;
import com.devtonix.amerricard.model.ListCategoryItem;
import com.devtonix.amerricard.model.ListContact;
import com.devtonix.amerricard.model.ListEventItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public void saveCards(List<CategoryItem> items) {
        sharedPreferences.edit().putString(Fields.CARDS, new Gson().toJson(new ListCategoryItem(items))).apply();
    }

    public List<CategoryItem> getCards() {
        ListCategoryItem listCategoryItem = new Gson().fromJson(sharedPreferences.getString(Fields.CARDS, ""), ListCategoryItem.class);
        return listCategoryItem == null ? new ArrayList<CategoryItem>() : listCategoryItem.data;
    }

    public void saveContacts(List<Contact> contacts) {
        sharedPreferences.edit().putString(Fields.CONTACTS, new Gson().toJson(new ListContact(contacts))).apply();
    }

    public List<Contact> getContacts() {
        ListContact listContact = new Gson().fromJson(sharedPreferences.getString(Fields.CONTACTS, ""), ListContact.class);
        return listContact == null ? new ArrayList<Contact>() : listContact.contacts;
    }

    public void saveEvents(List<EventItem> items) {
        sharedPreferences.edit().putString(Fields.EVENTS, new Gson().toJson(new ListEventItem(items))).apply();
    }

    public List<EventItem> getEvents() {
        ListEventItem li = new Gson().fromJson(sharedPreferences.getString(Fields.EVENTS, ""), ListEventItem.class);
        return li == null ? new ArrayList<EventItem>() : li.getEvents();
    }


    public void saveEventsForHide(List<EventItem> items) {
        sharedPreferences.edit().putString(Fields.EVENTS_FOR_HIDE, new Gson().toJson(new ListEventItem(items))).apply();
    }

    public List<EventItem> getEventsForHide() {
        ListEventItem events = new Gson().fromJson(sharedPreferences.getString(Fields.EVENTS_FOR_HIDE, ""), ListEventItem.class);
        return events == null ? new ArrayList<EventItem>() : events.getEvents();
    }

    public void saveFavorites(List<CardItem> items) {
        sharedPreferences.edit().putString(Fields.FAVORITES, new Gson().toJson(new ListCardItem(items))).apply();
    }

    public List<CardItem> getFavorites() {
        ListCardItem li = new Gson().fromJson(sharedPreferences.getString(Fields.FAVORITES, ""), ListCardItem.class);
        return li == null ? new ArrayList<CardItem>() : li.getCards();
    }

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
        return sharedPreferences.getString(Fields.LANGUAGE, Locale.getDefault().getLanguage());
    }


}


