package edu.ucsd.cse110.socialcompass;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import edu.ucsd.cse110.cse110_project.R;

public class Dynamic_Button {
    private float bearingAngle;
    private final Activity activity;
    static final int radius = 495; //some hard-coded value
    private final String label;
    private TextView button;
    private int id;

    // Constructor; passes in the activity you want the button,
    // label of the button, and the corresponding bearing angle
    public Dynamic_Button(Activity activity, String label, float bearingAngle) {
        this.activity = activity;
        this.bearingAngle = bearingAngle;
        this.label = label;
    }

    // Updates the button object's bearing angle
    public void updateAngle(float bearingAngle) {
        this.bearingAngle = bearingAngle;
    }

    public float getBearingAngle() {
        return bearingAngle;
    }

    public TextView getButton() {
        return button;
    }

    // Creates a new button in the activity and sets ints constraints
    public void createButton() {
        button = new Button(activity);
        button.setId(View.generateViewId());
        button.setBackground(ContextCompat.getDrawable(activity, R.drawable.house_icon));
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                150, 150
        );
        float outerCircleRadius = (float) activity.findViewById(R.id.outer_circle).getHeight() / 2;
        float innerCircleRadius = (float) activity.findViewById(R.id.inner_circle).getHeight() / 2;
        float dynamicRadius = ((outerCircleRadius - innerCircleRadius) / 2) + innerCircleRadius;

        layout.circleRadius = Math.round(dynamicRadius);
        layout.circleConstraint = R.id.location_icon;
        layout.circleAngle = bearingAngle;
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        button.setLayoutParams(layout);
        id = button.getId();

        // Add OnClickListener to the button
        button.setOnClickListener(view -> LabelWindow.showLabel(activity, label));
    }

    // Unused for now
//    public void updateButtonLayout() {
//        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) button.getLayoutParams();
//        layout.circleAngle = bearingAngle;
//        button.setLayoutParams(layout);
//    }

    public String getLabel() {
        return this.label;
    }

    public int getId() {
        return this.id;
    }

}
