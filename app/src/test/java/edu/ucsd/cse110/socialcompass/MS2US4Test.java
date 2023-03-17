package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.location.Location;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import edu.ucsd.cse110.socialcompass.Bearing;
import edu.ucsd.cse110.socialcompass.Utilities;
import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;


/**
 * Tests for detecting orientation
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US4Test {
    private MainActivity mainActivity;
    private FriendListViewModel friendListViewModel;

    @Before
    public void setUp() {
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class).create();
        ShadowApplication shadowApplication = Shadows.shadowOf(activityController.get().getApplication());
        shadowApplication.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        activityController.resume();
        mainActivity = activityController.get();
    }
    @Test
    public void testFriendOrientation() {
        // Getting current location
        Location myLocation = new Location("");
        myLocation.setLatitude(32.8801);
        myLocation.setLongitude(-117.2340);

        //Initializing Friend Melissa
        double melissaLatitude = 32.8805;
        double melissaLongitude = -117.2335;
        Friend melissa = new Friend("Melissa", "123", melissaLatitude, melissaLongitude, 1);

        // Calculating expected bearing between me and Melissa
        float expectedBearing = Bearing.bearing(myLocation.getLatitude(), myLocation.getLongitude(), melissaLatitude, melissaLongitude);

        // Updating the bearing angle for Melissa in the app
        mainActivity.updateFriendOrientation(myLocation, melissa);

        assertEquals(expectedBearing, melissa.getBearingAngle(), 0.01);
    }

}
