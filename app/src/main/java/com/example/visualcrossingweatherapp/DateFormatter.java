package com.example.visualcrossingweatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static String formatDate(long datetimeEpoch, String format) {
        // seconds to milliseconds
        long epochMillis = datetimeEpoch * 1000L;
        Date date = new Date(epochMillis);

        // format
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String formatTime(long datetimeEpoch, String format) {
        // seconds to milliseconds
        long epochMillis = datetimeEpoch * 1000L;
        Date date = new Date(epochMillis);

        // format
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getCurrentFormattedTime(String format) {
        // current time in milliseconds
        long currentEpoch = System.currentTimeMillis();
        Date date = new Date(currentEpoch);

        // format
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }
}
