package com.example.visualcrossingweatherapp;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.location.Location;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    TreeMap<String, Double> timeTempValues = new TreeMap<>();
    private final ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
    private final ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
    private HourlyWeatherAdapter hourlyWeatherAdapter;
    private RecyclerView recyclerView;
    private ChartMaker chartMaker;

    private WeatherLocation weatherLocation;
    private Alerts alerts;
    private CurrentConditions currentConditions;

    public boolean unit_f = true;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize the binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Use binding to reference views instead of findViewById
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = binding.hourlyWeatherRecycler;
        hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hourlyWeatherList);
        recyclerView.setAdapter(hourlyWeatherAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        chartMaker = new ChartMaker(this, binding);

        // Request the current location
        LocationHelper.getCurrentLocation(this, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(List<Address> addresses) {
                // use addresses to find location
                String cityName = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                Log.d(TAG, "onLocationResult: " + cityName);
                EnterLocation(cityName);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d(TAG, "onFailure: " + errorMessage);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(this::refreshWeather);
    }

    public void refreshWeather()
    {
        if (weatherLocation != null)
        {
            Log.d(TAG, "refreshWeather: Refreshing Weather for " + weatherLocation.resolvedAddress);
            WeatherDownloader.getWeather(this, weatherLocation.resolvedAddress);
        }
    }

    public void setLocation(WeatherLocation weatherLocation)
    {
        this.weatherLocation = weatherLocation;
    }

    public void setCurrentConditions(CurrentConditions currentConditions)
    {
        this.currentConditions = currentConditions;
    }

    public void setAlerts(Alerts alerts)
    {
        this.alerts = alerts;
    }

    public void setWeatherList(ArrayList<DailyWeather> weatherList)
    {
        binding.swipeRefresh.setRefreshing(false); // This stops the busy-circle
        if (!weatherList.isEmpty())
        {
            updateHourlyWeather(weatherList.get(0).hourlyWeatherList);
            updateDailyWeather(weatherList);
        }
    }

    public void updateHourlyWeather(ArrayList<HourlyWeather> hourlyWeather)
    {
        this.hourlyWeatherList.clear();
        long currentEpoch = System.currentTimeMillis() / 1000;
        for (HourlyWeather weather : hourlyWeather) {
            if (currentEpoch < weather.datetimeEpoch)
            {
                this.hourlyWeatherList.add(weather);
            }
        }
        hourlyWeatherAdapter.notifyDataSetChanged();

        timeTempValues = createTimeTempValues(hourlyWeather);
        chartMaker.makeChart(timeTempValues, System.currentTimeMillis());
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }

    public void resetTemperature()
    {
        String temperature = (unit_f) ? String.format("%d°F", (int)currentConditions.temp_f) : String.format("%d°C", (int)currentConditions.temp_c);
        binding.temperatureText.setText(temperature);
    }

    public void updateDailyWeather(ArrayList<DailyWeather> dailyWeather)
    {
        this.dailyWeatherList.clear();
        this.dailyWeatherList.addAll(dailyWeather);

        if(!dailyWeather.isEmpty())
        {
            // Set Colors
            ColorMaker.setColorGradient(this, binding.main, currentConditions.temp_f);
            ColorMaker.setToolbarColor(this, binding.iconBar, currentConditions.temp_f);
            ColorMaker.setRecyclerColor(this, binding.hourlyWeatherRecycler, currentConditions.temp_f);

            String temperature = (unit_f) ? String.format("%d°F", (int)currentConditions.temp_f) : String.format("%d°C", (int)currentConditions.temp_c);
            binding.temperatureText.setText(temperature);
            String feelslike = (unit_f) ? String.format("%d°F", (int)currentConditions.feelslike_f) : String.format("%d°C", (int)currentConditions.feelslike_c);
            binding.feelsLikeText.setText("Feels Like " + feelslike);
            binding.weatherDescriptionText.setText(
                    currentConditions.conditions + " (" + currentConditions.cloudcover + "% clouds)");
            binding.humidityText.setText("Humidity: " + currentConditions.humidity + "%");
            binding.uvIndexText.setText("UV Index: " + currentConditions.uvindex);
            binding.visibilityText.setText("Visibility: " + currentConditions.visibility + " mi");
            String windDirection = getWindDirection((int)currentConditions.winddir);
            binding.windDirectionText.setText(
                    "Winds: " + windDirection + " at " + currentConditions.windspeed +
                            " mph gusting to " + currentConditions.windgust + " mph"
            );

            binding.sunriseText.setText("Sunrise: " + DateFormatter.formatTime(currentConditions.sunriseEpoch, "h:mm a"));
            binding.sunsetText.setText("Sunset: " + DateFormatter.formatTime(currentConditions.sunsetEpoch, "h:mm a"));

            IconMapper.setIcon(binding.weatherIcon, currentConditions.icon);

            String city = weatherLocation.resolvedAddress.split(",")[0].trim();
            String resolvedAddress = city + ", " + DateFormatter.getCurrentFormattedTime("EEE MMM dd h:mm a");
            binding.resolvedAddress.setText(resolvedAddress);
        }
    }

    private String getWindDirection(int windDir)
    {
        if (windDir >= 337.5 || windDir < 22.5) {
            return "N";
        } else if (windDir >= 22.5 && windDir < 67.5) {
            return "NE";
        } else if (windDir >= 67.5 && windDir < 112.5) {
            return "E";
        } else if (windDir >= 112.5 && windDir < 157.5) {
            return "SE";
        } else if (windDir >= 157.5 && windDir < 202.5) {
            return  "S";
        } else if (windDir >= 202.5 && windDir < 247.5) {
            return "SW";
        } else if (windDir >= 247.5 && windDir < 292.5) {
            return  "W";
        } else if (windDir >= 292.5 && windDir < 337.5) {
            return  "NW";
        } else {
            return "X";
        }
    }

    private void EnterLocation(String location)
    {
        Log.d(TAG, "EnterLocation: " + location);

        WeatherDownloader.getWeather(this, location);
    }

    public void onEnterLocationClick(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", (dialog, id) -> EnterLocation(String.valueOf(et.getText())));

        builder.setNegativeButton("Cancel", (dialog, id) -> {});

        builder.setMessage(
                "For US locations, enter as 'City', or 'City, State'" +
                "\n\n" +
                "For internation locations enter as 'City, Country'");
        builder.setTitle("Enter Location");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onForecastClick(View v)
    {
        if(!dailyWeatherList.isEmpty())
        {
            Intent intent = new Intent(this, ForecastActivity.class);
            intent.putExtra("DailyWeatherList", dailyWeatherList);
            intent.putExtra("unit_f", unit_f);

            String city = weatherLocation.resolvedAddress.split(",")[0].trim();
            intent.putExtra("City", city);
            startActivity(intent);
        }
    }

    public void onMapClick(View v)
    {
        if (currentConditions == null) return;
        String location = String.valueOf(weatherLocation.latitude + ", " + weatherLocation.longitude);

        Uri mapUri = Uri.parse("geo:" + location + "?q=" + weatherLocation.resolvedAddress);

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        // Check if there is an app that can handle geo intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }

    public void doShare(View v) {
        if (currentConditions == null) return;

        String main = "Weather for " + weatherLocation.resolvedAddress + "\n\n";

        String forecast;
        String now;
        String winds;
        String visibility;
        if (unit_f)
        {
            forecast = "Forecast: " + currentConditions.conditions + " with a high of " +
                    dailyWeatherList.get(0).tempmax_f + "°F and a low of " + dailyWeatherList.get(0).tempmin_f + "°F.\n\n";
            now = currentConditions.temp_f + "°F, " + currentConditions.conditions + " (Feels like: " + currentConditions.feelslike_f + "°F)\n\n";
            winds = "Winds: " + getWindDirection((int)currentConditions.winddir) + " at " + currentConditions.windspeed + " mph\n";
            visibility = "Visibility: " + currentConditions.visibility + " mi \n";
        } else {
            forecast = "Forecast: " + currentConditions.conditions + " with a high of " +
                    dailyWeatherList.get(0).tempmax_c + "°C and a low of " + dailyWeatherList.get(0).tempmin_c + "°C.\n\n";
            now = currentConditions.temp_c + "°C, " + currentConditions.conditions + " (Feels like: " + currentConditions.feelslike_c + "°C)\n\n";
            winds = "Winds: " + getWindDirection((int)currentConditions.winddir) + " at " + (currentConditions.windspeed * 1.609) + " kph\n";
            visibility = "Visibility: " + currentConditions.visibility * 1.609 + " km \n";
        }
        String humidity = "Humdity: " + currentConditions.humidity + "%\n";
        String uvindex = "UV Index: " + currentConditions.uvindex + "\n";
        String sunrise = "Sunrise: " + DateFormatter.formatDate(currentConditions.sunriseEpoch, "h:mm a");
        String sunset = "Sunset: " + DateFormatter.formatDate(currentConditions.sunsetEpoch, "h:mm a");

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, main);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                main +
                forecast +
                now +
                humidity +
                winds +
                uvindex +
                sunrise +
                sunset +
                visibility
                );
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share to...");
        startActivity(shareIntent);

    }


    public void changeUnitClick(View v)
    {
        unit_f = !unit_f;
        if (unit_f) {
            binding.unitIcon.setImageResource(R.drawable.units_f);
            hourlyWeatherAdapter.notifyDataSetChanged();
        }
        else {
            binding.unitIcon.setImageResource(R.drawable.units_c);
            hourlyWeatherAdapter.notifyDataSetChanged();
        }

        if (currentConditions != null)
        {
            String temperature = (unit_f) ? String.format("%d°F", (int)currentConditions.temp_f) : String.format("%d°C", (int)currentConditions.temp_c);
            binding.temperatureText.setText(temperature);
            String feelslike = (unit_f) ? String.format("%d°F", (int)currentConditions.feelslike_f) : String.format("%d°C", (int)currentConditions.feelslike_c);
            binding.feelsLikeText.setText("Feels Like " + feelslike);
        }
    }

    private TreeMap<String, Double> createTimeTempValues(ArrayList<HourlyWeather> hourlyWeather) {
        TreeMap<String, Double> timeTempValues = new TreeMap<>();
        for (int i = 0; i < hourlyWeather.size(); i++)
        {
            HourlyWeather current_weather = hourlyWeather.get(i);
            timeTempValues.put(current_weather.datetime, current_weather.temp_f);
        }
        return timeTempValues;
    }

    public void connectionError() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("This app requires an internet connection to function properly. Please check your connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }

    public void locationError(String location) {
        new AlertDialog.Builder(this)
                .setTitle("Location Error")
                .setMessage("The specified location '" + location + "' could not be resolved. Please try a different location")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }

    public void dataError() {
        new AlertDialog.Builder(this)
                .setTitle("Weather Data Error")
                .setMessage("There was an error retrieving the weather data. Please try again later.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }

    public void resetLocation(View v)
    {
        LocationHelper.getCurrentLocation(this, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(List<Address> addresses) {
                String cityName = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                Log.d(TAG, "onLocationResult: " + cityName);
                EnterLocation(cityName);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d(TAG, "onFailure: " + errorMessage);
            }
        });
    }

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle("App-resolving error");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationHelper.handlePermissionResult(requestCode, permissions, grantResults, this, () -> {
            // Permission granted, request the location again
            LocationHelper.getCurrentLocation(this, new LocationHelper.LocationCallback() {
                @Override
                public void onLocationResult(List<Address> addresses) {
                    String cityName = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                    Log.d(TAG, "onLocationResult: " + cityName);
                    EnterLocation(cityName);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d(TAG, "onFailure: " + errorMessage);
                }
            });
        });
    }
}
