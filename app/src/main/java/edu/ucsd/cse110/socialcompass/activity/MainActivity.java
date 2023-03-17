package edu.ucsd.cse110.socialcompass.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110.socialcompass.Bearing;
import edu.ucsd.cse110.socialcompass.FriendIcon;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.services.ZoomingService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private LocationService locationService;
    private String UID; // The user's unique UID
    private LiveData<Friend> user;
    private MainActivityViewModel mainViewModel;
    private FriendListViewModel friendListViewModel;
    private double UserLatitude, UserLongitude;
    private Friend self;    // adding any new user to list of friends
    private int range = 1000;
    private HashMap<String, FriendIcon> friendIcons;
    private Handler handler;
    private long lastActiveDuration;
    private int scaleOfCircles = 100;
    Location location;

    // Sensor stuff
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean accelerometerSet = false;
    private boolean magnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float currentAzimuth = 0;

    @Override
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing for Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Check if user is new
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean newUser = preferences.getBoolean("newUser", true);
        if (newUser) {
            initNewUser();
        }
        UID = preferences.getString("myUID", "Default UID");

        // Setup ViewModel and Adapter
        mainViewModel = setupMainViewModel();
        friendListViewModel = setupFriendListViewModel();
        var adapter = setupAdapter(mainViewModel);

        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();

        // Setup location service
        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        friendIcons = new HashMap<>();
        lastActiveDuration = locationService.getSavedLastDuration(this);
        handler = new Handler();
        handler.postDelayed(myRunnable, 100);

        // Start polling friends
        startPollingFriends();
        displayFriends(mainViewModel, range, Double.POSITIVE_INFINITY, 480, true);

        // Find views for zooming
        var firstCircle = (TextView)findViewById(R.id.first_circle);
        var secondCircle = (TextView)findViewById(R.id.second_circle);
        var thirdCircle = (TextView)findViewById(R.id.third_circle);
        var zoomIn = (TextView)findViewById(R.id.zoom_in);
        var zoomOut = (TextView)findViewById(R.id.zoom_out);

        // Fetch the zooming setting saved
        int scaleOfCirclesSaved = preferences.getInt("scaleOfCircles", 300);

        // Reflect zooming setting
        ZoomingService zoomingService = new ZoomingService(this);
        zoomingService.reflectZoomingSetting(this, zoomIn, firstCircle, secondCircle, thirdCircle, scaleOfCirclesSaved);

        // Start polling friends
        startPollingFriends();
        displayFriends(mainViewModel, range, Double.POSITIVE_INFINITY, 480, true);

        // Start zooming in and out service
        zoomingService.zoomIn(this, zoomIn, firstCircle, secondCircle, thirdCircle);
        zoomingService.zoomOut(this, zoomOut, firstCircle, secondCircle, thirdCircle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);

        // Save the zooming setting
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("scaleOfCircles", scaleOfCircles);
        editor.apply();
    }

    public int getScaleOfCircles() {
        return scaleOfCircles;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setScaleOfCircles(int scaleOfCircles) {
        this.scaleOfCircles = scaleOfCircles;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            accelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            magnetometerSet = true;
        }

        if (accelerometerSet && magnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);
            currentAzimuth = (float) Math.toDegrees(orientation[0]);
            currentAzimuth = (currentAzimuth + 360) % 360;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // get implementation for accurate orientation
    }

    private void startPollingFriends() {
        // live updating for friends already in the database (when you rerun the program)
        LiveData<List<Friend>> friendsLiveData = friendListViewModel.getAll();
        friendsLiveData.observe(this, new Observer<List<Friend>>() {
            //grabs the list of friends
            @Override
            public void onChanged(List<Friend> friendList) {
                friendsLiveData.removeObserver(this);
                if (friendList != null) {
                    //for each friend, if its not the user then grabs its live data and poll from it
                    for (Friend friend : friendList) {
                        if (friend.order != -1) {
                            LiveData<Friend> friendLiveData = friendListViewModel.getFriend(friend.getUid());
                            friendLiveData.observe(MainActivity.this, new Observer<Friend>() {
                                @Override
                                public void onChanged(Friend friend) {
                                    ConstraintLayout mainLayout = findViewById(R.id.main_layout);

                                    // check if the user deleted his/her friend, if so remove the friendIcon and stop observing
                                    if (!friendListViewModel.existsLocal(friend.getUid())) {
                                        friendLiveData.removeObserver(this);
                                        mainLayout.removeView(friendIcons.remove(friend.getUid()).getFriendIcon());
                                    } else {
                                        double friendLat = friend.getLatitude();
                                        double friendLong = friend.getLongitude();
                                        double newDist = Utilities.recalculateDistance(UserLatitude,
                                                UserLongitude,friendLat, friendLong);

                                        friend.setDistance(newDist);
                                        int zone = Utilities.getFriendZone(newDist, scaleOfCircles);

                                        float bearingAngle = Bearing.bearing(UserLatitude,
                                                UserLongitude, friendLat, friendLong);

                                        bearingAngle = (((bearingAngle - currentAzimuth) % 360) + 360) % 360;

                                        friend.setBearingAngle(bearingAngle);

                                        friendListViewModel.saveLocal(friend);

                                        boolean isWithinRange = newDist < range;

                                        if (friendIcons != null && friendIcons.containsKey(friend.getUid())) {
                                            FriendIcon icon = friendIcons.get(friend.getUid());

                                            // check if there is overlap
                                            if (icon != null && icon.getOverlapIconUID() != null
                                                    && friendIcons.containsKey(icon.getOverlapIconUID())){
                                                FriendIcon overlapIcon = friendIcons.get(icon.getOverlapIconUID());
                                                // check whether the the overlapIcon was shifted closer to the center or further and set offset to it
                                                int offset = overlapIcon.getOverlapIsCloser() ? 75 : -75;

                                                // if there is still overlap, return and don't update the icons, otherwise remove overlap
                                                if (zone == overlapIcon.getRadius() + offset
                                                        && Math.abs(overlapIcon.getBearingAngle() - bearingAngle) <= 10){
                                                    return;
                                                }
                                                else {
                                                    overlapIcon.setOverlapIconUID(null);
                                                }
                                            }
                                            mainLayout.removeView(friendIcons.get(friend.getUid()).getFriendIcon());
                                        }

                                        // if the icon is within range, then check if there is any overlap, if so grab the uid of the icon that it is overlapping
                                        String overlapIconUID = isWithinRange ? stackLabels(mainLayout,friend.getUid(),zone,bearingAngle) : "";

                                        // offset to shift the icon further from the circle if there is overlap
                                        int offset = overlapIconUID.equals("") ? 0 : 75;

                                        // create a new friendIcon with updated bearing, zone, and distance
                                        FriendIcon friendIcon = new FriendIcon(MainActivity.this, friend.getName(), bearingAngle, zone + offset, newDist, isWithinRange);

                                        boolean truncate = false;

                                        // if there is overlap, we want to make note of that
                                        if(offset > 0){
                                            friendIcon.setOverlapIconUID(overlapIconUID);
                                            friendIcon.setOverlapIsCloser(false);

                                            // check if we want to truncate the icon
                                            truncate = bearingAngle > 225 && bearingAngle < 315 ;
                                        }
                                        friendIcon.createIcon(truncate);
                                        mainLayout.addView(friendIcon.getFriendIcon());

                                        // add friendIcon to map
                                        friendIcons.put(friend.getUid(), friendIcon);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    // function to check if there are any overlaps, if so shift overlapIcon closer to the circle and return its UID
    private String stackLabels(ConstraintLayout mainLayout, String overlapIconUID, int zone, float bearingAngle){
        FriendIcon friend = null;
        String uid = "";

        // iterate through all of the friendIcons on the screen and check for overlap
        for (HashMap.Entry<String, FriendIcon> friendIcon : friendIcons.entrySet()) {
            String friendUID = friendIcon.getKey();
            FriendIcon value = friendIcon.getValue();

            // check if the icons are in the same zone and the difference of their bearingAngle is within 10 degrees
            if (value.getRadius() == zone && Math.abs(value.getBearingAngle() - bearingAngle) <= 10){
                friend = value;
                uid = friendUID;
                break;
            }
        }

        // if there is no overlap, return ""
        if(friend == null || uid.equals("") || uid.equals(overlapIconUID)){
            return "";
        }

        mainLayout.removeView(friend.getFriendIcon());
        friend.setRadius(friend.getRadius() - 75);
        friend.setOverlapIconUID(overlapIconUID);
        friend.setOverlapIsCloser(true);
        boolean truncate = friend.getBearingAngle() < 135 && friend.getBearingAngle() > 45;
        friend.createIcon(truncate);
        mainLayout.addView(friend.getFriendIcon());
        friendIcons.put(uid, friend);
        return uid;
    }

    private MainActivityViewModel setupMainViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private FriendListViewModel setupFriendListViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }

    private void setFriends(List<Friend> friends1, List<Friend> friends2) {
        friends1 = friends2;
    }

    private void displayFriends(MainActivityViewModel viewModel, double inner, double outer,
                                int radius, boolean isWithinRange) {
        LiveData<List<Friend>> liveDataFriends = viewModel.getFriendsWithinZone(inner, outer);
        List<Friend> friends = new ArrayList<>();
        liveDataFriends.observeForever(new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friendsWithinZone) {
                if (friendsWithinZone != null) {
                    setFriends(friends, friendsWithinZone);
                }
            }
        });

        for (Friend friend : friends) {
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            FriendIcon friendIcon = new FriendIcon(this, friend.getName(), friend.getBearingAngle(),
                    radius, friend.getDistance(), isWithinRange);
            double friendLat = friend.getLatitude();
            double friendLong = friend.getLongitude();
            double newDist = Utilities.recalculateDistance(UserLatitude, UserLongitude, friendLat, friendLong);

            int zone = Utilities.getFriendZone(newDist, scaleOfCircles);
            String overlapIconUID = isWithinRange ? stackLabels(mainLayout,friend.getUid(),zone,friend.getBearingAngle()) : "";
            int offset = overlapIconUID.equals("") ? 0 : 75;

            boolean truncate = false;

            // if there is overlap, we want to make note of that
            if(offset > 0){
                friendIcon.setOverlapIconUID(overlapIconUID);
                friendIcon.setOverlapIsCloser(false);

                // check if we want to truncate the icon
                truncate = friend.getBearingAngle() > 225 && friend.getBearingAngle() < 315 ;
            }
            friendIcon.createIcon(truncate);
            mainLayout.addView(friendIcon.getFriendIcon());
        }
    }

    private MainActivityViewModel setupViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    @NonNull
    private FriendAdapter setupAdapter(MainActivityViewModel viewModel) {
        FriendAdapter adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        viewModel.getFriends().observe(this, adapter::setFriends);
        return adapter;
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("myLatitude", latLong.first.floatValue());
        editor.putFloat("myLongitude", latLong.second.floatValue());
        editor.apply();

        Gson gson = new Gson();
        String json = preferences.getString("self", "");
        self = gson.fromJson(json, Friend.class);

        UserLatitude = latLong.first;
        UserLongitude = latLong.second;

        if (self != null) {
            if (self.getLatitude() != latLong.first || self.getLongitude() != latLong.second) {
                self.setLatitude(latLong.first);
                self.setLongitude(latLong.second);
                // if your distance changed, recompute distance for all friends
                startPollingFriends();
                friendListViewModel.saveLocal(self);
            }
        }
    }

    // This method should only be called one time EVER - for initializing brand new users.
    private void initNewUser() {
        Utilities.showUserNamePromptAlert(this, "Please enter your name");
    }

    public void onSeeFriendsClicked(View view) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void setInactiveTimeText(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String timeStr;
        if (hours > 0) {
            timeStr = hours + "h";
        } else if (minutes >= 1) {
            timeStr = minutes + "m";
        } else {
            timeStr = "<1m";
        }
        TextView lastActiveTimeText = this.findViewById(R.id.gps_status);
        if (seconds != 0) {
            // update the duration if we are inactive
            lastActiveTimeText.setText(timeStr + " ago");
        } else {
            // indicate GPS is live otherwise
            lastActiveTimeText.setText("LIVE");
        }
    }

    public MainActivity getActivity() {
        return this;
    }

    public void setIconVisibility(long seconds) {
        ImageView redIcon = this.findViewById(R.id.red_btn);
        ImageView greenIcon = this.findViewById(R.id.green_btn);
        if (seconds != 0) {
            greenIcon.setVisibility(View.INVISIBLE);
            redIcon.setVisibility(View.VISIBLE);
        } else {
            greenIcon.setVisibility(View.VISIBLE);
            redIcon.setVisibility(View.INVISIBLE);
        }
    }

    public Location getLocation() {
        return location;
    }

    // occasionally check for GPS status using a Runnable thread
    Runnable myRunnable = new Runnable() {
        @Override
        @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
        public void run() {
            boolean permission = locationService.checkPermissions();
            if (!permission) {
                return;
            }
            if (locationService.getLastLocation() != null) {
                if (locationService.getLastActiveTime(getActivity()) == locationService.getLastLocation().getTime()) {
                    // GPS signal has gone stale
                    locationService.incrementInactiveDuration(getActivity());
                } else {
                    // GPS signal is live
                    locationService.resetInactiveDuration(getActivity());
                    lastActiveDuration = 0;
                }
                locationService.setLastKnownActiveTime(getActivity());
                // automatically use the last detected location if GPS is off
                locationService.setInactiveDuration(lastActiveDuration
                        + locationService.getSavedLastDuration(getActivity()), getActivity());
                setInactiveTimeText(locationService.getSavedLastDuration(getActivity()));
                setIconVisibility(locationService.getSavedLastDuration(getActivity()));
                handler.postDelayed(this, 1000);
            }
        }
    };

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void updateFriendOrientation(Location myLocation, Friend friend) {
        if (locationService == null) {
            return;
        }
        if (myLocation == null) {
            return;
        }

        double friendLat = friend.getLatitude();
        double friendLong = friend.getLongitude();
        double newDist = Utilities.recalculateDistance(myLocation.getLatitude(), myLocation.getLongitude(), friendLat, friendLong);

        friend.setDistance(newDist);

        float bearingAngle = Bearing.bearing(myLocation.getLatitude(), myLocation.getLongitude(), friendLat, friendLong);
        bearingAngle = (((bearingAngle - currentAzimuth) % 360) + 360) % 360;
        friend.setBearingAngle(bearingAngle);
    }

    public void setCurrentAzimuth(float azimuth) {
        currentAzimuth = azimuth;
    }

    public Location getCurrentLocation() {
        return location;
    }

    /**
     * Calculating distance for mocking
     * @param location1 First location for distance
     * @param location2 Second location for distance
     * @return The distance between these locations
     */
    public float calculateDistance(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }
        return location1.distanceTo(location2);
    }
}