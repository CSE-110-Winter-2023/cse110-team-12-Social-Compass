package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;

import android.view.View;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import java.util.HashMap;

import edu.ucsd.cse110.cse110_project.R;

public class MainActivity extends AppCompatActivity {
    private HashMap<String, Dynamic_Button> dynamic_buttons;
    private HashMap<String, Pair<Double,Double>> sampleHashSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting layout for activity
        setContentView(R.layout.activity_main);
        loadProfile();
        //Checking for app permissions and requesting permission if permissions not granted
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        //Populating hashmap for quick reference
        //Get rid of sampleHashSet afterwards
        sampleHashSet = new HashMap<String, Pair<Double,Double>>();
        sampleHashSet.put("Geisel", new Pair<>(32.88114549458315d,-117.23758450131251d ));
        sampleHashSet.put("Rimac", new Pair<>(32.885159942166624d, -117.24044656136009d ));
        sampleHashSet.put("Boston", new Pair<>(42.3199d, -71.0359d ));

        //Creating instance of locationService to get device location
        LocationService locationService = LocationService.singleton(this);

        // Create a list to hold DynamicButton objects
        dynamic_buttons = new HashMap<>();

        //Observing location changes and updating dynamic buttons
        locationService.getLocation().observe(this, loc->{

            // Iterate through the Dynamic_Buttons objects, update their bearing angles,
            // and display them in the layout
            for(String i : sampleHashSet.keySet()){
                float angle = Bearing.bearing(loc.first, loc.second, sampleHashSet.get(i).first, sampleHashSet.get(i).second);

                //Creating dynamic_button object if not already created, otherwise update its angle
                if (dynamic_buttons.size() < sampleHashSet.size()) {
                    dynamic_buttons.put(i,new Dynamic_Button(this, i, (float) angle));
                }
                else {
                    dynamic_buttons.get(i).updateAngle(angle);
                }
            }

            //Displaying the dynamic buttons in the layout
            for (Dynamic_Button button : dynamic_buttons.values()) {
                ConstraintLayout mainLayout = findViewById(R.id.main_layout);
                mainLayout.removeView(button.getButton());
                button.createButton();
                mainLayout.addView(button.getButton());
            }
        });
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

        //setContentView(R.layout.activity_main)
    }
}
