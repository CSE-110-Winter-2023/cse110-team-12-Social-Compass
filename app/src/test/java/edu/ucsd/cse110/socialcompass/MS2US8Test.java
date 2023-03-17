package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;

/**
 * Tests for zooming in and out functionality
 */
public class MS2US8Test {

    FriendDatabase db;
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void teardown() {
        db.close();
    }

    /**
     * Test that a friend's icon is correctly displayed as either a TextView or a Dot
     * depending on where the friend is and what zoom level the user's compass is at.
     */
    @Test
    public void testFriendIconDisplay() {
        var friendName = "John Doe";
        float bearingAngle = 0;
        double distance = 15;

        int radius1 = 100; // In Zone 1
        int radius2 = 300; // In Zone 2
        int radius3 = 350; // In Zone 3
        int radius4 = 390; // In Zone 4
        int radius5 = 495; // Further than Zone 4 (Always a dot)

        scenario.onActivity(activity -> {
            // create a new friendIcon for each zone
            FriendIcon friendIcon1 = new FriendIcon(activity,
                    friendName, bearingAngle, radius1, distance, true);
            FriendIcon friendIcon2 = new FriendIcon(activity,
                    friendName, bearingAngle, radius2, distance, true);
            FriendIcon friendIcon3 = new FriendIcon(activity,
                    friendName, bearingAngle, radius3, distance, true);
            FriendIcon friendIcon4 = new FriendIcon(activity,
                    friendName, bearingAngle, radius4, distance, true);
            FriendIcon friendIcon5 = new FriendIcon(activity,
                    friendName, bearingAngle, radius5, distance, true);

            SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            int scale = preferences.getInt("scaleOfCircles", 0);
            assertEquals(scale, 300); // test default startup shows 2 zones

            // get the icons for each of the friends
            var icon1 = friendIcon1.getFriendIcon();
            var icon2 = friendIcon2.getFriendIcon();
            var icon3 = friendIcon3.getFriendIcon();
            var icon4 = friendIcon4.getFriendIcon();
            var icon5 = friendIcon5.getFriendIcon();
            // assert that friends 1-2 appear as text, and friends 3-5 are dots
            assertEquals(friendName, icon1.getText());
            assertEquals(friendName, icon2.getText());
            assertEquals("", icon3.getText());
            assertEquals("", icon4.getText());
            assertEquals("", icon5.getText());
        });
    }
}
