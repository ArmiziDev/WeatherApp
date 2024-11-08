package com.example.visualcrossingweatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.HourlyWeatherIconBinding;

public class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {
    public TextView dateText;
    public TextView timeText;
    public TextView tempText;
    public TextView descText;
    public ImageView weatherImage;

    HourlyWeatherIconBinding binding;

    public HourlyWeatherViewHolder(HourlyWeatherIconBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

        dateText = binding.hourlyDateText;
        timeText = binding.hourlyTimeText;
        tempText = binding.hourlyTempText;
        descText = binding.hourlyDescriptionText;
        weatherImage = binding.hourlyImage;
    }
}
