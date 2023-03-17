package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.ImageView;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;

/**
 * Tests for color icons for GPS activity indicator
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US7Test {

    FriendDatabase db;
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void teardown() {

    }

    @Test
    public void testOfflineGPS() {
        scenario.onActivity(activity -> {
            long time = 600;
            final int[] btnVisibility = new int[1];

            activity.runOnUiThread(() -> {
                activity.setIconVisibility(time);
                ImageView redButton = (ImageView) activity.findViewById(R.id.red_btn);
                btnVisibility[0] = redButton.getVisibility();
            });
            assertEquals(0,btnVisibility[0]);
        });
    }

    @Test
    public void testOnlineGPS() {
        scenario.onActivity(activity -> {
            long time = 0;
            final int[] btnVisibility = new int[1];

            activity.runOnUiThread(() -> {
                activity.setIconVisibility(time);
                ImageView greenButton = (ImageView) activity.findViewById(R.id.green_btn);
                btnVisibility[0] = greenButton.getVisibility();
            });
            assertEquals(0,btnVisibility[0]);
        });
    }
}