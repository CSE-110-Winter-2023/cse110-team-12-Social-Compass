package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;
import org.w3c.dom.Text;

import java.util.UUID;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;

/**
 * Tests for detecting last active time for GPS indicator
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US6Test {
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