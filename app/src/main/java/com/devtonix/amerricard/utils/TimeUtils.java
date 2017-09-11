package com.devtonix.amerricard.utils;


import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    private static final String REGEX_DOT_SYMBOL = "\\.";
    private static final String REGEX_COLON_SYMBOL = ":";

    private static final Calendar oneInstance = Calendar.getInstance();
    private static final Calendar secondInstance = Calendar.getInstance();

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

    public static boolean isSameDay( Date first, Date second){
        oneInstance.setTime(first);
        secondInstance.setTime(second);
        return oneInstance.get(Calendar.MONTH) == secondInstance.get(Calendar.MONTH)
                && oneInstance.get(Calendar.DAY_OF_MONTH) == secondInstance.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isAfter(Date currentDate, Date eventDate) {
        oneInstance.setTime(currentDate);
        secondInstance.setTime(eventDate);
        return oneInstance.get(Calendar.DAY_OF_YEAR) < secondInstance.get(Calendar.DAY_OF_YEAR);
    }
}
