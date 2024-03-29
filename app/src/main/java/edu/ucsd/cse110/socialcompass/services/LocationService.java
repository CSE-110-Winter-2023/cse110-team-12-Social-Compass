package edu.ucsd.cse110.socialcompass.services;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static edu.ucsd.cse110.socialcompass.Constants.MILES_CONVERSION;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;

public class LocationService implements LocationListener {

    final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static LocationService instance;
    // This needs to be more specific than just Activity for location permissions requesting.
    private final AppCompatActivity activity;

    private MutableLiveData<Pair<Double,Double>> locationValue;
    private final LocationManager locationManager;

    private long lastActiveTime = 0;
    private long inactiveDuration = 0;

    public static LocationService singleton(AppCompatActivity activity){
        if (instance == null){
            instance = new LocationService(activity);
        }
        return instance;
    }

    /**
     * Constructor for LocationService
     * @param activity Context needed to initiate LocationManager
     */
    protected LocationService(AppCompatActivity activity){
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //Register sensor listeners
        withLocationPermissions(this::registerLocationListener);
    }

    public static double metersToMiles(float result) {
        return result * MILES_CONVERSION;
    }

    /**  This will only be called when we for sure have permissions. */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void registerLocationListener() {
        this.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                LocationService.this);
    }

    /** Utility method for doing something with location permissions if we have them, and
     *  after asking for them if we don't already.
     * @param action the thing to do that needs permissions.
     */
    private void withLocationPermissions(Runnable action) {
        if (Arrays.stream(REQUIRED_PERMISSIONS).allMatch(perm -> activity.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED)) {
            // We already have at least one of the location permissions, go ahead!
            action.run();
        } else {
            // We need to ask for permission first.
            // This is the call that requires AppCompatActivity and not just Activity!
            var launcher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), grants -> {
                // At least one of the values in the Map<String, Boolean> grants needs to be true.
                if (grants.values().stream().noneMatch(isGranted -> isGranted)) {
                    // If you've landed here by denying it, you should grant it manually in settings or wipe data.
                    throw new IllegalStateException("App needs you to grant at least one location permission!");
                }
                // We have permission now, carry on!
                action.run();
            });
            launcher.launch(REQUIRED_PERMISSIONS);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.locationValue.postValue(new Pair<Double,Double>(location.getLatitude(),location.getLongitude()));
    }

    private void unregisterLocationListener(){locationManager.removeUpdates(this);}

    public LiveData<Pair<Double,Double>> getLocation(){return this.locationValue;}

    public void setMockOrientationSource(MutableLiveData<Pair<Double,Double>> mockDataSource) {
        unregisterLocationListener();
        this.locationValue = mockDataSource;
    }

    public boolean checkPermissions() {
        // We already have at least one of the location permissions, go ahead!
        return Arrays.stream(REQUIRED_PERMISSIONS).anyMatch(perm
                -> activity.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public long getSavedLastDuration(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return preferences.getLong("inactiveDuration", this.inactiveDuration);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public Location getLastLocation() {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void setLastKnownActiveTime(MainActivity activity) {
        Location location = (getLastLocation() != null) ? getLastLocation() : activity.getLocation();
        this.lastActiveTime = location.getTime();
        SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastActiveTime", this.lastActiveTime);
        editor.apply();
    }

    public long getLastActiveTime(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return preferences.getLong("lastActiveTime", 0);
    }

    public void resetInactiveDuration(Activity activity) {
        this.inactiveDuration = 0;
        setInactiveDuration(this.inactiveDuration, activity);
    }

    public void incrementInactiveDuration(Activity activity) {
        this.inactiveDuration += 1;
        setInactiveDuration(this.inactiveDuration, activity);
    }

    public void setInactiveDuration(long value, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("inactiveDuration", value);
        editor.apply();
    }
}