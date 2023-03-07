package edu.ucsd.cse110.socialcompass;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.io.IOException;
import java.util.List;

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
        scenario.close();
    }

    @Test
    public void testValidUsernameID() {
        scenario.onActivity(activity -> {
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);

            // make sure that the alert has popped up
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // check that the UID is created
            assertNotNull(Utilities.getUID());
        });
    }

//    private FriendDatabase db;
//    private FriendListItemDao dao;
//    private ActivityScenario<MainActivity> scenario;
//    private Intent intent;
//    private Context context;
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Before
//    public void setup() throws Exception {
//        this.context = ApplicationProvider.getApplicationContext();
//        FriendDatabase.useTestSingleton(context);
//        db = FriendDatabase.getSingleton(context);
//        dao = db.friendListItemDao();
//    }
//
//    @After
//    public void cleanup() throws IOException {
//        db.close();
//    }
//
//    @Test
//    public void test_username_uid() {
//
//        //clear();
//        //var scenario = ActivityScenario.launch(MainActivity.class);
////        ActivityScenario scenario = rule.getScenario();
////        scenario.moveToState(Lifecycle.State.CREATED);
////        scenario.moveToState(Lifecycle.State.STARTED);
////        //scenario.moveToState(Lifecycle.State.CREATED);
////        scenario.onActivity(activity -> {
////            // create the database and call the Alert Dialog
////            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);
////
////            // make sure that the alert has popped up
////            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
////            assertNotNull(alertDialog);
////
////            // check that the UID is created
////            assertNotNull(Utilities.getUID());
////        });
//        /*FriendListItemDao testSession = new FriendListItemDao();
//        sessionDao.insert(testSession);
//
//        assertEquals(1, sessionDao.getAll().size());
//        assertNotEquals(null, sessionDao.getSession("testSession"));
//        assertEquals(null, sessionDao.getSession("notSession"));*/
//        assertEquals(1, 1);
//    }
//
//    @Test
//    public void test_username_and_uid_stored() {
//        //clear();
//        //var scenario = ActivityScenario.launch(MainActivity.class);
//
//        ActivityScenario scenario = rule.getScenario();
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        intent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
//        if (scenario == null || !scenario.getState().isAtLeast(Lifecycle.State.CREATED)) {
//            scenario = ActivityScenario.launch(intent); // this is an error
//        } else {
//            scenario.recreate();
//        }
//        //scenario.moveToState(Lifecycle.State.CREATED);
//        //scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.onActivity(activity -> {
//            // create the database and call the Alert Dialog
//            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            assertNotNull(alertDialog);
//
//            // set the username
//            EditText username = alertDialog.findViewById(R.id.inputName);
//            username.setText("Sam");
//
//            assertEquals("Sam", username.getText().toString());
//
//            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());
//
//            // hard coded from button positive
//            String name = username.getText().toString();
//            FriendListItem user = new FriendListItem(name, Utilities.getUID(), -1);
//            db.friendListItemDao().insert(user);
//
//
//            //var dao = db.friendListItemDao();
//            List<FriendListItem> users = dao.getAll();
//            assertEquals(1, users.size());
//
//            // check that the user is stored in the database
//            user = users.get(0);
//            assertEquals("Sam", user.name);
//            assertEquals(Utilities.getUID(), user.uid);
//            assertEquals(-1, user.order);
//        });
}
