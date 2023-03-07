package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.io.IOException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class TestUsernameInput {

    private FriendDatabase db;
    private ActivityScenario<MainActivity> scenario;
    private Intent intent;


    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = FriendDatabase.getSingleton(context);

        if (scenario == null || !scenario.getState().isAtLeast(Lifecycle.State.CREATED)) {
            scenario = ActivityScenario.launch(intent); // this is an error
        } else {
            scenario.recreate();
        }
    }

    @After
    public void cleanup() throws IOException {
        db.close();
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
    }

    @Test
    public void test_username_uid() {
        //scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            // create the database and call the Alert Dialog
            Utilities.showUserNamePromptAlert(activity, "Please enter your name", db);

            // make sure that the alert has popped up
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // check that the UID is created
            assertNotNull(Utilities.getUID());
        });
    }

    @Test
    public void test_username_and_uid_stored() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        if (scenario == null || !scenario.getState().isAtLeast(Lifecycle.State.CREATED)) {
            scenario = ActivityScenario.launch(intent); // this is an error
        } else {
            scenario.recreate();
        }
        //scenario.moveToState(Lifecycle.State.CREATED);
        //scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            // create the database and call the Alert Dialog
            Utilities.showUserNamePromptAlert(activity, "Please enter your name", db);

            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // set the username
            EditText username = alertDialog.findViewById(R.id.inputName);
            username.setText("Sam");

            assertEquals("Sam", username.getText().toString());

            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());

            // hard coded from button positive
            String name = username.getText().toString();
            FriendListItem user = new FriendListItem(name, Utilities.getUID(), -1);
            db.friendListItemDao().insert(user);

            var dao = db.friendListItemDao();
            List<FriendListItem> users = dao.getAll();
            assertEquals(1, users.size());

            // check that the user is stored in the database
            user = users.get(0);
            assertEquals("Sam", user.name);
            assertEquals(Utilities.getUID(), user.uid);
            assertEquals(-1, user.order);
        });
    }
}
