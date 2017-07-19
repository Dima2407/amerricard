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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SharedHelper {

    private static final String USER_STATUS_VIP = "vip";
    private static final String USER_STATUS_PREMIUM = "premium";
    private static final String USER_STATUS_NORMAL = "normal";

    private SharedPreferences sharedPreferences;

    public interface Fields {
        String FIRST_TIME = "first_time";
        String FIRST_LAUNCH_APPLICATION = "first_launch_application";
        String LOGGED_IN = "loggedIn";
        String USER_ID = "userId";
        String TOKEN = "token";
        String USER_STATUS = "normal";
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
        return listContact == null ? new ArrayList<Contact>() : listContact.getListContacs();
    }

    public void saveEvents(List<EventItem> items) {
        sharedPreferences.edit().putString(Fields.EVENTS, new Gson().toJson(new ListEventItem(items))).apply();
    }

    public List<EventItem> getEvents() {
        ListEventItem li = new Gson().fromJson(sharedPreferences.getString(Fields.EVENTS, ""), ListEventItem.class);
        return li == null ? new ArrayList<EventItem>() : li.getEvents();
    }

    public void saveCelebrities(List<Celebrity> celebrities) {
        sharedPreferences.edit().putString(Fields.LIST_OF_CELEBRITIES, new Gson().toJson(new ListCelebrities(celebrities))).apply();
    }

    public List<Celebrity> getCelebrities() {
        ListCelebrities celebrities = new Gson().fromJson(sharedPreferences.getString(Fields.LIST_OF_CELEBRITIES, ""), ListCelebrities.class);
        return celebrities == null ? new ArrayList<Celebrity>() : celebrities.getCelebrities();
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
        List<Contact> contacts = getContacsForHide();
        contacts.add(contact);
        saveContacsForHide(contacts);
    }

    public void removeContactFromHidden(Contact contact) {
        List<Contact> contacts = getContacsForHide();
        for (Contact c : contacts) {
            if (c.equals(contact)) {
                contacts.remove(contact);
                break;
            }
        }
        saveContacsForHide(contacts);
    }

    public List<Contact> getContacsForHide() {
        ListContact contacts = new Gson().fromJson(sharedPreferences.getString(Fields.CONTACS_FOR_HIDE, ""), ListContact.class);
        return contacts == null ? new ArrayList<Contact>() : contacts.getListContacs();
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
        return sharedPreferences.getBoolean(Fields.CELEBRITIES, true);
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

    public void setUserStatus(String status) {
        sharedPreferences.edit().putString(Fields.USER_STATUS, status).apply();
    }

    public boolean isVipOrPremium() {
        return sharedPreferences.getString(Fields.USER_STATUS, USER_STATUS_NORMAL).equals(USER_STATUS_VIP) ||
                sharedPreferences.getString(Fields.USER_STATUS, USER_STATUS_NORMAL).equals(USER_STATUS_PREMIUM);
    }

    public boolean isVip() {
        return sharedPreferences.getString(Fields.USER_STATUS, USER_STATUS_NORMAL).equals(USER_STATUS_VIP);
    }

    public boolean getCurrentServer() {
        return sharedPreferences.getBoolean(Fields.USE_FIRST_SERVER, true);
    }

    public void setServer(boolean isFirst) {
        sharedPreferences.edit().putBoolean(Fields.USE_FIRST_SERVER, isFirst).apply();
    }

    public void saveDisplayWidth(int width) {
        sharedPreferences.edit().putInt(Fields.DISPLAY_WIDTH, width).apply();
    }

    public int getDisplayWidth() {
        return sharedPreferences.getInt(Fields.DISPLAY_WIDTH, 0);
    }
}


