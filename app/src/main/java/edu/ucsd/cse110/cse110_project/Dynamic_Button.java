package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class Dynamic_Button {
    private float bearingAngle;
    private final Activity activity;
    static final int radius = 495; //some hard-coded value
    private final String label;
    private TextView button;
    private int id;


    public Dynamic_Button(Activity context, String label, float bearingAngle) {
        this.activity = context;
        this.bearingAngle = bearingAngle;
        this.label = label;
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
        button = new Button(activity);
        button.setId(View.generateViewId());
        button.setBackground(ContextCompat.getDrawable(activity, R.drawable.house_icon));
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
        id = button.getId();

        // Add OnClickListener to the button
        button.setOnClickListener(view -> LabelWindow.showLabel(activity, label));
    }

    public void updateButtonLayout() {
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        layout.circleAngle = bearingAngle;
        button.setLayoutParams(layout);
    }

    public String getLabel() {
        return this.label;
    }

    public int getId() {
        return this.id;
    }

}
