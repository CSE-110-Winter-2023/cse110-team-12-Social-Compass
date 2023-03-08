package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.Manifest;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static FriendDatabase db;
    private static FriendListItemDao dao;
    private LocationService locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checking for app permissions and requesting permission if permissions not granted
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }
        setContentView(R.layout.activity_main);
        Context context = this.getApplicationContext();
        db = FriendDatabase.getSingleton(context);
        var dao = db.friendListItemDao();
        List<FriendListItem> users = dao.getAll();
        locationManager = LocationService.singleton(this);
        if (users.size() == 0) {
            initNewUser();
        }

    }

    public static FriendListItemDao getDao()
    {
        return db.friendListItemDao();
    }



    // This method should only be called one time EVER - for initializing brand new users.
    private void initNewUser() {
        //TODO: Ask for location permission and build initial HashMap for storing data
        Utilities.showUserNamePromptAlert(this, "Please enter your name",db, locationManager);
    }

    public void onSeeFriendsClicked(View view) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }
}