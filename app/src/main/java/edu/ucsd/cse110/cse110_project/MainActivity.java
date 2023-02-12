package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

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
        mockCoordinates = LocationService.singleton(this);
        mockCoordinates.setMockOrientationSource((MutableLiveData<Pair<Double, Double>>) mockCoordinates.getLocation());

        TextView textView = (TextView) findViewById(R.id.serviceTextView);


        Location dest = new Location("");
        dest.setLatitude(32.8819389463388d);
        dest.setLongitude(-117.23756590001862d);
        Location myLocation = new Location(LocationManager.GPS_PROVIDER); //LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        TextView bearing_angle = (TextView) findViewById(R.id.bearingAngle);


        float bearing = myLocation.bearingTo(dest);

        //Convert bearingTo to realBearing(which is 0-270deg
        if(bearing < 0)
        {
            bearing = bearing+360;

        }


        //float bearing = ((Location)locationService).bearingTo(mockCoordinates);
        locationService.getLocation().observe(this, loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));

        });
        bearing_angle.setText(Float.toString(bearing));
    }
}