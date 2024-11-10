package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.DailyWeatherBinding;

import java.util.ArrayList;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherViewHolder>
{
    private final ArrayList<DailyWeather> dailyWeatherList;
    private ForecastActivity forecastActivity;

    public DailyWeatherAdapter(ForecastActivity forecastActivity, ArrayList<DailyWeather> dailyWeatherList) {
        this.forecastActivity = forecastActivity;
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

        // Set Background Colors
        ColorMaker.setColorGradient(forecastActivity, holder.main, weather.temp_f);
        ColorMaker.setToolbarColor(forecastActivity, holder.binding.dayDateText, weather.temp_f);

        String temperature = (forecastActivity.unit_f) ?
                String.format("%d°F/%d°F", (int)weather.tempmax_f, (int)weather.tempmin_f) : String.format("%d°C/%d°C", (int)weather.tempmax_c, (int)weather.tempmin_c);
        holder.Temperature.setText(temperature);
        holder.Description.setText(weather.description);
        holder.Precipitation.setText("(" + weather.precipprob + "% precip.)");
        holder.UVIndex.setText("UVIndex: " + weather.UVIndex);

        String morning_temp = (forecastActivity.unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(8).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(8).temp_c);
        String afternoon_temp = (forecastActivity.unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(13).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(13).temp_c);
        String evening_temp = (forecastActivity.unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(17).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(17).temp_c);
        String night_temp = (forecastActivity.unit_f) ? String.format("%d°F", (int)weather.hourlyWeatherList.get(23).temp_f) : String.format("%d°C", (int)weather.hourlyWeatherList.get(23).temp_f);

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
