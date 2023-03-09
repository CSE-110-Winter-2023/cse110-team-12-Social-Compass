package edu.ucsd.cse110.socialcompass;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;


@RunWith(AndroidJUnit4.class)
public class FriendListActivityTest {

    private FriendListItemDao dao;
    private FriendDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.friendListItemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testPromptUserForUid() {
        // Launch activity
        ActivityScenario<FriendListActivity> scenario = ActivityScenario.launch(FriendListActivity.class);

        // Set UID during activity
        scenario.onActivity(activity -> {
            String uid = "testUid";
            activity.uidEditText.setText(uid);
            assertEquals(uid, activity.uidEditText.getText().toString());
        });

        scenario.close();
    }

    @Test
    public void testInsertFriendListItem() {
        // Create a test item
        String uid = "testUid";
        FriendListItem testItem = new FriendListItem("testName", uid, 0);

        dao.insert(testItem);

        List<FriendListItem> items = dao.getAll();

        assertEquals(1, items.size());
        assertEquals(uid, items.get(0).getUid());
    }

    @Test
    public void testShowUserPromptAlert() {
        ActivityScenario<FriendListActivity> scenario = ActivityScenario.launch(FriendListActivity.class);

        // Show user prompt alert
        scenario.onActivity(activity -> {
            //Making sure dialog appears
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            //Setting name in dialog
            EditText username = alertDialog.findViewById(R.id.inputName);
            username.setText("testName");
            assertEquals("testName", username.getText().toString());

            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());

            List<FriendListItem> items = dao.getAll();
            assertEquals(1, items.size());
        });

        scenario.close();
    }
}
