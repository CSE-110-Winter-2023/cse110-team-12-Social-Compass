package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
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
        });
    }
}







