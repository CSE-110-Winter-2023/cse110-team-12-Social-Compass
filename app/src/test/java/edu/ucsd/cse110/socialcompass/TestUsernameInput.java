package edu.ucsd.cse110.socialcompass;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.UUID;

/**
 * Tests for Milestone 2, Stories 2 and 3
 */
@RunWith(RobolectricTestRunner.class)
public class TestUsernameInput {
    FriendDatabase db;
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        FriendDatabase.useTestSingleton(getApplicationContext());
        db = FriendDatabase.getSingleton(getApplicationContext());
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void testValidUsernameIDAndCopy() {
        scenario.onActivity(activity -> {
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);

            // make sure that the alert has popped up
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // check that the UID is created
            assertNotNull(Utilities.getUID());
        });
    }

    @Test
    public void testStoredUsernameAndUID() {
        scenario.onActivity(activity -> {
            // create the database and call the Alert Dialog
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);

            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // set the username
            EditText username = alertDialog.findViewById(R.id.inputName);
            String name = "Sam";
            username.setText(name);

            // check that the name is set and that the alert is clicked
            assertEquals("Sam", username.getText().toString());
            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());

            FriendListItem user = new FriendListItem(name, Utilities.getUID(), -1);
            db.friendListItemDao().insert(user);


            assertEquals(1, db.friendListItemDao().getAll().size());

            // check that the user is stored in the database
            assertEquals("Sam", db.friendListItemDao().getAll().get(0).name);
            assertEquals(Utilities.getUID(), db.friendListItemDao().getAll().get(0).uid);
            assertEquals(-1, db.friendListItemDao().getAll().get(0).order);
        });
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
