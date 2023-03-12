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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendAPI;
import edu.ucsd.cse110.socialcompass.services.LocationService;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

import java.util.List;
public class FriendListActivity extends AppCompatActivity {
    FriendListViewModel friendListViewModel;
    FriendViewModel friendViewModel;

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;
    private LocationService locationService;
    private Friend self;
    private String UserName, UserUID;
    private double latitude, longitude;
    static boolean isInserted = false;
    private Friend newFriend;

    private int friendListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // For first time users, get their uid and name from sharedPreferences
        SharedPreferences preferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        UserName = preferences.getString("myName", "Error getting name");
        UserUID = preferences.getString("myUID", "Error getting UID");
        boolean newUser = preferences.getBoolean("newUser", true);

        var friendListViewModel = setupViewModel();
        var adapter = setupAdapter(friendListViewModel);
        setupViews(friendListViewModel, adapter);

        // Get the updated latitude and longitude of the user
        locationService = LocationService.singleton(this);
        var locationData = locationService.getLocation();
//        latitude = locationData.getValue().first;
//        longitude = locationData.getValue().second;

        // if this is a new user, add them to the database
        if (newUser) {
            var self = new Friend(UserName, UserUID, latitude, longitude,-1);
            Log.d("USER", self.name);
            friendListViewModel.save(self);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("newUser", false);
            editor.apply();
        }

//        locationService.getLocation().observe(this, loc -> {
//            self.setLatitude(locationService.getLocation().getValue().first);
//            self.setLongitude(locationService.getLocation().getValue().second);
//            Log.i("Location", self.latitude + "," + self.longitude);
//        });
        friendListViewModel.getAll().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friendList) {
                if (friendList != null) {
                    friendListSize = friendList.size();
                }
            }
        });
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
            friendLiveData.observe(this, new Observer<Friend>() {
                @Override
                public void onChanged(Friend friend) {
                    // Remove the observer after the first update
                    friendLiveData.removeObserver(this);
                    // save the friend to the viewModel
                    viewModel.save(friend);
                }
            });
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

    public int getFriendListSize() {
        return this.friendListSize;
    }
}