package edu.ucsd.cse110.socialcompass.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    private LocationService locationService;
    private String UID; // The user's unique UID
    private LiveData<Friend> user;
    private MainActivityViewModel mainViewModel;
    private FriendListViewModel friendListViewModel;
    private double UserLatitude, UserLongitude;
    private Friend self;    // adding any new user to list of friends
    private int range = 1000;
    private HashMap<String, FriendIcon> friendIcons;
    private int scaleOfCircles = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views for zooming
        var firstCircle = (TextView)findViewById(R.id.first_circle);
        var secondCircle = (TextView)findViewById(R.id.second_circle);
        var thirdCircle = (TextView)findViewById(R.id.third_circle);
        var zoomIn = (TextView)findViewById(R.id.zoom_in);
        var zoomOut = (TextView)findViewById(R.id.zoom_out);

        // Set up animations
        var zoomInFirstFrom100Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_100);
        var zoomInSecondFrom133Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_133);
        var zoomInSecondFrom200Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_200);
        var zoomInThirdFrom267Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_third_circle_from_267);
        var zoomInThirdBackAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_third_circle_back);
        var zoomOutFirstFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_1000);
        var zoomOutSecondFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_1000);
        var zoomOutSecondFrom200Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_200);
        var zoomOutThirdFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_third_circle_from_1000);
        var zoomOutThirdFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_third_circle_from_300);
        zoomInFirstFrom100Animation.setAnimationListener(this);

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

        // Fetch the zooming setting saved
        int scaleOfCirclesSaved = preferences.getInt("scaleOfCircles", 300);

        // Start polling friends
        startPollingFriends();
        displayFriends(mainViewModel, range, Double.POSITIVE_INFINITY, 480, true);

        // Zooming in based on the zooming setting
        if(scaleOfCirclesSaved > 100) {
            firstCircle.startAnimation(zoomInFirstFrom100Animation);
            secondCircle.startAnimation(zoomOutSecondFrom200Animation);
            thirdCircle.startAnimation(zoomOutThirdFrom300Animation);
            range = 500;
            scaleOfCircles = 200;
        }
        if(scaleOfCirclesSaved > 200) {
            secondCircle.startAnimation(zoomInSecondFrom133Animation);
            thirdCircle.startAnimation(zoomInThirdFrom267Animation);
            range = 10;
            scaleOfCircles = 300;
        }
        if(scaleOfCirclesSaved > 300) {
            secondCircle.startAnimation(zoomInSecondFrom200Animation);
            range = 1;
            scaleOfCircles = 400;
        }

        // Run animations
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 100) {
                    firstCircle.startAnimation(zoomInFirstFrom100Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom200Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom300Animation);
                    range = 500;
                    scaleOfCircles = 200;
                } else if(scaleOfCircles == 200) {
                    secondCircle.startAnimation(zoomInSecondFrom133Animation);
                    thirdCircle.startAnimation(zoomInThirdFrom267Animation);
                    range = 10;
                    scaleOfCircles = 300;
                } else if(scaleOfCircles == 300) {
                    secondCircle.startAnimation(zoomInSecondFrom200Animation);
                    range = 1;
                    scaleOfCircles = 400;
                }
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 400) {
                    secondCircle.startAnimation(zoomOutSecondFrom1000Animation);
                    range = 10;
                    scaleOfCircles = 300;
                } else if(scaleOfCircles == 300) {
                    secondCircle.startAnimation(zoomOutSecondFrom200Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom1000Animation);
                    range = 500;
                    scaleOfCircles = 200;
                } else if(scaleOfCircles == 200) {
                    firstCircle.startAnimation(zoomOutFirstFrom1000Animation);
                    secondCircle.startAnimation(zoomInSecondFrom133Animation);
                    thirdCircle.startAnimation(zoomInThirdBackAnimation);
                    range = 1000;
                    scaleOfCircles = 100;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        // Save the zooming setting
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("scaleOfCircles", scaleOfCircles);
        editor.apply();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
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
                                        int zone = Utilities.getFriendZone(newDist, scaleOfCircles);
                                        float bearingAngle = Bearing.bearing(UserLatitude, UserLongitude, friendLat, friendLong);
                                        friend.setBearingAngle(bearingAngle);
                                        friendListViewModel.saveLocal(friend);

                                        boolean isWithinRange = newDist < range;

                                        // check if there is a friendIcon with the UID on the screen, if so delete it
                                        if (friendIcons != null && friendIcons.containsKey(friend.getUid())) {
                                            mainLayout.removeView(friendIcons.get(friend.getUid()).getFriendIcon());
                                        }

                                        // create a new friendIcon with updated bearing, zone, and distance
                                        FriendIcon friendIcon = new FriendIcon(MainActivity.this, friend.getName(), bearingAngle, zone, newDist, isWithinRange);
                                        friendIcon.createIcon();
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
        // .getValue() seems to return null for live data, so this implementation assumes it doesn't return null
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
}