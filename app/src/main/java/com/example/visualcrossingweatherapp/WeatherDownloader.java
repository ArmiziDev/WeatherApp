package com.example.visualcrossingweatherapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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

    public static void getWeather(MainActivity mainActivity, String location) {
        // Build the full URL
        queue = Volley.newRequestQueue(mainActivity);

        String apiKey = mainActivity.getString(R.string.apikey);
        String BASE_URL = mainActivity.getString(R.string.apilink);
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
                        WeatherLocation weatherLocation = LocationJsonParse(response);
                        CurrentConditions currentConditions = CurrentConditionsJsonParse(response);
                        Alerts alerts = AlertsJsonParse(response);
                        ArrayList<DailyWeather> WeatherList = WeatherJsonParse(response);
                        mainActivity.setLocation(weatherLocation);
                        mainActivity.setCurrentConditions(currentConditions);
                        mainActivity.setAlerts(alerts);
                        if (WeatherList == null || currentConditions == null || weatherLocation == null)
                        {
                            mainActivity.dataError();
                        } else
                        {
                            mainActivity.setWeatherList(WeatherList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: WeatherDownloader, Error fetching artwork data " + error);

                        if (error instanceof NoConnectionError || error instanceof TimeoutError)
                        {
                            mainActivity.connectionError();
                        }
                        else if (error.networkResponse != null)
                        {
                            int statusCode = error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);

                            if (statusCode == 400 && responseBody.contains("Bad API Request:Invalid location parameter value")) {
                                Log.d(TAG, "onErrorResponse: Bad location parameter value");
                                mainActivity.locationError(location);
                            } else if (statusCode >= 400 && statusCode < 500) {
                                Log.d(TAG, "onErrorResponse: Client error, status code: " + statusCode);
                                mainActivity.dataError();
                            } else if (statusCode >= 500) {
                                Log.d(TAG, "onErrorResponse: Server error, status code: " + statusCode);
                                mainActivity.dataError();
                            }
                        }
                    }
                }
        );

        queue.add(request);
    }

    private static Alerts AlertsJsonParse(JSONObject response)
    {
        String event, headline, id, description;
        try
        {
            JSONObject alerts = response.getJSONObject("alerts");

            event = alerts.getString("event");
            headline = alerts.getString("headline");
            id = alerts.getString("id");
            description = alerts.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
            event = "";
            headline = "";
            id = "";
            description = "";
        }

        return new Alerts(event, headline, id, description);
    }

    private static CurrentConditions CurrentConditionsJsonParse(JSONObject response)
    {
        String datetimeEpoch, conditions, icon;
        int sunriseEpoch, sunsetEpoch, uvindex;
        double temp, feelslike, humidity, windgust, windspeed, winddir, visibility, cloudcover;

        try {
            JSONObject currentConditions = response.getJSONObject("currentConditions");

            datetimeEpoch = currentConditions.getString("datetimeEpoch");
            conditions = currentConditions.getString("conditions");
            icon = currentConditions.getString("icon");

            temp = currentConditions.getDouble("temp");
            feelslike = currentConditions.getDouble("feelslike");
            humidity = currentConditions.getDouble("humidity");
            windgust = currentConditions.getDouble("windgust");
            windspeed = currentConditions.getDouble("windspeed");
            winddir = currentConditions.getDouble("winddir");
            visibility = currentConditions.getDouble("visibility");
            cloudcover = currentConditions.getDouble("cloudcover");
            uvindex = currentConditions.getInt("uvindex");

            sunriseEpoch = currentConditions.getInt("sunriseEpoch");
            sunsetEpoch = currentConditions.getInt("sunsetEpoch");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return new CurrentConditions(datetimeEpoch, conditions, icon, temp, feelslike, humidity,
                windgust, windspeed, winddir, visibility, cloudcover, uvindex, sunriseEpoch, sunsetEpoch);
    }

    private static WeatherLocation LocationJsonParse(JSONObject response)
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

        return new WeatherLocation(resolvedAddress, latititude, longitude);
    }

    private static ArrayList<DailyWeather> WeatherJsonParse(JSONObject response) {
        ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
        ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();

        try {
            // access days array
            JSONArray daysArray = response.getJSONArray("days");

            // loop through all days
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

                DailyWeather dailyWeather = new DailyWeather(
                        datetimeEpoch, temp, feelslike, tempmax, tempmin, humidity, visibility,
                        windgust, windspeed, cloudcover, winddir, precipprob, uvIndex,
                        conditions, description, icon, sunrise, sunset
                );

                JSONArray hoursArray = dayData.getJSONArray("hours");

                for (int j = 0; j < hoursArray.length(); j++) {
                    JSONObject hourData = hoursArray.getJSONObject(j);

                    String dailydatetime = hourData.getString("datetime");
                    int dailydatetimeEpoch = hourData.getInt("datetimeEpoch");
                    int dailytemp = (int)hourData.getDouble("temp");
                    int dailyfeelslike = (int)hourData.getDouble("feelslike");
                    String dailyconditions = hourData.getString("conditions");
                    String dailyicon = hourData.getString("icon");

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
