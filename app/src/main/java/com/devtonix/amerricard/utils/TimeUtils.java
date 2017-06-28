package com.devtonix.amerricard.utils;

import android.text.format.DateFormat;

import java.util.Calendar;

public class TimeUtils {

    private static final String REGEX_DOT_SYMBOL = "\\.";
    private static final String REGEX_COLON_SYMBOL = ":";

    public static int getHour(String time) {
        String[] s = time.split(REGEX_COLON_SYMBOL);
        return Integer.parseInt(s[0]);
    }

    public static int getMinute(String time) {
        String[] s = time.split(REGEX_COLON_SYMBOL);
        return Integer.parseInt(s[1]);
    }

    public static int getDayOfMonth(String date) {
        String[] s = date.split(REGEX_DOT_SYMBOL);
        return Integer.parseInt(s[1]);
    }

    public static int getMonth(String date) {
        String[] s = date.split(REGEX_DOT_SYMBOL);
        return Integer.parseInt(s[0]);
    }

    public static int getYear(String date) {
        String[] s = date.split(REGEX_DOT_SYMBOL);
        return Integer.parseInt(s[2]);
    }

    public static String calDateToString(Calendar calendar) {
        return DateFormat.format(RegexDateUtils.GODLIKE_DATE_FORMAT, calendar).toString();
    }
}
