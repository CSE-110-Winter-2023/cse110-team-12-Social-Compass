package edu.ucsd.cse110.cse110_project;

import static androidx.core.graphics.MatrixKt.times;
import static com.google.common.base.Verify.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPopupWindow;


@RunWith(RobolectricTestRunner.class)
public class Dynamic_ButtonUnitTest {

    @Test
    public void createButtonNegativeAngle() {

        Activity activity = Robolectric.buildActivity(Activity.class).create().get();

        String label = "Test Label";
        float bearingAngle = -45;
        Dynamic_Button button = new Dynamic_Button(activity, label, bearingAngle);

        button.createButton();


        assertNotNull(button.getButton());

    }

    @Test
    public void createButtonPositiveAngle() {

        Activity activity = Robolectric.buildActivity(Activity.class).create().get();

        String label = "Test Label";
        float bearingAngle = 45;
        Dynamic_Button button = new Dynamic_Button(activity, label, bearingAngle);

        button.createButton();

        assertNotNull(button.getButton());

    }

    @Test
    public void createButtonEmptyString() {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();

        String label = "";
        float bearingAngle = 45;
        Dynamic_Button button = new Dynamic_Button(activity, label, bearingAngle);

        button.createButton();

        assertNotNull(button.getButton());

    }


    @Test
    public void updateAngle_NegativeAngle() {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        String label = "Button Label";
        float bearingAngle = 45f;

        Dynamic_Button dynamicButton = new Dynamic_Button(activity, label, bearingAngle);
        dynamicButton.createButton();


        dynamicButton.updateAngle(-45f);


        assertEquals(bearingAngle, dynamicButton.getBearingAngle(), 0.0);
    }

    // Test case for handling bearing angles greater than 360 degrees
    @Test
    public void updateAngle_GreaterThan360() {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        String label = "Button Label";
        float bearingAngle = 45f;

        Dynamic_Button dynamicButton = new Dynamic_Button(activity, label, bearingAngle);
        dynamicButton.createButton();


        dynamicButton.updateAngle(405f);


        assertEquals(45f, dynamicButton.getBearingAngle(), 0.0);
    }

    // Test case for handling bearing angles equal to 360 degrees
    @Test
    public void updateAngle_EqualTo360() {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        String label = "Button Label";
        float bearingAngle = 45f;

        Dynamic_Button dynamicButton = new Dynamic_Button(activity, label, bearingAngle);
        dynamicButton.createButton();


        dynamicButton.updateAngle(360f);


        assertEquals(0f, dynamicButton.getBearingAngle(), 0.0);
    }


}




