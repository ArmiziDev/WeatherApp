package com.example.visualcrossingweatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.DailyWeatherBinding;


public class DailyWeatherViewHolder extends RecyclerView.ViewHolder
{
    TextView Date;
    TextView Temperature;
    TextView Description;
    TextView Precipitation;
    TextView UVIndex;
    TextView morningTemperature;
    TextView afternoonTemperature;
    TextView eveningTemperature;
    TextView nightTemperature;
    ImageView image;

    DailyWeatherBinding binding;

    public DailyWeatherViewHolder(DailyWeatherBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

        Date = binding.dayDateText;
        Temperature = binding.dailyTemperatureText;
        Description = binding.dailyDescriptionText;
        Precipitation = binding.dailyPrecipitaitonText;
        UVIndex = binding.dailyUVIndexText;
        morningTemperature = binding.morningTempNumberText;
        afternoonTemperature = binding.afternoonTempNumberText;
        eveningTemperature = binding.eveningTempNumberText;
        nightTemperature = binding.nightTempNumberText;
        image = binding.imageView;
    }
}
