package com.devtonix.amerricard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Name;
import com.devtonix.amerricard.storage.SharedHelper;
import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageUtils {

//    public static void setupLanguage(Context context) {
//        Log.d("LanguageUtils", "setupLanguage");
//        setLanguage(context, getLanguage(context));
//    }

//    public static void setLanguage(Context context, String lang) {
//        if (TextUtils.isEmpty(lang)) lang = "en";
//        final Resources res = context.getResources();
//        final DisplayMetrics dm = res.getDisplayMetrics();
//        final Configuration conf = res.getConfiguration();
//        conf.locale = new Locale(lang.toLowerCase());
//        res.updateConfiguration(conf, dm);
//        final SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        prefs.edit().putString(SharedHelper.Fields.LANGUAGE, lang.toLowerCase()).apply();
//    }

//    public static String getLanguage(Context context) {
////        final SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
////        String currentLang = prefs.getString(SharedHelper.Fields.LANGUAGE, "");
//        //todo FIX THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        String currentLang = "en";
//
//        if (currentLang.isEmpty()) {
//            currentLang = Locale.getDefault().getLanguage();
//        }
//        Log.d("LanguageUtils", "language:" + currentLang);
//
//        return currentLang;
//    }

    public static String convertLang(Name name, String currLang) {
        switch (currLang) {
            case "en":
                return name.getEn();
            case "fr":
                return name.getFr();
            case "es":
                return name.getEs();
            case "ru":
                return name.getRu();
            case "ua":
                return name.getUa();
            default:
                return "en";
        }
    }


    //todo TRY-CATCH for logic is very bad solution, but server return bad json-structure
    public static String convertLang(JsonElement jsonElementName, String currLang) {

        String output = "";
        try {
            output = jsonElementName.getAsJsonObject().get(currLang).toString();
        } catch (IllegalStateException e) {
            output = jsonElementName.toString();
            e.printStackTrace();
        }

        return output;
    }

    public static int getLanguagePositionInList(String currLang, Context context) {
        final List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));

        return list.indexOf(currLang);
    }

    public static String getLanguageByCode(Context context, String language) {
        List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));
        int index = list.indexOf(language);
        String[] langs = context.getResources().getStringArray(R.array.languages);

        return langs[index];
    }
}

