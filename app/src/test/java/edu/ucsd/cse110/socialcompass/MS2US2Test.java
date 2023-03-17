package edu.ucsd.cse110.socialcompass;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

/**
 * Tests for
 */
@RunWith(RobolectricTestRunner.class)
public class MS2US2Test {
    FriendDatabase db;
    private FriendDao dao;
    ActivityScenario<MainActivity> scenario;
    FriendViewModel friendViewModel;
    FriendRepository friendRepository;
    FriendListViewModel friendListViewModel;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
        Context context = getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
        friendViewModel = new FriendViewModel(getApplicationContext());
        this.friendRepository = new FriendRepository(dao);
        this.friendListViewModel = new FriendListViewModel(getApplicationContext());
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void testValidUsernameIDAndCopy() {
        scenario.onActivity(activity -> {
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");

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
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");

            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // set the username
            EditText username = alertDialog.findViewById(R.id.inputName);
            String name = "Sam";
            username.setText(name);

            // check that the name is set and that the alert is clicked
            assertEquals("Sam", username.getText().toString());
            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());

            String uid = Utilities.getUID();

            Friend friend = new Friend(name, uid, 0, 0, -1);
            dao.upsert(friend);

            LiveData<List<Friend>> liveDataFriends = friendListViewModel.getAll();

            CountDownLatch latch = new CountDownLatch(1);
            liveDataFriends.observeForever(friendList -> {
                if (friendList != null) {
                    assertEquals(1, friendList.size());
                    assertEquals("Sam", friendList.get(0).getName());
                    assertEquals(Utilities.getUID(), friendList.get(0).getUid());
                    assertEquals(-1, friendList.get(0).order);
                }
            });
            try {
                latch.await(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
