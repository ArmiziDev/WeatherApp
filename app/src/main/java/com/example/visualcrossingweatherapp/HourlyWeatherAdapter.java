package com.example.visualcrossingweatherapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.HourlyWeatherIconBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherViewHolder> {

    private static final String TAG = "HourlyWeatherAdapter";

    private final ArrayList<HourlyWeather> hourlyWeatherList;
    private final MainActivity mainActivity;

    public HourlyWeatherAdapter(MainActivity mainActivity, ArrayList<HourlyWeather> hourlyWeatherList) {
        this.mainActivity = mainActivity;
        this.hourlyWeatherList = hourlyWeatherList;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HourlyWeatherIconBinding binding =
                HourlyWeatherIconBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false
                );

        return new HourlyWeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder holder, int position) {
        HourlyWeather weather = hourlyWeatherList.get(position);

        String formattedTime = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(weather.datetime);
            if (date != null) {
                formattedTime = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.timeText.setText(formattedTime);

        String temperature;
        if (mainActivity.unit_f)
        {
            temperature = String.format("%d°F", (int)weather.temp_f);
        }
        else
        {
            temperature = String.format("%d°C", (int)weather.temp_c);
        }
        holder.tempText.setText(temperature);
        holder.descText.setText(weather.conditions);
        int iconId = mainActivity.getResources().getIdentifier(weather.icon_name, "drawable", mainActivity.getPackageName());
        if (iconId != 0)
        {
            holder.weatherImage.setImageResource(iconId);
        }
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
}
