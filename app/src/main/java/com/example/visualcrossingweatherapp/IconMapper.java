package com.example.visualcrossingweatherapp;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class IconMapper {
    private static final HashMap<String, Integer> iconMap = new HashMap<>();

    static {
        iconMap.put("clear-day", R.drawable.clear_day);
        iconMap.put("clear-night", R.drawable.clear_night);
        iconMap.put("cloudy", R.drawable.cloudy);
        iconMap.put("fog", R.drawable.fog);
        iconMap.put("hail", R.drawable.hail);
        iconMap.put("partly-cloudy-day", R.drawable.partly_cloudy_day);
        iconMap.put("partly-cloudy-night", R.drawable.partly_cloudy_night);
        iconMap.put("rain", R.drawable.rain);
        iconMap.put("rain-snow", R.drawable.rain_snow);
        iconMap.put("rain-snow-showers-day", R.drawable.rain_snow_showers_day);
        iconMap.put("rain-snow-showers-night", R.drawable.rain_snow_showers_night);
        iconMap.put("showers-day", R.drawable.showers_day);
        iconMap.put("showers-night", R.drawable.showers_night);
        iconMap.put("sleet", R.drawable.sleet);
        iconMap.put("snow", R.drawable.snow);
        iconMap.put("snow-showers-day", R.drawable.snow_showers_day);
        iconMap.put("snow-showers-night", R.drawable.snow_showers_night);
        iconMap.put("thunder", R.drawable.thunder);
        iconMap.put("thunder-rain", R.drawable.thunder_rain);
        iconMap.put("thunder-showers-day", R.drawable.thunder_showers_day);
        iconMap.put("thunder-showers-night", R.drawable.thunder_showers_night);
        iconMap.put("wind", R.drawable.wind);
    }

    public static int getIconId(String iconName)
    {
        Integer iconId = iconMap.get(iconName);
        return (iconId != null) ? iconId : R.drawable.noimage;
    }

    public static void setIcon(ImageView imageView, String iconName)
    {
        int iconId = getIconId(iconName);

        Picasso.get()
                .load(iconId)
                .into(imageView);
    }
}
