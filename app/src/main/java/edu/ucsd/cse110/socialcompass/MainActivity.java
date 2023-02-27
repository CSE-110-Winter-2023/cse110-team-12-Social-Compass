package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("mainPrefs", MODE_PRIVATE);
        // check if this is a new user, and if so, initialize their sharedPreferences
        Boolean newUser = preferences.getBoolean("newUser", true); // true by default
        if (newUser) { initNewUser(); }
    }

    // This method should only be called one time EVER - for initializing brand new users.
    private void initNewUser() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //TODO: Ask for location permission and build initial HashMap for storing data

        editor.putBoolean("newUser", false); // update newUser status to false.
        editor.apply();
    }

    public void onSeeFriendsClicked(View view) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }
}