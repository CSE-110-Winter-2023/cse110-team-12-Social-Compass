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

        // Start polling friends
        startPollingFriends();

        // ... (rest of the code)
        displayFriends(mainViewModel, 10.0, Double.POSITIVE_INFINITY, 480, true);

    }

//    private void startPollingFriends() {
//        // live updating for friends already in the database (when you rerun the program)
//        LiveData<List<Friend>> friendsLiveData = friendListViewModel.getAll();
//        friendsLiveData.observe(this, new Observer<List<Friend>>() {
//            //grabs the list of friends
//            @Override
//            public void onChanged(List<Friend> friendList) {
//                friendsLiveData.removeObserver(this);
//                if (friendList != null) {
//                    //for each friend, if its not the user then grabs its live data and poll from it
//                    for (Friend friend : friendList) {
//                        if (!friend.getUid().equals(UID)) {
//                            LiveData<Friend> friendLiveData = friendListViewModel.getFriend(friend.getUid());
//                            friendLiveData.observe(MainActivity.this, new Observer<Friend>() {
//                                @Override
//                                public void onChanged(Friend friend) {
////                                    double newDist = recalculateDistance(friendLat, friendLong);
////                                    friend.setDistance(newDist);
////                                    friendLiveData.removeObserver(this);
////                                    friendListViewModel.saveLocal(friend);
//
//                                    friendLiveData.removeObserver(this);
//                                    double friendLat = friend.getLatitude();
//                                    double friendLong = friend.getLongitude();
//                                    double newDist = recalculateDistance(friendLat, friendLong);
//                                    friend.setDistance(newDist);
//                                    System.out.println(newDist + "INSIDE FRIENDITEM");
//                                    friendListViewModel.saveLocal(friend);
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });
//    }


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
                                    friendLiveData.removeObserver(this);
                                    double friendLat = friend.getLatitude();
                                    double friendLong = friend.getLongitude();
                                    double newDist = recalculateDistance(friendLat, friendLong);
                                    friend.setDistance(newDist);
                                    friendListViewModel.saveLocal(friend);
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

//        private void startPollingFriends(){
//        // live updating for friends already in the database (when you rerun the program)
//        LiveData<List<Friend>> friendsLiveData = friendListViewModel.getAll();
//        friendsLiveData.observe(this, new Observer<List<Friend>>() {
//            //grabs the list of friends
//            @Override
//            public void onChanged(List<Friend> friendList) {
//                friendsLiveData.removeObserver(this);
//                if (friendList != null) {
//                    System.out.println("I AM HERE INSIDE FRIENDLIST");
//                    friendListSize = friendList.size();
//                    //for each friend, if its not the user then grabs its live data and poll from it
//                    for(Friend friend : friendList){
//                        if (friend.order != -1) {
//                            LiveData<Friend> friendLiveData = friendListViewModel.getFriend(friend.getUid());
//                            friendLiveData.observe(FriendListActivity.this, new Observer<Friend>() {
//                                @Override
//                                public void onChanged(Friend friend) {
//                                    friendLiveData.removeObserver(this);
//                                    double friendLat = friend.getLatitude();
//                                    double friendLong = friend.getLongitude();
//                                    double newDist = recalculateDistance(friendLat, friendLong);
//                                    friend.setDistance(newDist);
//                                    System.out.println(newDist + "INSIDE FRIENDITEM");
//                                    viewModel.saveLocal(friend);
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });
//    }
    private MainActivityViewModel setupMainViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private FriendListViewModel setupFriendListViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }
    private void setFriends(List<Friend> friends1,List<Friend> friends2){
        friends1 = friends2;
    }

    private void displayFriends(MainActivityViewModel viewModel, double inner, double outer, int radius, boolean isWithinRange){
        // .getValue() seems to return null for live data, so this implementation assumes it doesnt return null
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

        for(Friend friend : friends){
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            FriendIcon friendIcon = new FriendIcon(this, friend.getName(), bearingAngle,radius, distance,isWithinRange);
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

//    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
//
////        UserLatitude = preferences.getFloat("myLatitude", 0);
////        UserLongitude = preferences.getFloat("myLongitude", 0);
//
//        System.out.println("Location: " + Utilities.formatLocation(latLong.first, latLong.second));
//    }

//    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
//        UserLatitude = latLong.first;
//        UserLongitude = latLong.second;
//
//        System.out.println("Location: " + Utilities.formatLocation(latLong.first, latLong.second));
//    }
    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("myLatitude", latLong.first.floatValue());
        editor.putFloat("myLongitude", latLong.second.floatValue());
        editor.apply();
        //UserLatitude = preferences.getFloat("myLatitude", 0);
        //UserLongitude = preferences.getFloat("myLongitude", 0);

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