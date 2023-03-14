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

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.socialcompass.FriendIcon;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    private LocationService locationService;
    private String UID; // The user's unique UID
    private LiveData<Friend> user;
    private int scaleOfCircles = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views for zooming
        var firstCircle = (TextView)findViewById(R.id.first_circle);
        var secondCircle = (TextView)findViewById(R.id.second_circle);
        var zoomIn = (TextView)findViewById(R.id.zoom_in);
        var zoomOut = (TextView)findViewById(R.id.zoom_out);

        // Set up animations
        var zoomInFirstFrom50Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_50);
        var zoomInFirstFrom100Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_100);
        var zoomOutFirstFrom100Animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_100);
        var zoomOutFirstFrom150Animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_150);
        var zoomInSecondFrom50Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_50);
        var zoomInSecondFrom100Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_100);
        var zoomOutSecondFrom100Animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_100);
        var zoomOutSecondFrom150Animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_150);
        zoomInFirstFrom50Animation.setAnimationListener(this);

        // Run animations
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 50) {
                    firstCircle.startAnimation(zoomInFirstFrom50Animation);
                    secondCircle.startAnimation(zoomInSecondFrom50Animation);
                    scaleOfCircles = 100;
                } else if(scaleOfCircles == 100) {
                    firstCircle.startAnimation(zoomInFirstFrom100Animation);
                    secondCircle.startAnimation(zoomInSecondFrom100Animation);
                    scaleOfCircles = 150;
                }
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 100) {
                    firstCircle.startAnimation(zoomOutFirstFrom100Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom100Animation);
                    scaleOfCircles = 50;
                } else if(scaleOfCircles == 150) {
                    firstCircle.startAnimation(zoomOutFirstFrom150Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom150Animation);
                    scaleOfCircles = 100;
                }
            }
        });

        // Check if user is new
        SharedPreferences preferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        boolean newUser = preferences.getBoolean("newUser", true);
        if (newUser) {
            initNewUser();
        }
        // TODO: add test to make sure UID will always pull correctly from shared preferences
        UID = preferences.getString("myUID", "Default UID");

        // Setup ViewModel and Adapter
        var viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel);

        // Setup location service
        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        displayFriends(viewModel, 10.0, Double.POSITIVE_INFINITY, 480, true);
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

    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
        System.out.println("Location: " + Utilities.formatLocation(latLong.first, latLong.second));
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