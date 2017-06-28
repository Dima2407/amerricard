package com.devtonix.amerricard.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDateUtils {

    private static final String REGEX_PATTERN_FIRST = "^((19|20)\\d{2})-([1-9]|0[1-9]|1[0-2])-(0[1-9]|[1-9]|[12][0-9]|3[01])$";//"1999-12-31"
    private static final String REGEX_PATTERN_SECOND = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(([0-9])|([0-2][0-9])|([3][0-1])),\\s\\d{4}$";// Jun 21, 1999
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_ONE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_TWO = new SimpleDateFormat("MMM dd, yyyy");

    public static final SimpleDateFormat GODLIKE_APPLICATION_DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy");
    public static final String GODLIKE_DATE_FORMAT = "MM.dd.yyyy";

    public static long verifyDateFormat(String date) {
        final Matcher firstMatcher = Pattern.compile(REGEX_PATTERN_FIRST).matcher(date);
        final Matcher secondMatcher = Pattern.compile(REGEX_PATTERN_SECOND).matcher(date);

        try {
            if (firstMatcher.matches()) {
                return SIMPLE_DATE_FORMAT_ONE.parse(date).getTime();
            } else if (secondMatcher.matches()) {
                return SIMPLE_DATE_FORMAT_TWO.parse(date).getTime();
            } else {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return -2;
        }
    }
}
