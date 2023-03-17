package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.location.Location;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;


/**
 * Tests for detecting orientation
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US4Test {
    private MainActivity mainActivity;

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
    );

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

        //Initializing Friends
        double melissaLatitude = 32.8805;
        double melissaLongitude = -117.2335;
        Friend melissa = new Friend("Melissa", "123", melissaLatitude, melissaLongitude, 1);

        double melissa_juniorLatitude = 32.8805;
        double melissa_juniorLongitude = -117.2335;
        Friend melissa_junior = new Friend("Melissa Junior", "123", melissa_juniorLatitude, melissa_juniorLongitude, 1);

        // Calculating expected bearing between me and Melissa
        float expectedBearing = Bearing.bearing(myLocation.getLatitude(), myLocation.getLongitude(), melissaLatitude, melissaLongitude);

        // Updating the bearing angle for Melissa in the app
        mainActivity.updateFriendOrientation(myLocation, melissa);
        mainActivity.updateFriendOrientation(myLocation, melissa_junior);


        assertEquals(expectedBearing, melissa.getBearingAngle(), 0.01);
        assertEquals(expectedBearing, melissa_junior.getBearingAngle(), 0.01);
    }

    //Test for checking orientation of friends when rotating phone
    @Test
    public void testFriendOrientationRotation() {

        // Set up the MainActivity
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).setup().get();

        // Set up the test location data
        Location myLocation = new Location("");
        myLocation.setLatitude(32.8801);
        myLocation.setLongitude(-117.2340);

        // Set up the Friend object
        double melissaLatitude = 32.8805;
        double melissaLongitude = -117.2335;

        Friend melissa = new Friend("Melissa", "123", melissaLatitude, melissaLongitude, 1);

        double joeLatitude = -32;
        double joeLongitude = -120;
        Friend joe = new Friend("Joe", "321", joeLatitude, joeLongitude, 1);

        // Get the initial bearing angle
        float initialBearingMelissa = Bearing.bearing(myLocation.getLatitude(), myLocation.getLongitude(), melissaLatitude, melissaLongitude);
        float initialBearingJoe = Bearing.bearing(myLocation.getLatitude(), myLocation.getLongitude(), joeLatitude, joeLongitude);

        // Simulate phone rotation
        float newAzimuth = 45.0f;
        simulateSensorEvent(activity, newAzimuth);

        // Update friend orientation after the phone rotation
        activity.updateFriendOrientation(myLocation, melissa);
        activity.updateFriendOrientation(myLocation, joe);

        // Calculate the expected bearing angle after the phone rotation
        float expectedBearingMelissa = (((initialBearingMelissa - newAzimuth) % 360) + 360) % 360;
        float expectedBearingJoe = (((initialBearingJoe - newAzimuth) % 360) + 360) % 360;


        // Verify that the updated bearing angle is correct
        assertEquals(expectedBearingMelissa, melissa.getBearingAngle(), 0.01);
        assertEquals(expectedBearingJoe, joe.getBearingAngle(), 0.01);
    }

    private void simulateSensorEvent(MainActivity activity, float azimuth) {
        activity.setCurrentAzimuth(azimuth);
    }
}