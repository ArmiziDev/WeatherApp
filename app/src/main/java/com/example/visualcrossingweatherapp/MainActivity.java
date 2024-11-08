package com.example.visualcrossingweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    TreeMap<String, Double> timeTempValues = new TreeMap<>();
    private final ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
    private final ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
    private HourlyWeatherAdapter hourlyWeatherAdapter;
    private RecyclerView recyclerView;
    private ChartMaker chartMaker;

    private final Date currentDate = new Date();

    DailyWeather current_weather;

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

        WeatherDownloader.getWeather(this, "Chicago, IL");

    }

    public void updateHourlyWeather(ArrayList<HourlyWeather> hourlyWeather)
    {
        this.hourlyWeatherList.clear();
        Date currentDate = new Date();
        int currentHour = currentDate.getHours();
        for (HourlyWeather weather : hourlyWeather) {
            int weatherHour = Integer.parseInt(weather.datetime.split(":")[0]);
            if (weatherHour >= currentHour) {
                this.hourlyWeatherList.add(weather);
            }
        }

        hourlyWeatherAdapter.notifyDataSetChanged();

        timeTempValues = createTimeTempValues(hourlyWeather);
        chartMaker.makeChart(timeTempValues, System.currentTimeMillis());
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }

    public void updateDailyWeather(ArrayList<DailyWeather> dailyWeather)
    {
        this.dailyWeatherList.clear();
        this.dailyWeatherList.addAll(dailyWeather);

        if(!dailyWeather.isEmpty())
        {
            current_weather = dailyWeather.get(0);

            String temperature = (unit_f) ? String.format("%d°F", (int)current_weather.temp_f) : String.format("%d°C", (int)current_weather.temp_c);
            binding.temperatureText.setText(temperature);
            String feelslike = (unit_f) ? String.format("%d°F", (int)current_weather.feelslike_f) : String.format("%d°C", (int)current_weather.feelslike_c);
            binding.feelsLikeText.setText("Feels Like " + feelslike);
            binding.weatherDescriptionText.setText(
                    current_weather.conditions + " (" + current_weather.cloudcover + "% clouds)");
            binding.humidityText.setText("Humidity: " + current_weather.humidity + "%");
            binding.uvIndexText.setText("UV Index: " + current_weather.UVIndex);
            binding.visibilityText.setText("Visibility: " + current_weather.visibility + " mi");
            String windDirection = getWindDirection(current_weather.winddir);
            binding.windDirectionText.setText(
                    "Winds: " + windDirection + " at " + current_weather.windspeed +
                            " mph gusting to " + current_weather.windgust + " mph"
            );

            String formattedTime = "";
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            try {
                Date date = inputFormat.parse(current_weather.sunrise);
                if (date != null) {
                    formattedTime = outputFormat.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.sunriseText.setText("Sunrise: " + formattedTime);
            try {
                Date date = inputFormat.parse(current_weather.sunset);
                if (date != null) {
                    formattedTime = outputFormat.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.sunsetText.setText("Sunset: " + formattedTime);

            IconMapper.setIcon(binding.weatherIcon, current_weather.icon);
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
            return "Unknown";
        }
    }

    private void EnterLocation(String location)
    {
        Log.d(TAG, "EnterLocation: " + location);
    }

    public void onEnterLocationClick(View v)
    {
        // Single input value dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        //builder.setIcon(R.drawable.icon1);

        // lambda can be used here (as is below)
        builder.setPositiveButton("OK", (dialog, id) -> EnterLocation(String.valueOf(et.getText())));

        // lambda can be used here (as is below)
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
            startActivity(intent);
        }
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

        if (current_weather != null)
        {
            String temperature = (unit_f) ? String.format("%d°F", (int)current_weather.temp_f) : String.format("%d°C", (int)current_weather.temp_c);
            binding.temperatureText.setText(temperature);
            String feelslike = (unit_f) ? String.format("%d°F", (int)current_weather.feelslike_f) : String.format("%d°C", (int)current_weather.feelslike_c);
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
                .setTitle("Connection Error")
                .setMessage("Unable to connect. Please check your internet connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        binding.loadingHourlyWeather.setVisibility(View.INVISIBLE);
    }
}
