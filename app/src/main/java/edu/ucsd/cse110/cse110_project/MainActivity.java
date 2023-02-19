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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private HashMap<String, Dynamic_Button> dynamic_buttons;
    private LocationService locationService;
    //private double geiselLat = 32.88114549458315d;
    //private double geiselLong = -117.23758450131251d;
    private HashMap<String, Pair<Double,Double>> sampleHashSet;

    final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

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


        locationService = LocationService.singleton(this);
        TextView textView = (TextView) findViewById(R.id.serviceTextView);
        TextView bearingAngle = (TextView) findViewById(R.id.bearingAngle);

        // Create a list to hold ButtonCreator objects
        dynamic_buttons = new HashMap<>();

        // Instantiate ButtonCreator objects and add them to the list
//        dynamic_buttons.add(new Dynamic_Button(this, 0f));
//        dynamic_buttons.add(new Dynamic_Button(this, 90f));


        LiveData<Pair<Double, Double>> locationLiveData = locationService.getLocation();
        locationService.getLocation().observe(this, loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));

            //double angle = Bearing.bearing(loc.first, loc.second, geiselLat, geiselLong);
            //bearingAngle.setText(Double.toString(angle));

            // Iterate through the ButtonCreator objects, update their bearing angles,
            // and display them in the layout

            for(String i : sampleHashSet.keySet()){
                float angle = Bearing.bearing(loc.first, loc.second, sampleHashSet.get(i).first, sampleHashSet.get(i).second);

                if (dynamic_buttons.size() < sampleHashSet.size()) {
                    dynamic_buttons.put(i,new Dynamic_Button(this, (float) angle));
                }
                else {
                    dynamic_buttons.get(i).updateAngle(angle);
                }

            }

            for (Dynamic_Button button : dynamic_buttons.values()) {
                //buttonCreator.updateAngle((float) 120);

                //button.updateAngle();
                ConstraintLayout mainLayout = findViewById(R.id.main_layout);
                mainLayout.removeView(button.getButton());
                button.createButton();
                mainLayout.addView(button.getButton());
            }
        });
    }

    // Assume that we have a method called startPolling that starts the polling process
//    public void startPolling() {
//
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                for (Dynamic_Button button : dynamic_buttons) {
//                    float newBearingAngle = button.getBearingAngle(); // Get the latest bearing angle
//                    if (newBearingAngle != button.getBearingAngle()) {
//                        // Update the button position if the bearing angle has changed
//                        button.updateAngle(newBearingAngle);
//                        button.updateButtonLayout();
//                    }
//                }
//                handler.postDelayed(this, POLL_INTERVAL); // Schedule the next check
//            }
//        };
//
//        handler.postDelayed(runnable, POLL_INTERVAL); // Start the polling process
//    }
//
//
//    private static final long POLL_INTERVAL = 1000; // Poll every 1 second

}







