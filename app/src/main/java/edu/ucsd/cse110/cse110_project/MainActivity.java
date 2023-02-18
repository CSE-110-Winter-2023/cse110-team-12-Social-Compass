package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Dynamic_Button> dynamic_buttons;
    private LocationService locationService;
    //private double geiselLat = 32.88114549458315d;
    //private double geiselLong = -117.23758450131251d;
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


        locationService = LocationService.singleton(this);
        TextView textView = (TextView) findViewById(R.id.serviceTextView);
        TextView bearingAngle = (TextView) findViewById(R.id.bearingAngle);

        // Create a list to hold ButtonCreator objects
        dynamic_buttons = new ArrayList<>();

        // Instantiate ButtonCreator objects and add them to the list
//        dynamic_buttons.add(new Dynamic_Button(this, 0f));
//        dynamic_buttons.add(new Dynamic_Button(this, 90f));



        locationService.getLocation().observe(this, loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));
            //double angle = Bearing.bearing(loc.first, loc.second, geiselLat, geiselLong);
            //bearingAngle.setText(Double.toString(angle));

            // Iterate through the ButtonCreator objects, update their bearing angles,
            // and display them in the layout

            for(String i : sampleHashSet.keySet()){
                double angle = Bearing.bearing(loc.first, loc.second, sampleHashSet.get(i).first, sampleHashSet.get(i).second);
                dynamic_buttons.add(new Dynamic_Button(this, (float)angle));


            }

            for (Dynamic_Button button : dynamic_buttons) {
                //buttonCreator.updateAngle((float) 120);

                //buttonCreator.updateAngle();
                ConstraintLayout mainLayout = findViewById(R.id.main_layout);
                mainLayout.removeView(button.getButton());
                button.createButton();
                mainLayout.addView(button.getButton());
            }
        });
    }

//    // Assume that we have a method called startPolling that starts the polling process
//    public void startPolling() {
//        final Handler handler = new Handler();
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                float newBearingAngle = getBearingAngle(); // Get the latest bearing angle
//                if (newBearingAngle != Dynamic_Button.getBearingAngle()) {
//                    // Update the button position if the bearing angle has changed
//                    Dynamic_Button.setBearingAngle(newBearingAngle);
//                    Dynamic_Button.updateButtonLayout();
//                }
//                handler.postDelayed(this, POLL_INTERVAL); // Schedule the next check
//            }
//        };
//
//        handler.postDelayed(runnable, POLL_INTERVAL); // Start the polling process
//    }
//
//    private float getBearingAngle() {
//        // Return the latest bearing angle
//    }

    private static final long POLL_INTERVAL = 1000; // Poll every 1 second

}







