package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.HourlyWeatherIconBinding;

import java.util.ArrayList;


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

        holder.dateText.setText(weather.timestamp);

        String formattedTime = DateFormatter.formatTime(weather.datetimeEpoch, "h:mm a");
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

        IconMapper.setIcon(holder.weatherImage, weather.icon_name);
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
}
