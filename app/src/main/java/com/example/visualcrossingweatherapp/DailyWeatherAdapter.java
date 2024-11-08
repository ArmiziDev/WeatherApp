package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.DailyWeatherBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherViewHolder>
{
    private final ArrayList<DailyWeather> dailyWeatherList;
    private boolean unit_f;

    public DailyWeatherAdapter(boolean unit_f, ArrayList<DailyWeather> dailyWeatherList) {
        this.unit_f = unit_f;
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public DailyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyWeatherBinding binding =
                DailyWeatherBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false
                );

        return new DailyWeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherViewHolder holder, int position) {
        DailyWeather weather = dailyWeatherList.get(position);

        String temperature = (unit_f) ? String.format("%d°F", (int)weather.temp_f) : String.format("%d°C", (int)weather.temp_c);
        holder.Temperature.setText(temperature);
        holder.Description.setText(weather.description);
        holder.Precipitation.setText("(" + weather.precipprob + "% precip.)");
        holder.UVIndex.setText("UVIndex: " + weather.UVIndex);
        //holder.morningTemperature.setText();

        IconMapper.setIcon(holder.image, weather.icon);
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }
}
