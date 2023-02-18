package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class Dynamic_Button {
    private float bearingAngle;
    private Context context;
    static final int radius = 495; //some hard-coded value
    private String label;
    private TextView button;


    public Dynamic_Button(Context context, float bearingAngle) {
        this.context = context;
        this.bearingAngle = bearingAngle;
    }

    public void updateAngle(float bearingAngle) {
        this.bearingAngle = bearingAngle;
    }

    public float getBearingAngle() {
        return bearingAngle;
    }

    public TextView getButton() {
        return button;
    }

    public void createButton() {
        button = new TextView(context);
        button.setId(View.generateViewId());
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.house_icon));
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                150, 150
        );
        layout.circleRadius = radius;
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = bearingAngle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        button.setLayoutParams(layout);
    }

    public void updateButtonLayout() {
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        layout.circleAngle = bearingAngle;
        button.setLayoutParams(layout);
    }

    public String getLabel() {
        return this.label;
    }
}



/*
public class Dynamic_Button {
    private float bearingAngle;
    private Context context;
    static final int radius = 495; //some hard-coded value
    private String label;

    //Pair<Double, Double> loc; //(Long, Lat) or was it (Lat, Long)?

    public Dynamic_Button(Context context, float bearingAngle) {
        this.context = context;
        this.bearingAngle = bearingAngle;
        //this.label = label;
    }

    public void updateAngle(float new_angle){
        this.bearingAngle = new_angle;
    }

/*
    public displayLabel(){

    }*/
/*
    public void createButton() {
        TextView sample = new TextView(context);
        sample.setId(View.generateViewId());
        sample.setBackground(ContextCompat.getDrawable(context, R.drawable.house_icon));
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                150, 150
        );
        layout.circleRadius = 490;
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = bearingAngle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        sample.setLayoutParams(layout);

        ViewGroup mainLayout = (ViewGroup) ((Activity) context).findViewById(R.id.main_layout);
        mainLayout.addView(sample);
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