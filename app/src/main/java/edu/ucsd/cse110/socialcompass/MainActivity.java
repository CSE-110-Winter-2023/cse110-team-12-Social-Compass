package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;

import java.util.*;

import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProfile();
    }

    public void loadProfile() {

        SharedPreferences preferences = getSharedPreferences("mainPrefs", MODE_PRIVATE);

        // check if this is a new user, and if so, initialize their sharedPreferences
        Boolean newUser = preferences.getBoolean("newUser", true);
        initNewUser();
    }

    // This method should only be called one time EVER - for initializing brand new users.
    private void initNewUser() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //TODO: Ask for location permission and build initial HashMap for storing data
        //HashMap<Double latitude, Double longitutude, String name> location = new HashMap<>();

        // HashMap containing (label, coordinates) pairs inputted by the User
        HashMap<String, Pair<Float,Float>> userInputHashSet = new HashMap<String, Pair<Float,Float>>();

//        // Prompt user to input their home coordinates
        //Utilities.showAlertDialog(this, "Please input your Home coordinates");
        Utilities.showFirstAlertonLoad(this, "Please input your Home, Friend, and Parent " +
                "coordinates");

        //below line is set to "true" for testing purposes
        editor.putBoolean("newUser", true);
        editor.apply();
    }

    public void onAddLocationClicked(View view) {
        Intent intent = new Intent(this, LocationListActivity.class);
        startActivity(intent);
    }
}