package edu.ucsd.cse110.socialcompass.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendAPI;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;

public class FriendListActivity extends AppCompatActivity {
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;
    private LocationService locationService;
    private Friend self;
    private String UserName, UserUID;
    private double latitude, longitude;
    static boolean isInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // For first time users, get their uid and name from sharedPreferences
        SharedPreferences preferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        UserName = preferences.getString("myName", "Error getting name");
        UserUID = preferences.getString("myUID", "Error getting UID");
        boolean newUser = preferences.getBoolean("newUser", true);

        System.out.println(newUser);

        var viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel);
        setupViews(viewModel, adapter);

        // Get the updated latitude and longitude of the user
        locationService = LocationService.singleton(this);
        var locationData = locationService.getLocation();
//        latitude = locationData.getValue().first;
//        longitude = locationData.getValue().second;

        // if this is a new user, add them to the database
        if (newUser) {
            self = new Friend(UserName, UserUID, latitude, longitude,-1);
            viewModel.save(self);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("newUser", false);
            editor.apply();
        }

//        locationService.getLocation().observe(this, loc -> {
//            self.setLatitude(locationService.getLocation().getValue().first);
//            self.setLongitude(locationService.getLocation().getValue().second);
//            Log.i("Location", self.latitude + "," + self.longitude);
//        });
    }

    private FriendListViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }

    @NonNull
    private FriendAdapter setupAdapter(FriendListViewModel viewModel) {
        FriendAdapter adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnFriendClickListener(friend -> onFriendClicked(friend, viewModel));
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
            //var friend = viewModel.getFriend(uid).getValue();
            LiveData<Friend> liveFriend = viewModel.getFriend(uid);


            liveFriend.observe(this, new Observer<Friend>() {
                @Override
                public void onChanged(Friend friend) {
                    if (friend == null) {
//                        Utilities.showErrorAlert(this, "Error: Cannot find friend");
                        System.out.println("error");
                    } else {
                        friend.uid = uid;
                        viewModel.save(friend);
                    }
                }
            });


//            //TODO: if friend is null, catch and display an alertidalog error to user
//            FriendAPI api = new FriendAPI();
//            if (api.getFriend(uid) == null || friend == null) {
//                Utilities.showErrorAlert(this, "Error: Cannot find friend");
//            } else {
//                friend.uid = uid;
//                viewModel.save(friend);
//            }
        });
    }

    private void onFriendClicked(Friend friend, FriendListViewModel viewModel) {
        Log.d("FriendAdapter", "Opened friend " + friend.name);
        var intent = FriendActivity.intentFor(this, friend);
        startActivity(intent);
    }

    public static boolean checkInsert() {
        return isInserted;
    }


}