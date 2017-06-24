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
    @Deprecated
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

