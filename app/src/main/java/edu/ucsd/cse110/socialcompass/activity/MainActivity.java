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
        var thirdCircle = (TextView)findViewById(R.id.third_circle);
        var fourthCircle = (TextView)findViewById(R.id.fourth_circle);
        var zoomIn = (TextView)findViewById(R.id.zoom_in);
        var zoomOut = (TextView)findViewById(R.id.zoom_out);

        // Set up animations
        var zoomInFirstFrom100Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_100);
        var zoomInFirstFrom200Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_200);
        var zoomInFirstFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_first_circle_from_300);
        var zoomInSecondFrom200Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_200);
        var zoomInSecondFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_300);
        var zoomInSecondFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_second_circle_from_400);
        var zoomInThirdFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_third_circle_from_300);
        var zoomInThirdFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_third_circle_from_400);
        var zoomInFourthFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_fourth_circle_from_400);
        var zoomOutFirstFrom200Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_200);
        var zoomOutFirstFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_300);
        var zoomOutFirstFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_first_circle_from_400);
        var zoomOutSecondFrom300Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_300);
        var zoomOutSecondFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_400);
        var zoomOutSecondFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_second_circle_from_1000);
        var zoomOutThirdFrom400Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_third_circle_from_400);
        var zoomOutThirdFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_third_circle_from_1000);
        var zoomOutFourthFrom1000Animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_fourth_circle_from_1000);
        zoomInFirstFrom100Animation.setAnimationListener(this);

        // Run animations
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 100) {
                    firstCircle.startAnimation(zoomInFirstFrom100Animation);
                    secondCircle.startAnimation(zoomInSecondFrom200Animation);
                    thirdCircle.startAnimation(zoomInThirdFrom300Animation);
                    fourthCircle.startAnimation(zoomInFourthFrom400Animation);
                    scaleOfCircles = 200;
                } else if(scaleOfCircles == 200) {
                    firstCircle.startAnimation(zoomInFirstFrom200Animation);
                    secondCircle.startAnimation(zoomInSecondFrom300Animation);
                    thirdCircle.startAnimation(zoomInThirdFrom400Animation);
                    scaleOfCircles = 300;
                } else if(scaleOfCircles == 300) {
                    firstCircle.startAnimation(zoomInFirstFrom300Animation);
                    secondCircle.startAnimation(zoomInSecondFrom400Animation);
                    scaleOfCircles = 400;
                }
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scaleOfCircles == 400) {
                    firstCircle.startAnimation(zoomOutFirstFrom400Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom1000Animation);
                    scaleOfCircles = 300;
                } else if(scaleOfCircles == 300) {
                    firstCircle.startAnimation(zoomOutFirstFrom300Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom400Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom1000Animation);
                    scaleOfCircles = 200;
                } else if(scaleOfCircles == 200) {
                    firstCircle.startAnimation(zoomOutFirstFrom200Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom300Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom400Animation);
                    fourthCircle.startAnimation(zoomOutFourthFrom1000Animation);
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