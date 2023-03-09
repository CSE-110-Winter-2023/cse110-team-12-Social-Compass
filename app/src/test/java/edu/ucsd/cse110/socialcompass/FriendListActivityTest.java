package edu.ucsd.cse110.socialcompass;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;


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

import edu.ucsd.cse110.socialcompass.activity.FriendListActivity;
import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;


//@RunWith(AndroidJUnit4.class)
///**
// * Scenario Tests for Story 1
// */
//public class FriendListActivityTest {
//<<<<<<< HEAD
//    private FriendDao dao;
//=======
//
//    private FriendListItemDao dao;
//>>>>>>> 2d12d414c810ed935f7b761ea34bfbe25809708d
//    private FriendDatabase db;
//    ActivityScenario<MainActivity> mainScenario;
//    ActivityScenario<FriendListActivity> friendListScenario;
//
//    @Before
//    public void init() {
//        Context context = ApplicationProvider.getApplicationContext();
//
//        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.getDao();
//
//        FriendDatabase.useTestSingleton(getApplicationContext());
//
//        mainScenario = ActivityScenario.launch(MainActivity.class);
//        friendListScenario = ActivityScenario.launch(FriendListActivity.class);
//    }
//
//    @After
//    public void closeDb() {
//        db.close();
//    }
//
//    @Test
//    public void testPromptUserForUid() {
//        // Set UID during activity
//        friendListScenario.onActivity(activity -> {
//            String uid = "testUid";
//            activity.uidEditText.setText(uid);
//            assertEquals(uid, activity.uidEditText.getText().toString());
//        });
//
//<<<<<<< HEAD
//        List<FriendListItem> items = dao.getFriendList();
//        assertTrue(FriendListActivity.checkInsert());
//        //assertEquals(1, items.size());
//        //ssertEquals(uid, items.get(0).getUid());
//    }*/
///**
//=======
//    }
//
//>>>>>>> 2d12d414c810ed935f7b761ea34bfbe25809708d
//    @Test
//    public void testInsertFriendListItem() {
//        // Create a test item
//        String uid = "testUid";
//        FriendListItem testItem = new FriendListItem("testName", uid, 0);
//
//        dao.insert(testItem);
//
//        List<FriendListItem> items = dao.getAll();
//
//        assertEquals(1, items.size());
//        assertEquals(uid, items.get(0).getUid());
//    }
//<<<<<<< HEAD
//    */
//    /*@Test
//    public void testInsertFriendListItem() {
//        String uid = "testUid";
//        FriendListActivity activity = new FriendListActivity();
//=======
//>>>>>>> 2d12d414c810ed935f7b761ea34bfbe25809708d
//
//    @Test
//    public void testShowUserPromptAlert() {
//        // Show user prompt alert
//        friendListScenario.onActivity(activity -> {
//            //Making sure dialog appears
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            assertNotNull(alertDialog);
//            assertTrue(alertDialog.isShowing());
//        });
//    }
//
//     */
//}
