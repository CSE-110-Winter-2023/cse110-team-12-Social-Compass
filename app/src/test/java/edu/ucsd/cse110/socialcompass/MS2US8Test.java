package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import androidx.test.core.app.ActivityScenario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import edu.ucsd.cse110.socialcompass.activity.MainActivity;

/**
 * Tests for zooming in and out
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US8Test {
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void teardown() {
    }

    /**
     * Test if a friend's icon is correctly displayed as either a TextView or a Dot
     * depending on where the friend is and what zoom level the user's compass is at.
     */
    @Test
    public void testFriendIconDisplay() {
        var friendName = "John Doe";
        float bearingAngle = 0;
        double distance = 15;

        int radius1 = 100; // In Zone 1
        int radius2 = 200; // In Zone 2
        int radius3 = 340; // In Zone 3
        int radius4 = 390; // In Zone 4
        int radius5 = 490; // In Zone 5 (A dot)

        scenario.onActivity(activity -> {
            // Create a new friendIcon for each zone
            FriendIcon friendIcon1 = new FriendIcon(activity,
                    friendName, bearingAngle, radius1, distance, radius1 < 490);
            FriendIcon friendIcon2 = new FriendIcon(activity,
                    friendName, bearingAngle, radius2, distance, radius2 < 490);
            FriendIcon friendIcon3 = new FriendIcon(activity,
                    friendName, bearingAngle, radius3, distance, radius3 < 490);
            FriendIcon friendIcon4 = new FriendIcon(activity,
                    friendName, bearingAngle, radius4, distance, radius4 < 490);
            FriendIcon friendIcon5 = new FriendIcon(activity,
                    friendName, bearingAngle, radius5, distance, radius5 < 490);
            friendIcon1.createIcon(false);
            friendIcon2.createIcon(false);
            friendIcon3.createIcon(false);
            friendIcon4.createIcon(false);
            friendIcon5.createIcon(false);

            // Get the icons for each of the friends
            var icon1 = friendIcon1.getFriendIcon();
            var icon2 = friendIcon2.getFriendIcon();
            var icon3 = friendIcon3.getFriendIcon();
            var icon4 = friendIcon4.getFriendIcon();
            var icon5 = friendIcon5.getFriendIcon();
            // Assert that friends 1~4 appear as texts, and friend 5 is a dot
            assertEquals(friendName, icon1.getText());
            assertEquals(friendName, icon2.getText());
            assertEquals(friendName, icon3.getText());
            assertEquals(friendName, icon4.getText());
            assertEquals("", icon5.getText());

            // Zoom in once
            var zoomIn = activity.findViewById(R.id.zoom_in);
            zoomIn.performClick();

            // Update friendIcons
            friendIcon1 = new FriendIcon(activity,
                    friendName, bearingAngle, radius1, distance, radius1 < (490 / 4 * 3));
            friendIcon2 = new FriendIcon(activity,
                    friendName, bearingAngle, radius2, distance, radius2 < (490 / 4 * 3));
            friendIcon3 = new FriendIcon(activity,
                    friendName, bearingAngle, radius3, distance, radius3 < (490 / 4 * 3));
            friendIcon4 = new FriendIcon(activity,
                    friendName, bearingAngle, radius4, distance, radius4 < (490 / 4 * 3));
            friendIcon5 = new FriendIcon(activity,
                    friendName, bearingAngle, radius5, distance, radius5 < (490 / 4 * 3));
            friendIcon1.createIcon(false);
            friendIcon2.createIcon(false);
            friendIcon3.createIcon(false);
            friendIcon4.createIcon(false);
            friendIcon5.createIcon(false);
            // Get the icons for each of the friends
            icon1 = friendIcon1.getFriendIcon();
            icon2 = friendIcon2.getFriendIcon();
            icon3 = friendIcon3.getFriendIcon();
            icon4 = friendIcon4.getFriendIcon();
            icon5 = friendIcon5.getFriendIcon();
            // Assert that friends 1~3 appear as texts, and friend 4~5 are dots
            assertEquals(friendName, icon1.getText());
            assertEquals(friendName, icon2.getText());
            assertEquals(friendName, icon3.getText());
            assertEquals("", icon4.getText());
            assertEquals("", icon5.getText());
        });
    }
}