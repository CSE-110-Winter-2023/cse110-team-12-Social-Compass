package edu.ucsd.cse110.socialcompass.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.ucsd.cse110.socialcompass.Bearing;
import edu.ucsd.cse110.socialcompass.Constants;
import edu.ucsd.cse110.socialcompass.FriendIcon;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.activity.FriendListActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private String UID; // The user's unique UID
    private LiveData<Friend> user;
    private MainActivityViewModel mainViewModel;
    private FriendListViewModel friendListViewModel;
    private double UserLatitude, UserLongitude;
    private Friend self;    // adding any new user to list of friends
    private int range = 10;
    private HashMap<String, FriendIcon> friendIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is new
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean newUser = preferences.getBoolean("newUser", true);
        if (newUser) {
            initNewUser();
        }
        // TODO: add test to make sure UID will always pull correctly from shared preferences
        UID = preferences.getString("myUID", "Default UID");

        // Setup ViewModel and Adapter
        mainViewModel = setupMainViewModel();
        friendListViewModel = setupFriendListViewModel();
        var adapter = setupAdapter(mainViewModel);

        // Setup location service
        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        friendIcons = new HashMap<>();

        // Start polling friends
        startPollingFriends();

        // ... (rest of the code)
        displayFriends(mainViewModel, 10.0, Double.POSITIVE_INFINITY, 480, true);

    }

    private void startPollingFriends() {
        // live updating for friends already in the database (when you rerun the program)
        LiveData<List<Friend>> friendsLiveData = friendListViewModel.getAll();
        friendsLiveData.observe(this, new Observer<>() {
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
                                    if (!friendListViewModel.existsLocal(friend.getUid())) {
                                        friendLiveData.removeObserver(this);
                                        mainLayout.removeView(friendIcons.remove(friend.getUid()).getFriendIcon());
                                    } else {
                                        double friendLat = friend.getLatitude();
                                        double friendLong = friend.getLongitude();
                                        double newDist = recalculateDistance(friendLat, friendLong);
                                        friend.setDistance(newDist);
                                        int zone = Utilities.getFriendZone(newDist);
                                        float bearingAngle = Bearing.bearing(UserLatitude,
                                                UserLongitude, friendLat, friendLong);
                                        friend.setBearingAngle(bearingAngle);
                                        friendListViewModel.saveLocal(friend);

                                        boolean isWithinRange = newDist < range;

                                        if (friendIcons != null && friendIcons.containsKey(friend.getUid())) {
                                            mainLayout.removeView(friendIcons.get(friend.getUid()).getFriendIcon());
                                        }

                                        FriendIcon friendIcon = new FriendIcon(MainActivity.this,
                                                friend.getName(), bearingAngle, zone, newDist, isWithinRange);
                                        friendIcon.createIcon();
                                        mainLayout.addView(friendIcon.getFriendIcon());

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

    private double recalculateDistance(double friendLat, double friendLong) {
        float[] results = new float[2];
        Location.distanceBetween(UserLatitude, UserLongitude,
                friendLat, friendLong, results);
        return LocationService.metersToMiles(results[0]);
    }

    private MainActivityViewModel setupMainViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private FriendListViewModel setupFriendListViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }
    private void setFriends(List<Friend> friends1, List<Friend> friends2){
        friends1 = friends2;
    }

    private void displayFriends(MainActivityViewModel viewModel, double inner, double outer,
                                int radius, boolean isWithinRange){
        // .getValue() seems to return null for live data, so this implementation assumes it doesn't return null
        LiveData<List<Friend>> liveDataFriends = viewModel.getFriendsWithinZone(inner,outer);
        List<Friend> friends = new ArrayList<>();
        liveDataFriends.observeForever(new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friendsWithinZone) {
                if(friendsWithinZone != null){
                    setFriends(friends, friendsWithinZone);
                }
            }
        });

        // hardcoded
        double distance = 0.1;
        float bearingAngle = 180;

        for (Friend friend : friends) {
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            FriendIcon friendIcon = new FriendIcon(this, friend.getName(), bearingAngle,
                    radius, distance, isWithinRange);
            friendIcon.createIcon();
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

    private String getInactiveTime() {
        //locationService.getInactiveDuration().observe();
        long seconds = locationService.getInactiveDuration() / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String timeStr;
        if (hours > 0) {
            timeStr = hours + "h";
        } else if (minutes > 3) {
            timeStr = minutes + "m";
        } else {
            timeStr = "<3m";
        }
        // Display the inactive time using a Handler on the UI thread
        new Handler(getMainLooper()).post(() -> {
            // Display the inactive time in a TextView
            //textView.setText(timeStr);
        });
        //SharedPreferences.Editor editor = preferences.edit();

        return timeStr;
    }
}