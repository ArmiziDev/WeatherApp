package com.example.visualcrossingweatherapp;

public class WeatherLocation {

    public double latitude;
    public double longitude;
    public String resolvedAddress;

    WeatherLocation(String resolvedAddress, double latitude, double longitude)
    {
        this.resolvedAddress = resolvedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
