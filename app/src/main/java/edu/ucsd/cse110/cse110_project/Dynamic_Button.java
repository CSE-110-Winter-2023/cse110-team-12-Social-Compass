package edu.ucsd.cse110.cse110_project;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class Dynamic_Button {
    int angle;
    static final int radius = 495; //some hard-coded value
    String label;

    Pair<Double, Double> loc; //(Long, Lat) or was it (Lat, Long)?

    public Dynamic_Button(Pair<Double, Double> curr, Pair<Double, Double> dest, String label) {

    }

    public void updateAngle(Pair<Double, Double> curr){

    }

    public displayLabel(){

    }

}
/*
    private void createButtons(int bearing_angle) {
        int length = 1;
        //int angle = 360 / (length + 1);
        int angle = bearing_angle;
        for (int i = 0; i <= length; i++) {

            TextView sample = new TextView(this);
            sample.setId(View.generateViewId());
            sample.setBackground(ContextCompat.getDrawable(this, R.drawable.house_icon));
            ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                    150, 150
            );
            layout.circleRadius = 490;
            layout.circleConstraint = R.id.location_icon;
            layout.circleAngle = angle;
            layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            sample.setLayoutParams(layout);

            ViewGroup main_layout = (ViewGroup) findViewById(R.id.main_layout);
            main_layout.addView(sample);
        }
}
*/