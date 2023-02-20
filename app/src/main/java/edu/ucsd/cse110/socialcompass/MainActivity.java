package edu.ucsd.cse110.socialcompass;

import static java.lang.Float.parseFloat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;


public class MainActivity extends AppCompatActivity {
    private HashMap<String, Dynamic_Button> dynamic_buttons;
    private HashMap<String, Pair<Double,Double>> sampleHashSet;

    private OrientationService orientationService;
    private HashMap<String, Pair<Double,Double>> locationHashset;
    private float orientation;

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
//        sampleHashSet.put("Geisel", new Pair<>(32.88114549458315d,-117.23758450131251d ));
//        sampleHashSet.put("Rimac", new Pair<>(32.885159942166624d, -117.24044656136009d ));
//        sampleHashSet.put("Boston", new Pair<>(42.3199d, -71.0359d ));



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

    public void onInputReady() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        SharedPreferences mainPreferences = this.getSharedPreferences("mainPrefs",
                Context.MODE_PRIVATE);

        SharedPreferences preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);


        //Populating hashmap for quick reference
        locationHashset = new HashMap<String, Pair<Double,Double>>();
        double [] parseHomeCoord = Utilities.parseCoordinates(mainPreferences.getString("homeCoords", ""));
        double [] parseFriendCoord = Utilities.parseCoordinates(mainPreferences.getString("friendCoords", ""));
        double [] parseParentCoord = Utilities.parseCoordinates(mainPreferences.getString("parentCoords", ""));

        String homeName = mainPreferences.getString("homeLabel","Home");
        String friendName = mainPreferences.getString("friendLabel","Friend");
        String parentName = mainPreferences.getString("parentLabel","Parent");

        locationHashset.put(homeName, new Pair<>(parseHomeCoord[0],parseHomeCoord[1]));
        locationHashset.put(friendName, new Pair<>(parseFriendCoord[0],parseFriendCoord[1]));
        locationHashset.put(parentName, new Pair<>(parseParentCoord[0],parseParentCoord[1]));
//        locationHashset.put("Geisel", new Pair<>(32.88114549458315d,-117.23758450131251d ));
//        locationHashset.put("Rimac", new Pair<>(32.885159942166624d, -117.24044656136009d ));
//        locationHashset.put("Boston", new Pair<>(42.3199d, -71.0359d ));

        //orientation = (float) preferences.getInt("orientation",0);

        LocationService locationService = LocationService.singleton(this);

        // Create a list to hold ButtonCreator objects
        dynamic_buttons = new HashMap<>();

        locationService.getLocation().observe(this, loc->{

            // Iterate through the ButtonCreator objects, update their bearing angles,
            // and display them in the layout
            for(String i : locationHashset.keySet()){
                float angle = Bearing.bearing(loc.first, loc.second, locationHashset.get(i).first, locationHashset.get(i).second);

                if (dynamic_buttons.size() < locationHashset.size()) {
                    dynamic_buttons.put(i,new Dynamic_Button(this, i, (float) orientation + angle));
                }
                else {
                    dynamic_buttons.get(i).updateAngle(orientation + angle);
                }
            }

            for (Dynamic_Button button : dynamic_buttons.values()) {
                ConstraintLayout mainLayout = findViewById(R.id.main_layout);
                mainLayout.removeView(button.getButton());
                button.createButton();
                mainLayout.addView(button.getButton());
            }
            orientationService = OrientationService.singleton(this);
            this.reobserveOrientation();
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
        HashMap<String, android.util.Pair<Float,Float>> userInputHashSet = new HashMap<String, android.util.Pair<Float,Float>>();

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

    /**
     * method used to grab the orientation data
     */
    private void reobserveOrientation() {
        var orientationData = orientationService.getOrientation();
        orientationData.observe(this, this::onOrientationChanged);
    }

    /**
     * method to use the orientation change value to rotate the NESW directions and house buttons
     * @param orientation the azimuth for the orientation change
     */
    private void onOrientationChanged(Float orientation) {
        // change azimuth value to be in degrees
        float degrees = (float) Math.toDegrees(orientation);

        // set the value of degrees to be positive
        if (1 / degrees == Double.POSITIVE_INFINITY) degrees = 180;
        if (degrees < 0) {
            degrees += 360;
        }

        // rotating the NESW direction signs
        TextView northView = (TextView) findViewById(R.id.north);
        TextView eastView = (TextView) findViewById(R.id.east);
        TextView southView = (TextView) findViewById(R.id.south);
        TextView westView = (TextView) findViewById(R.id.west);
        float north_angle = 360 - degrees + orientation;
        float east_angle = 450 - degrees + orientation;
        float south_angle = 180 - degrees + orientation;
        float west_angle = 270 - degrees + orientation;
        setDirectionLayout(north_angle,northView);
        setDirectionLayout(east_angle,eastView);
        setDirectionLayout(south_angle,southView);
        setDirectionLayout(west_angle,westView);

        //rotating house buttons
        for (Dynamic_Button button : dynamic_buttons.values()) {
            Button dynamic_button = (Button) button.getButton();
            float bearing_angle = button.getBearingAngle();
            float rotated_angle = north_angle + bearing_angle + + orientation;
            setButtonLayout(rotated_angle,dynamic_button);
        }
    }

    /**
     * method use to rotate the direction textView signs
     * @param angle the new angle to update the compass with
     * @param view the text view that corresponds to the NESW direction signs
     */
    private void setDirectionLayout(float angle, TextView view){
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                55, 100
        );
        float outerCircleRadius = (float) this.findViewById(R.id.outer_circle).getHeight() / 2;
        float innerCircleRadius = (float) this.findViewById(R.id.inner_circle).getHeight() / 2;
        float dynamicRadius = ((outerCircleRadius - innerCircleRadius) / 2) + innerCircleRadius;
        layout.circleRadius = (int)Math.floor(dynamicRadius);
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = angle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        view.setLayoutParams(layout);
    }

    /**
     * method use to rotate the house buttons
     * @param angle the new angle to update the compass with
     * @param button the house buttons
     */
    public void setButtonLayout(float angle, Button button){
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                150, 150
        );
        float outerCircleRadius = (float) this.findViewById(R.id.outer_circle).getHeight() / 2;
        float innerCircleRadius = (float) this.findViewById(R.id.inner_circle).getHeight() / 2;
        float dynamicRadius = ((outerCircleRadius - innerCircleRadius) / 2) + innerCircleRadius;
        layout.circleRadius = (int)Math.floor(dynamicRadius);
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = angle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        button.setLayoutParams(layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    public HashMap<String,Pair<Double,Double>> getLocation() {
        return this.locationHashset;
    }

}
