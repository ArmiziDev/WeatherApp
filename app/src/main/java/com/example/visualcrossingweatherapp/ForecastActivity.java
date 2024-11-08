package com.example.visualcrossingweatherapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.ActivityDailyForecastBinding;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity
{
    private static final String TAG = "ForecastActivity";

    private ActivityDailyForecastBinding binding;

    private ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
    private DailyWeatherAdapter dailyWeatherAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDailyForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "onCreate: Registering OnBackInvokedCallback");
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    this::backInvoked
            );
        } else {
            getOnBackPressedDispatcher().addCallback(
                    new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            backInvoked();
                        }
                    }
            );
        }

        dailyWeatherList = (ArrayList<DailyWeather>) getIntent().getSerializableExtra("DailyWeatherList");
        boolean unit_f = getIntent().getBooleanExtra("unit_f", true);

        if (!dailyWeatherList.isEmpty())
        {
            recyclerView = binding.dailyWeatherRecycler;
            dailyWeatherAdapter = new DailyWeatherAdapter(unit_f, dailyWeatherList);
            recyclerView.setAdapter(dailyWeatherAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            fillDailyWeatherRecycler();
        }
    }

    private void fillDailyWeatherRecycler()
    {
        dailyWeatherAdapter.notifyDataSetChanged();
    }

    private void backInvoked()
    {
        finish();
    }
}
