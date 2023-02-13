package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
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
        //mockCoordinates = LocationService.singleton(this);
        //mockCoordinates.setMockOrientationSource((MutableLiveData<Pair<Double, Double>>) mockCoordinates.getLocation());

        TextView textView = (TextView) findViewById(R.id.serviceTextView);



//        dest.setLatitude(32.8819389463388d);
//        dest.setLongitude(-117.23756590001862d);

        double geiselLat = 32.88114549458315d;
        double geiselLong = -117.23758450131251d;
//
//        LiveData<Pair<Double, Double>> currLoc = locationService.getLocation();
//        Location myLocation = new Location(String.valueOf(locationService));
//        myLocation.setLatitude(currLoc.getValue().first);
//        myLocation.setLongitude(currLoc.getValue().second);
//
//        //Location myLocation = new Location(LocationManager.GPS_PROVIDER); //LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        TextView bearing_angle = (TextView) findViewById(R.id.bearingAngle);
//
//
//        float bearing = myLocation.bearingTo(dest);
//
//        //Convert bearingTo to realBearing(which is 0-270deg
//        if(bearing < 0)
//        {
//            bearing = bearing+360;
//
//        }Location dest = new Location("");


        TextView bearing_angle = (TextView) findViewById(R.id.bearingAngle);
        TextView houseIcon = (TextView) findViewById(R.id.house_icon);

        //ConstraintSet set = new ConstraintSet();



        //float bearing = myLocation.bearingTo(mock);
        //float bearing = ((Location)locationService).bearingTo(mockCoordinates);
        locationService.getLocation().observe(this, loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));
            double angle = Bearing.bearing(loc.first, loc.second, geiselLat, geiselLong);
            bearing_angle.setText(Double.toString(angle));

//            ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.circle_constraint); // No need if you have already done.
//            ConstraintSet c = new ConstraintSet(); // Make new ConstraintSet.
//            c.clone(cl); // Cloning from our ConstraintLayout or else our ConstraintSet won't get affect on ConstraintLayout.
//            c.constrainCircle(R.id.house_icon, R.id.location_icon, 175, (float) angle); // Applying our circle constraint, use anything replacing 40 for radius & 45 for any angle.
//            c.applyTo(cl); // Apply back our ConstraintSet on ConstraintLayout.

            //set.connect(bearingAngle.getId(), );
            //set.applyTo();


        });
        //bearing_angle.setText(Float.toString(Bearing.bearing()));
    }
}