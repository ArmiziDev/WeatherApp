package com.example.visualcrossingweatherapp;

public class Location {

    public double latitude;
    public double longitude;
    public String resolvedAddress;

    Location(String resolvedAddress, double latitude, double longitude)
    {
        this.resolvedAddress = resolvedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
