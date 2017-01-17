package com.devtonix.amerricards.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.devtonix.amerricards.AmerriCardsApp;

public class Preferences {
    private static Preferences instance;
    private SharedPreferences sharedPreferences;

    public interface Fields {
        String FIRST_TIME = "first_time";
        String LOGGED_IN = "loggedIn";
        String USER_ID = "userId";
        String TOKEN = "token";
    }

    private Preferences() {
        String name = AmerriCardsApp.getInstance().getPackageName();
        sharedPreferences = AmerriCardsApp.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return getBoolean(Fields.LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        set(Fields.LOGGED_IN, loggedIn);
    }

    public long getUserId() {
        return getLong(Fields.USER_ID, 0);
    }

    public void setUserId(long userId) {
        set(Fields.USER_ID, userId);
    }

    public String getToken() {
        return getString(Fields.TOKEN, "");
    }

    public void setToken(String token) {
        set(Fields.TOKEN, token);
    }

    public boolean isFirstTime() {
        return getBoolean(Fields.FIRST_TIME, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        set(Fields.FIRST_TIME, isFirstTime);
    }


    /**-------------------------------------------------------------------------------------------*/
    private String getString(String key) {
        return getString(key, null);
    }

    private String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    private boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    private boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    private long getLong(String key) {
        return getLong(key, 0L);
    }

    private long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    private void set(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void set(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void set(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
}

