package com.example.visualcrossingweatherapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherDownloader
{
    /*
    * API Handler
    * All API Processes are handled through the WeatherDownloader
    * */
    private static final String TAG = "WeatherDownloader";
    private static RequestQueue queue;

    public static void getWeather(MainActivity activity, String location) {
        // Build the full URL
        queue = Volley.newRequestQueue(activity);

        String apiKey = activity.getString(R.string.apikey);
        String BASE_URL = activity.getString(R.string.apilink);
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(location)
                .appendQueryParameter("key", apiKey)
                .build();

        String fullUrl = builtUri.toString();

        Log.d(TAG, "Full URL: " + fullUrl);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Location location = LocationJsonParse(response);
                        ArrayList<DailyWeather> WeatherList = WeatherJsonParse(response);
                        activity.setLocation(location);
                        activity.updateWeather(WeatherList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ArtworkDownloader, Error fetching artwork data " + error);
                        activity.connectionError();
                    }
                }
        );

        queue.add(request);
    }

    private static Location LocationJsonParse(JSONObject response)
    {
        double latititude, longitude;
        String resolvedAddress;
        try {
            latititude = response.getDouble("latitude");
            longitude = response.getDouble("longitude");
            resolvedAddress = response.getString("resolvedAddress");
        } catch (JSONException e) {
            e.printStackTrace();
            latititude = 0;
            longitude = 0;
            resolvedAddress = "No Network Connection,";
        }

        return new Location(resolvedAddress, latititude, longitude);
    }

    private static ArrayList<DailyWeather> WeatherJsonParse(JSONObject response) {
        ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
        ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();

        try {
            // Access the "days" array from the response
            JSONArray daysArray = response.getJSONArray("days");

            // Loop through each day object in the "days" array
            for (int i = 0; i < daysArray.length(); i++) {
                hourlyWeatherList.clear();
                JSONObject dayData = daysArray.getJSONObject(i);

                // Parse required fields from each day object
                int datetimeEpoch = dayData.getInt("datetimeEpoch");
                double temp = dayData.getDouble("temp");
                double feelslike = dayData.getDouble("feelslike");
                double tempmax = dayData.getDouble("tempmax");
                double tempmin = dayData.getDouble("tempmin");
                double humidity = dayData.getDouble("humidity");
                double visibility = dayData.getDouble("visibility");
                double windgust = dayData.getDouble("windgust");
                double windspeed = dayData.getDouble("windspeed");
                int cloudcover = dayData.getInt("cloudcover");
                int winddir = dayData.getInt("winddir");
                int precipprob = dayData.getInt("precipprob");
                int uvIndex = dayData.getInt("uvindex");
                String conditions = dayData.getString("conditions");
                String description = dayData.getString("description");
                String icon = dayData.getString("icon");
                String sunrise = dayData.getString("sunrise");
                String sunset = dayData.getString("sunset");

                // Create a DailyWeather object
                DailyWeather dailyWeather = new DailyWeather(
                        datetimeEpoch, temp, feelslike, tempmax, tempmin, humidity, visibility,
                        windgust, windspeed, cloudcover, winddir, precipprob, uvIndex,
                        conditions, description, icon, sunrise, sunset
                );

                JSONArray hoursArray = dayData.getJSONArray("hours");

                // Loop through the hourly data
                for (int j = 0; j < hoursArray.length(); j++) {
                    JSONObject hourData = hoursArray.getJSONObject(j);

                    // Parse the relevant fields
                    String dailydatetime = hourData.getString("datetime");
                    int dailydatetimeEpoch = hourData.getInt("datetimeEpoch");
                    int dailytemp = (int)hourData.getDouble("temp");
                    int dailyfeelslike = (int)hourData.getDouble("feelslike");
                    String dailyconditions = hourData.getString("conditions");
                    String dailyicon = hourData.getString("icon");

                    // Create an HourlyWeather object and add it to the list
                    HourlyWeather hourlyWeather = new HourlyWeather(dailydatetime, dailydatetimeEpoch, dailytemp, dailyfeelslike, dailyconditions, dailyicon);
                    hourlyWeatherList.add(hourlyWeather);
                }
                // give the daily weather its hourly weather list
                dailyWeather.setHourlyWeatherList(hourlyWeatherList);
                // daily weather
                dailyWeatherList.add(dailyWeather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dailyWeatherList;
    }



}
