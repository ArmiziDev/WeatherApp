package com.example.visualcrossingweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private static final String TAG = "LocationHelper";
    private static final int LOCATION_REQUEST = 111;

    public static void getCurrentLocation(Activity activity, LocationCallback callback) {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(activity);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        callback.onLocationResult(getAddress(activity, location));
                    } else {
                        Log.d(TAG, "getCurrentLocation: NULL LOCATION");
                        callback.onFailure("Location was null");
                    }
                })
                .addOnFailureListener(activity, e -> {
                    Log.d(TAG, "getCurrentLocation: FAILURE");
                    callback.onFailure("Failed to access location: " + e.getMessage());
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public static void handlePermissionResult(int requestCode, @NonNull String[] permissions,
                                              @NonNull int[] grantResults, Activity activity, Runnable onPermissionGranted) {
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted.run();
                } else {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static List<Address> getAddress(Context context, Location loc) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses;
            } else {
                sb.append("Cannot determine location.");
            }
        } catch (IOException e) {
            Log.d(TAG, "getPlaceName: " + e.getMessage());
            sb.append("Cannot determine place.");
        }
        return null;
    }

    public interface LocationCallback {
        void onLocationResult(List<Address> locationString);
        void onFailure(String errorMessage);
    }
}