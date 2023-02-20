package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private OrientationService orientationService;
    private HashMap<String, Dynamic_Button> dynamic_buttons;
    private HashMap<String, Pair<Double,Double>> sampleHashSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        //Populating hashmap for quick reference
        sampleHashSet = new HashMap<String, Pair<Double,Double>>();
        sampleHashSet.put("Geisel", new Pair<>(32.88114549458315d,-117.23758450131251d ));
        sampleHashSet.put("Rimac", new Pair<>(32.885159942166624d, -117.24044656136009d ));
        sampleHashSet.put("Boston", new Pair<>(42.3199d, -71.0359d ));

        LocationService locationService = LocationService.singleton(this);

        // Create a list to hold ButtonCreator objects
        dynamic_buttons = new HashMap<>();

        locationService.getLocation().observe(this, loc->{

            // Iterate through the ButtonCreator objects, update their bearing angles,
            // and display them in the layout
            for(String i : sampleHashSet.keySet()){
                float angle = Bearing.bearing(loc.first, loc.second, sampleHashSet.get(i).first, sampleHashSet.get(i).second);

                if (dynamic_buttons.size() < sampleHashSet.size()) {
                    dynamic_buttons.put(i,new Dynamic_Button(this, i, (float) angle));
                }
                else {
                    dynamic_buttons.get(i).updateAngle(angle);
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
        float north_angle = 360 - degrees;
        float east_angle = 450 - degrees;
        float south_angle = 180 - degrees;
        float west_angle = 270 - degrees;
        setDirectionLayout(north_angle,northView);
        setDirectionLayout(east_angle,eastView);
        setDirectionLayout(south_angle,southView);
        setDirectionLayout(west_angle,westView);

        //rotating house buttons
        for (Dynamic_Button button : dynamic_buttons.values()) {
            Button dynamic_button = (Button) button.getButton();
            float bearing_angle = button.getBearingAngle();
            float rotated_angle = north_angle + bearing_angle;
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
}
