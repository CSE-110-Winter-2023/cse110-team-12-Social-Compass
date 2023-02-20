package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class LabelWindowUnitTest {

    @Test
    public void testShowLabel() {
        Activity activity = Robolectric.buildActivity(Activity.class).create().get();

        String message = "Test Message";
        LabelWindow.showLabel(activity, message);

        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();

        assertNotNull(alertDialog);
    }
}