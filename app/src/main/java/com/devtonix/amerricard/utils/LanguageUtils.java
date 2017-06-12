package com.devtonix.amerricard.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Name;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Oleksii on 11.05.17.
 */
public class LanguageUtils {
    public static void setupLanguage(Context context) {
        Log.d("LanguageUtils", "setupLanguage");
        setLanguage(context, getLanguage());
    }

    public static void setLanguage(Context context, String lang) {
        if (TextUtils.isEmpty(lang)) lang = "en";
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm);
        Preferences.getInstance().setLanguage(lang.toLowerCase());
    }

    public static String getLanguage() {
        String lang = "en";
        if (Preferences.getInstance().getLanguage().isEmpty()) {
            lang = Locale.getDefault().getLanguage();
        } else {
            lang = Preferences.getInstance().getLanguage();
        }
        Log.d("LanguageUtils", "language:" + lang);
        return lang;
    }

//    public static String getCardNameAccordingLang(Name name) {
//        final String currentLang = getLanguage();
//
//        if (TextUtils.equals("en", currentLang)) {
//            return name.getEn();
//        } else if (TextUtils.equals("fr", currentLang)) {
//            return name.getFr();
//        } else if (TextUtils.equals("es", currentLang)) {
//            return name.getEs();
//        } else if (TextUtils.equals("ru", currentLang)) {
//            return name.getRu();
//        } else {
//            return name.getUa();
//        }
//    }

    public static String getCardNameAccordingLang(JsonObject object) {
        return object.toString();
    }

    public static int getLanguagePositionInList(Context context) {
        List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));
        return list.indexOf(Preferences.getInstance().getLanguage());
    }

    public static String getLanguageByCode(Context context, String language) {
        List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));
        int index = list.indexOf(language);
        String[] langs = context.getResources().getStringArray(R.array.languages);
        return langs[index];
    }
}

