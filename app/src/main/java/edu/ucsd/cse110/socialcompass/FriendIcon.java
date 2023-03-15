package edu.ucsd.cse110.socialcompass;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class FriendIcon {
    private float bearingAngle;
    private final Activity activity;
    static int radius;// = 495; //some hard-coded value
    private String userName;
    private TextView username_icon;
    private int id;
    boolean isWithinRange;

    double distance;   //in miles
    // Constructor; passes in the activity you want the button,
    // label of the button, and the corresponding bearing angle
    public FriendIcon(Activity activity, String userName, float bearingAngle, int radius, double distance, boolean isWithinRange) {
        this.activity = activity;

        this.bearingAngle = bearingAngle;
        this.userName = userName;
        this.isWithinRange = isWithinRange;
        this.radius = radius;
        this.distance = distance;

    }

    public TextView getFriendIcon() {
        return username_icon;
    }



    public void updateAngle(float bearingAngle) {
        this.bearingAngle = bearingAngle;
    }

    public float getBearingAngle() {
        return bearingAngle;
    }

    public String getUsername() { return this.userName;}

    public int getRadius() { return this.radius; }

    public int getId() { return this.id;}

//
//    Creates a new button in the activity and sets ints constraints
    public void createIcon() {
        username_icon = new TextView(activity);
        username_icon.setId(View.generateViewId());

        ConstraintLayout.LayoutParams layout;
        if (isWithinRange){
            username_icon.setText(userName);
            username_icon.setGravity(Gravity.CENTER);
            username_icon.setTextColor(Color.BLACK);
            username_icon.setTextSize(20);
            username_icon.setTypeface(Typeface.DEFAULT_BOLD);
            layout = new ConstraintLayout.LayoutParams(
                    200, 200
            );
        } else{
            username_icon.setBackground(ContextCompat.getDrawable(activity, R.drawable.dot));
            layout = new ConstraintLayout.LayoutParams(
                    45, 45
            );
        }

        // Add null checks to avoid NullPointerException
        View outerCircle = activity.findViewById(R.id.second_circle);
        View innerCircle = activity.findViewById(R.id.first_circle);
        if (outerCircle != null && innerCircle != null) {
            float outerCircleRadius = (float) outerCircle.getHeight() / 2;
            float innerCircleRadius = (float) innerCircle.getHeight() / 2;

            layout.circleRadius = Math.round(radius);
            layout.circleConstraint = R.id.location_icon;
            layout.circleAngle = bearingAngle;
            layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            username_icon.setLayoutParams(layout);
            id = username_icon.getId();

        } else {
            Toast.makeText(activity, "Error creating button: outer_circle or inner_circle not found", Toast.LENGTH_SHORT).show();
        }
    }

}

