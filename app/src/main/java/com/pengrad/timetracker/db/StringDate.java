package com.pengrad.timetracker.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * stas
 * 7/31/15
 */
public class StringDate {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static String fromDate(Date date) {
        return FORMATTER.format(date);
    }

    public static String fromMillis(long millis) {
        return FORMATTER.format(new Date(millis));
    }

}
