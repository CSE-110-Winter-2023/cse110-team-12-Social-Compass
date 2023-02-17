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
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private LocationService locationService;
    private LocationService mockCoordinates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        locationService = LocationService.singleton(this);
        TextView textView = (TextView) findViewById(R.id.serviceTextView);

        double geiselLat = 32.88114549458315d;
        double geiselLong = -117.23758450131251d;

        TextView bearing_angle = (TextView) findViewById(R.id.bearingAngle);
        TextView houseIcon = (TextView) findViewById(R.id.house_icon);


        locationService.getLocation().observe(this, loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));
            double angle = Bearing.bearing(loc.first, loc.second, geiselLat, geiselLong);
            bearing_angle.setText(Double.toString(angle));
            //createButtons((int)angle);

        });

    }
    }
}