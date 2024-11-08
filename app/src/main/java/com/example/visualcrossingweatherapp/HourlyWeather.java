package com.example.visualcrossingweatherapp;


import android.widget.ImageView;

import java.io.Serializable;

public class HourlyWeather implements Serializable
{
    public String datetime;
    public int datetimeEpoch;
    public double temp_f;
    public double temp_c;
    public double feelslike;
    public String conditions;
    public String icon_name;

    // Constructor
    public HourlyWeather(String datetime, int datetimeEpoch,int temp, int feelslike, String conditions, String icon_name) {
        this.datetime = datetime;
        this.datetimeEpoch = datetimeEpoch;
        this.temp_f = temp;
        this.feelslike = feelslike;
        this.conditions = conditions;
        this.icon_name = icon_name;

        this.temp_c = (int)((temp_f - 32) * (5.0/9.0)); // conversion to celicus
    }
}