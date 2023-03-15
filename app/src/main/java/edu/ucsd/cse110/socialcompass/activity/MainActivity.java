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
                                        double newDist = Utilities.recalculateDistance(UserLatitude,UserLongitude,friendLat, friendLong);
                                        friend.setDistance(newDist);
                                        int zone = Utilities.getFriendZone(newDist);
                                        float bearingAngle = Bearing.bearing(UserLatitude, UserLongitude, friendLat, friendLong);
                                        friend.setBearingAngle(bearingAngle);
                                        friendListViewModel.saveLocal(friend);


                                        boolean isWithinRange = newDist < range;

                                        // check if there is a friendIcon with the UID on the screen, if so delete it
                                        if (friendIcons != null && friendIcons.containsKey(friend.getUid())) {
                                            FriendIcon icon = friendIcons.get(friend.getUid());

                                            if(icon != null && icon.getOverlapIconUID() != null){
                                                FriendIcon overlapIcon = friendIcons.get(icon.getOverlapIconUID());
                                                int offset = overlapIcon.getOverlapIsCloser() ? 75 : -75;
                                                if(zone == overlapIcon.getRadius() + offset && Math.abs(overlapIcon.getBearingAngle() - bearingAngle) <= 10){
                                                    return;
                                                }
                                                else{
                                                    overlapIcon.setOverlapIconUID(null);

                                                }
                                            }

                                            mainLayout.removeView(friendIcons.get(friend.getUid()).getFriendIcon());
                                        }

                                        // offset for stacking, moving the friendIcon
                                        String overlapIconUID = isWithinRange ? stackLabels(mainLayout,friend.getUid(),zone,bearingAngle) : "";

                                        int offset = overlapIconUID.equals("") ? 0 : 75;

                                        // create a new friendIcon with updated bearing, zone, and distance
                                        FriendIcon friendIcon = new FriendIcon(MainActivity.this, friend.getName(), bearingAngle, zone + offset, newDist, isWithinRange);

                                        boolean truncate = false;

                                        if(offset > 0){
                                            friendIcon.setOverlapIconUID(overlapIconUID);
                                            friendIcon.setOverlapIsCloser(false);
                                            truncate = bearingAngle > 225 && bearingAngle < 315;
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

    private String stackLabels(ConstraintLayout mainLayout, String overlapIconUID, int zone, float bearingAngle){
        FriendIcon friend = null;
        String uid = "";
        for (HashMap.Entry<String, FriendIcon> friendIcon : friendIcons.entrySet()) {
            String friendUID = friendIcon.getKey();
            FriendIcon value = friendIcon.getValue();
            if (value.getRadius() == zone && Math.abs(value.getBearingAngle() - bearingAngle) <= 10){
                friend = value;
                uid = friendUID;
                break;
            }
        }

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

}