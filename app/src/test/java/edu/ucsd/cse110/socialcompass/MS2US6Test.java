package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.TextView;

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
 * Tests for detecting last active time for GPS indicator
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US6Test {
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);

    }

    @After
    public void teardown() {

    }

    @Test
    public void testOfflineTime() {

        scenario.onActivity(activity -> {

            // time in seconds
            long time0 = 0; // 0 seconds = LIVE
            long time1 = 20; // 20 seconds
            long time2 = 360; // 6 minutes
            long time3 = 7200; // 2 hours
            final String[] status = new String[4];

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activity.setInactiveTimeText(time0);
                            TextView gps_status = (TextView) activity.findViewById(R.id.gps_status);
                            status[0] = gps_status.getText().toString();

                            activity.setInactiveTimeText(time1);
                            gps_status = (TextView) activity.findViewById(R.id.gps_status);
                            status[1] = gps_status.getText().toString();

                            activity.setInactiveTimeText(time2);
                            gps_status = (TextView) activity.findViewById(R.id.gps_status);
                            status[2] = gps_status.getText().toString();

                            activity.setInactiveTimeText(time3);
                            gps_status = (TextView) activity.findViewById(R.id.gps_status);
                            status[3] = gps_status.getText().toString();

                        }
                    }
            );
            assertEquals("LIVE", status[0]);
            assertEquals("<1m ago", status[1]);
            assertEquals("6m ago", status[2]);
            assertEquals("2h ago", status[3]);
        });
    }
}