package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.Manifest;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;

/**
 * Tests for detecting distance between user and friends
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US5Test {
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
    public void testGetCurrentLocationAndOtherUsersLocation() {
        // Get current user location
        Location myLocation = new Location("");
        myLocation.setLatitude(32.8801);
        myLocation.setLongitude(-117.2340);

        // Initializing Friends
        double melissaLatitude = 32.8805;
        double melissaLongitude = -117.2335;
        Friend melissa = new Friend("Melissa", "123", melissaLatitude, melissaLongitude, 1);

        double johnLatitude = 32.8810;
        double johnLongitude = -117.2350;
        Friend john = new Friend("John", "124", johnLatitude, johnLongitude, 1);

        assertEquals(melissaLatitude, melissa.getLatitude(), 0.0001);
        assertEquals(melissaLongitude, melissa.getLongitude(), 0.0001);

        assertEquals(johnLatitude, john.getLatitude(), 0.0001);
        assertEquals(johnLongitude, john.getLongitude(), 0.0001);
        //assertNotNull(myLocation);
    }

    @Test
    public void testCalculateDistanceBetweenLocations() {
        // Getting current location
        Location myLocation = new Location("");
        myLocation.setLatitude(32.8801);
        myLocation.setLongitude(-117.2340);

        // Initializing Friends
        double melissaLatitude = 32.8805;
        double melissaLongitude = -117.2335;
        Location melissaLocation = new Location("");
        melissaLocation.setLatitude(melissaLatitude);
        melissaLocation.setLongitude(melissaLongitude);

        double johnLatitude = 32.8810;
        double johnLongitude = -117.2350;
        Location johnLocation = new Location("");
        johnLocation.setLatitude(johnLatitude);
        johnLocation.setLongitude(johnLongitude);

        float distanceToMelissa = mainActivity.calculateDistance(myLocation, melissaLocation);
        float distanceToJohn = mainActivity.calculateDistance(myLocation, johnLocation);

        assertEquals(myLocation.distanceTo(melissaLocation), distanceToMelissa, 0.1);
        assertEquals(myLocation.distanceTo(johnLocation), distanceToJohn, 0.1);
    }


//    @Test
//    public void testIconBasedOnDistance() {
//        // Getting current location
//        Location myLocation = new Location("");
//        myLocation.setLatitude(32.8801);
//        myLocation.setLongitude(-117.2340);
//
//        // Initializing Friends
//        double melissaLatitude = 32.8805;
//        double melissaLongitude = -117.2335;
//        Friend melissa = new Friend("Melissa", "123", melissaLatitude, melissaLongitude, 1);
//
//        // Calculate distance between the two locations
//        float distance = mainActivity.calculateDistance(myLocation, melissa.getLocation());
//
//        // Get the icon for Melissa based on the distance
//        //String icon = mainActivity.getFriendIconBasedOnDistance(distance);
//
//        // Check if the icon is correct based on the distance
//        if (distance <= SOME_THRESHOLD) {
//            assertEquals("dot_icon", icon);
//        } else {
//            assertEquals("name_icon", icon);
//        }
//    }

}
