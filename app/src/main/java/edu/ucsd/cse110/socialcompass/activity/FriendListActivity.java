package edu.ucsd.cse110.socialcompass.activity;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.ucsd.cse110.socialcompass.Bearing;
import edu.ucsd.cse110.socialcompass.Constants;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendAPI;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

public class FriendListActivity extends AppCompatActivity {

    public FriendListViewModel viewModel;
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;
    private LocationService locationService;
    private Friend self;    // adding any new user to list of friends
    private String UserName, UserUID;
    private double UserLatitude, UserLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // For first time users, get their uid and name from sharedPreferences
        SharedPreferences preferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        UserName = preferences.getString("myName", "Error getting name");
        UserUID = preferences.getString("myUID", "Error getting UID");
        boolean newUser = preferences.getBoolean("newUser", true);

        viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel);
        setupViews(viewModel, adapter);

        // Get the updated latitude and longitude of the user
        locationService = LocationService.singleton(this);
        reobserveLocation();

        // if this is a new user, add them to the database
        System.out.println("new user " + newUser);
        if (newUser) {
            self = new Friend(UserName, UserUID, UserLatitude, UserLongitude,-1);
            viewModel.save(self);
            Log.d("USER", self.name);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("newUser", false);
            editor.apply();

            Gson gson = new Gson();
            String json = gson.toJson(self);
            editor.putString("self", json);
            editor.apply();

        }
        // self info
        TextView selfName = this.findViewById(R.id.selfName);
        System.out.println(newUser);

        selfName.setText(UserName);

        TextView selfUID = this.findViewById(R.id.selfUID);
        selfUID.setText(UserUID);
    }

    private double recalculateDistance(double friendLat, double friendLong) {
        float[] results = new float[2];
        Location.distanceBetween(UserLatitude, UserLongitude,
                friendLat, friendLong, results);
        return LocationService.metersToMiles(results[0]);
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void onLocationChanged(android.util.Pair<Double, Double> latLong) {
        @SuppressLint("RestrictedApi") TextView locationText = this.findViewById(R.id.selfLocation);
        locationText.setText(latLong.first + ", " + latLong.second);
        SharedPreferences preferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("myLatitude", latLong.first.floatValue());
        editor.putFloat("myLongitude", latLong.second.floatValue());
        editor.apply();
        UserLatitude = preferences.getFloat("myLatitude", 0);
        UserLongitude = preferences.getFloat("myLongitude", 0);

        Gson gson = new Gson();
        String json = preferences.getString("self", "");
        self = gson.fromJson(json, Friend.class);

        if (self != null) {
            if (self.getLatitude() != latLong.first || self.getLongitude() != latLong.second) {
                self.setLatitude(latLong.first);
                self.setLongitude(latLong.second);
                // if your distance changed, recompute distance for all friends
                viewModel.save(self);
            }
        }
    }

    private FriendListViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }

    @NonNull
    private FriendAdapter setupAdapter(FriendListViewModel viewModel) {
        FriendAdapter adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnFriendClickListener(friend -> onFriendClicked(friend, viewModel));
        adapter.setOnFriendDeleteClickListener(friend -> onFriendDeleteClicked(friend, viewModel));
        viewModel.getFriends().observe(this, adapter::setFriends);
        return adapter;
    }

    private void setupViews(FriendListViewModel viewModel, FriendAdapter adapter) {
        setupRecycler(adapter);
        setupInput(viewModel);
        setupAddUIDButton(viewModel);
    }

    // Override the @VisibleForTesting annotation to allow access from this (and only this) method.
    @SuppressLint("RestrictedApi")
    private void setupRecycler(FriendAdapter adapter) {
        // We store the recycler view in a field _only_ because we will want to access it in tests.
        recyclerView = findViewById(R.id.recycler_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void setupInput(FriendListViewModel viewModel) {
        var input = (EditText) findViewById(R.id.UID_text);
        input.setOnEditorActionListener((view, actionId, event) -> {
            // If the event isn't "done" or "enter", do nothing.
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return false;
            }

            // Otherwise, create a new note, persist it...
            var uid = input.getText().toString();
            var friend = viewModel.getFriend(uid);

            // ...wait for the database to finish persisting it...
            friend.observe(this, friendEntity -> {
                // ...stop observing.
                friend.removeObservers(this);
            });

            return true;
        });
    }

    private void setupAddUIDButton(FriendListViewModel viewModel) {
        var input = (EditText) findViewById(R.id.UID_text);
        var addUIDButton = findViewById(R.id.addUID_btn);
        addUIDButton.setOnClickListener((View v) -> {
            String uid = input.getText().toString();

            // Check if this user already exists on the user's local database
            if (viewModel.existsLocal(uid)) {
                Utilities.showErrorAlert(FriendListActivity.this, "Friend already added");
                return;
            }

            // Check if the user exists in the remote database
            if (!viewModel.existsRemote(uid)) {
                Utilities.showErrorAlert(FriendListActivity.this, "Error: Cannot find friend");
                return;
            }

            // Otherwise, create the livedata for the remote friend object and set an observer
            var friendLiveData = viewModel.getFriend(uid);
            //friendLiveData.observe(this,this::onFriendLocationChanged);
            friendLiveData.observe(this, new Observer<>() {
                @Override
                public void onChanged(Friend friend) {
                    // Remove the observer after the first update
                    friendLiveData.removeObserver(this);
                    double friendLat = friend.getLatitude();
                    double friendLong = friend.getLongitude();
                    double newDist = Utilities.recalculateDistance(UserLatitude,UserLongitude,friendLat, friendLong);
                    friend.setDistance(newDist);

                    int zone = Utilities.getFriendZone(newDist);
                    Log.d("ZONE",String.valueOf(zone));

                    float bearingAngle = Bearing.bearing(UserLatitude,UserLongitude,friendLat,friendLong);
                    friend.setBearingAngle(bearingAngle);
                    viewModel.saveLocal(friend);
                }
            });
        });
    }

    private void onFriendClicked(Friend friend, FriendListViewModel viewModel) {
        Log.d("FriendAdapter", "Opened friend " + friend.name);
        var intent = FriendActivity.intentFor(this, friend);
        startActivity(intent);
    }

    private void onFriendDeleteClicked(Friend friend, FriendListViewModel viewModel) {
        // Delete the friend
        Log.d("FriendAdapter", "Deleted friend " + friend.name);
        viewModel.delete(friend);
    }
}