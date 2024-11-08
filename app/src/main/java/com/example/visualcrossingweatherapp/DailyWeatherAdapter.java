package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.DailyWeatherBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        String morning_temp = (unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(8).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(8).temp_c);
        String afternoon_temp = (unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(13).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(13).temp_c);
        String evening_temp = (unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(17).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(17).temp_c);
        String night_temp = (unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(23).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(23).temp_f);

        holder.morningTemperature.setText(morning_temp);
        holder.afternoonTemperature.setText(afternoon_temp);
        holder.eveningTemperature.setText(evening_temp);
        holder.nightTemperature.setText(night_temp);

        String formattedDate = DateFormatter.formatDate(weather.datetimeEpoch, " EEEE, MM/dd");
        holder.Date.setText(formattedDate);

        IconMapper.setIcon(holder.image, weather.icon);
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }
}
