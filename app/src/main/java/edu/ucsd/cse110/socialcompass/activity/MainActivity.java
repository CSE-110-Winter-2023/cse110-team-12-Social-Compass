package edu.ucsd.cse110.socialcompass.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Zone 1
        int radius = Constants.ZONE1_2;
        for(double i = 0.0; i <= 1; i += 0.2){
            displayFriends(viewModel, i, i+0.2 , radius, true);
            radius += 39;
        }

        //Zone 2
        for(double i = 1.0; i <= 10; i += 1.8){
            displayFriends(viewModel, i, i+1.8 , radius,true);
            radius += 39;
        }

        //beyond Zone 2
        displayFriends(viewModel, 10.0, Double.POSITIVE_INFINITY, 390, false);
    }

    private void displayFriends(MainActivityViewModel viewModel, double inner, double outer, int radius, boolean isWithinRange){
        // .getValue() seems to return null for live data, so this implementation assumes it doesnt return null
        LiveData<List<Friend>> liveDataFriends = viewModel.getFriendsWithinZone(inner,outer);
        List<Friend> friends = liveDataFriends.getValue();

        // hardcoded
        double distance = 0.3;
        float bearingAngle = 90;

        for(Friend friend : friends){

            FriendIcon friendIcon = new FriendIcon(this,friend.getLabel(), bearingAngle,radius, distance,isWithinRange);
            friendIcon.createIcon();
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
//        TextView locationText = findViewById(R.id.location_text);
//        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
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