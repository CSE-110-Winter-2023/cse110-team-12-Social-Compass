package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.UUID;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;

/**
 * Tests for detecting last active time for GPS indicator
 */
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
    public void testCopyUid() {
        scenario.onActivity(activity -> {

            String uniqueID = UUID.randomUUID().toString();

            //Open the alert console with a given uid and make sure the console has popped up
            Utilities.showCopyUIDAlert(activity, "Copy UID", uniqueID);
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // find the copy button and simulate a click
            TextView copyButton = alertDialog.findViewById(R.id.copy);
            copyButton.performClick();

            //Check if the data in the clipboard matches the uid
            final ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            manager.addPrimaryClipChangedListener(() -> assertEquals(manager.getPrimaryClip().toString(), uniqueID));
        });
    }

}
