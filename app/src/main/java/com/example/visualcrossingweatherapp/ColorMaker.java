package com.example.visualcrossingweatherapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Locale;

public class ColorMaker {

    public static void setColorGradient(Context context, View view, double tempIn) {
        double temp = tempIn;
        int[] colors = getTemperatureColor((int) temp);
        String startColorString = String.format(Locale.getDefault(), "#FF%02x%02x%02x", colors[0], colors[1], colors[2]);
        int startColor = Color.parseColor(startColorString);
        String endColorString = String.format(Locale.getDefault(), "#99%02x%02x%02x", colors[0], colors[1], colors[2]);
        int endColor = Color.parseColor(endColorString);
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, endColor});
        view.setBackground(gd);
    }
    private static int[] getTemperatureColor(int temperature) {
        int[] rgb = new int[3];
        if (temperature < 40) {
            rgb[0] = 0;
            rgb[1] = 0;
            rgb[2] = 40 + temperature * 3;
        } else if (temperature <= 81) {
            rgb[1] = (int) (temperature * 1.5);
            rgb[0] = rgb[1] / 2;
            rgb[2] = rgb[0] / 2;
        } else {
            rgb[0] = 40 + temperature;
            rgb[1] = 0;
            rgb[2] = 0;
        }
        return rgb;
    }

    public static void setBackgroundColor(Context context, View view, double temperature) {
        GradientDrawable gradientDrawable = new GradientDrawable();

        if (temperature > 80) {
            // Red gradient
            int startColor = ContextCompat.getColor(context, R.color.red_start_bg);
            int endColor = ContextCompat.getColor(context, R.color.red_end_bg);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else if (temperature > 40 && temperature <= 80) {
            // Green gradient
            int startColor = ContextCompat.getColor(context, R.color.green_start_bg);
            int endColor = ContextCompat.getColor(context, R.color.green_end_bg);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else {
            // Blue gradient
            int startColor = ContextCompat.getColor(context, R.color.blue_start_bg);
            int endColor = ContextCompat.getColor(context, R.color.blue_end_bg);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        }

        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        view.setBackground(gradientDrawable);
    }

    public static void setToolbarColor(Context context, View view, double temperature) {
        GradientDrawable gradientDrawable = new GradientDrawable();

        // Determine the color gradient based on the temperature
        if (temperature > 80) {
            // Red gradient
            int startColor = ContextCompat.getColor(context, R.color.red_start_toolbar);
            int endColor = ContextCompat.getColor(context, R.color.red_end_toolbar);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else if (temperature > 40 && temperature <= 80) {
            // Green gradient
            int startColor = ContextCompat.getColor(context, R.color.green_start_toolbar);
            int endColor = ContextCompat.getColor(context, R.color.green_end_toolbar);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else {
            // Blue gradient
            int startColor = ContextCompat.getColor(context, R.color.blue_start_toolbar);
            int endColor = ContextCompat.getColor(context, R.color.blue_end_toolbar);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        }

        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        view.setBackground(gradientDrawable);
    }

    public static void setRecyclerColor(Context context, View view, double temperature) {
        GradientDrawable gradientDrawable = new GradientDrawable();

        if (temperature > 80) {
            // Red gradient
            int startColor = ContextCompat.getColor(context, R.color.red_start_recycler);
            int endColor = ContextCompat.getColor(context, R.color.red_end_recycler);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else if (temperature > 40 && temperature <= 80) {
            // Green gradient
            int startColor = ContextCompat.getColor(context, R.color.green_start_recycler);
            int endColor = ContextCompat.getColor(context, R.color.green_end_recycler);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        } else {
            // Blue gradient
            int startColor = ContextCompat.getColor(context, R.color.blue_start_recycler);
            int endColor = ContextCompat.getColor(context, R.color.blue_end_recycler);
            gradientDrawable.setColors(new int[]{startColor, endColor});
        }

        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        view.setBackground(gradientDrawable);
    }
}
