package com.example.visualcrossingweatherapp;

import java.io.Serializable;
import java.util.ArrayList;

public class DailyWeather implements Serializable {
    public double temp_f;
    public double temp_c;
    public double feelslike_f;
    public double feelslike_c;
    public double tempmax;
    public double tempmin;
    public double humidity;
    public double visibility;
    public double windgust;
    public double windspeed;

    public int cloudcover;
    public int winddir;
    public int precipprob;
    public int UVIndex;

    public String conditions;
    public String description;
    public String icon;

    public String sunrise;
    public String sunset;

    public ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();

    public DailyWeather(double temp_f, double feelslike_f, double tempmax, double tempmin,
                        double humidity, double visibility, double windgust, double windspeed,
                        int cloudcover, int winddir, int precipprob, int UVIndex, String conditions, String description,
                        String icon, String sunrise, String sunset)
    {
        this.temp_f = temp_f;
        this.temp_c = (int)((temp_f - 32) * (5.0/9.0));
        this.feelslike_f = feelslike_f;
        this.feelslike_c = (int)((feelslike_f - 32) * (5.0/9.0));
        this.tempmax = tempmax;
        this.tempmin = tempmin;
        this.precipprob = precipprob;
        this.UVIndex = UVIndex;
        this.conditions = conditions;
        this.description = description;
        this.icon = icon;

        this.humidity = humidity;
        this.visibility = visibility;
        this.windgust = windgust;
        this.windspeed = windspeed;
        this.cloudcover = cloudcover;
        this.winddir = winddir;

        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public void setHourlyWeatherList(ArrayList<HourlyWeather> hourlyWeatherList)
    {
        this.hourlyWeatherList.addAll(hourlyWeatherList);
    }

}
