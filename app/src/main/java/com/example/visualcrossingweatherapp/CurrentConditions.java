package com.example.visualcrossingweatherapp;

public class CurrentConditions
{
    public String datetimeEpoch;
    public String conditions;
    public String icon;

    public int sunriseEpoch;
    public int sunsetEpoch;
    public int uvindex;

    public double temp_f;
    public double temp_c;
    public double feelslike_f;
    public double feelslike_c;
    public double humidity;

    public double windgust;
    public double windspeed;

    public double winddir;
    public double visibility;
    public double cloudcover;

    CurrentConditions(String datetimeEpoch, String conditions, String icon, double temp_f, double feelslike_f,
                      double humidity, double windgust, double windspeed, double winddir, double visibility,
                      double cloudcover, int uvindex, int sunriseEpoch, int sunsetEpoch)
    {
        this.datetimeEpoch = datetimeEpoch;
        this.conditions = conditions;
        this.icon = icon;

        this.temp_f = temp_f;
        this.temp_c = (int)((temp_f - 32) * (5.0/9.0));

        this.feelslike_f = feelslike_f;
        this.feelslike_c = (int)((feelslike_f - 32) * (5.0/9.0));

        this.humidity = humidity;
        this.windgust = windgust;
        this.windspeed = windspeed;
        this.winddir = winddir;
        this.visibility = visibility;
        this.cloudcover = cloudcover;
        this.uvindex = uvindex;

        this.sunriseEpoch = sunriseEpoch;
        this.sunsetEpoch = sunsetEpoch;
    }
}



