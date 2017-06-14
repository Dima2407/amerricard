package com.devtonix.amerricard.utils;

import android.content.Context;
import android.util.Log;

import com.devtonix.amerricard.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Oleksii on 11.05.17.
 */
public class LanguageUtils {
    public static void setupLanguage(Context context) {
        Log.d("LanguageUtils", "setupLanguage");
        setLanguage(context, getLanguage());
    }

    public static void setLanguage(Context context, String lang) {
//        if (TextUtils.isEmpty(lang)) lang = "en";
//        Resources res = context.getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        conf.locale = new Locale(lang.toLowerCase());
//        res.updateConfiguration(conf, dm);
//        SharedHelper.getInstance().setLanguage(lang.toLowerCase());
    }

    public static String getLanguage() {
        String lang = "en";
//        if (SharedHelper.getInstance().getLanguage().isEmpty()) {
//            lang = Locale.getDefault().getLanguage();
//        } else {
//            lang = SharedHelper.getInstance().getLanguage();
//        }
//        Log.d("LanguageUtils", "language:" + lang);
        return lang;
    }

    //todo delete this after changing models
    public static String cardNameWrapper(String object) {
        return object;
    }

    public static int getLanguagePositionInList(Context context) {
//        List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));
//        return list.indexOf(SharedHelper.getInstance().getLanguage());
        return -1;
    }

    public static String getLanguageByCode(Context context, String language) {
        List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.language_codes));
        int index = list.indexOf(language);
        String[] langs = context.getResources().getStringArray(R.array.languages);
        return langs[index];
    }
}

