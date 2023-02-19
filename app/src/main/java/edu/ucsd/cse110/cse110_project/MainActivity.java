package edu.ucsd.cse110.cse110_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//        }
//
//        locationService = LocationService.singleton(this);
//
//        TextView textView = (TextView) findViewById(R.id.serviceTextView);
//
//        locationService.getLocation().observe(this, loc-> {
//            textView.setText(Double.toString(loc.first) + ", " +
//                    Double.toString(loc.second));
//        });

        orientationService = OrientationService.singleton(this);
        this.reobserveOrientation();
    }

    public void reobserveOrientation() {
        var orientationData = orientationService.getOrientation();
        orientationData.observe(this, this::onOrientationChanged);
    }

    private void onOrientationChanged(Float orientation) {
        TextView orientationText = findViewById(R.id.serviceTextView);
        float degrees = (float) Math.toDegrees(orientation);
        if (1 / degrees == Double.POSITIVE_INFINITY) degrees = 180;
        if (degrees < 0) {
            degrees += 360;
        }
        orientationText.setText(Utilities.formatOrientation(degrees));

        // rotating the NESW signs
        TextView northView = (TextView) findViewById(R.id.north);
        TextView eastView = (TextView) findViewById(R.id.east);
        TextView southView = (TextView) findViewById(R.id.south);
        TextView westView = (TextView) findViewById(R.id.west);
        float north_angle = 360 - degrees;
        float east_angle = 450 - degrees;
        float south_angle = 180 - degrees;
        float west_angle = 270 - degrees;
        setLayout(north_angle,northView);
        setLayout(east_angle,eastView);
        setLayout(south_angle,southView);
        setLayout(west_angle,westView);
    }

    private void setLayout(float angle, TextView view){
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                55, 100
        );
        layout.circleRadius = 495;
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = angle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        view.setLayoutParams(layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }
}
