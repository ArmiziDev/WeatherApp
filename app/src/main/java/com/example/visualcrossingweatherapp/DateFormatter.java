package com.example.visualcrossingweatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static String formatDate(long datetimeEpoch, String format) {
        // seconds to milliseconds
        long epochMillis = datetimeEpoch * 1000L;
        Date date = new Date(epochMillis);

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String formatTime(long datetimeEpoch) {
        // seconds to milliseconds
        long epochMillis = datetimeEpoch * 1000L;
        Date date = new Date(epochMillis);

        // Format the time
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getCurrentFormattedTime(String format) {
        // current time in milliseconds
        long currentEpoch = System.currentTimeMillis();
        Date date = new Date(currentEpoch);

        // formattin gtime
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }
}
