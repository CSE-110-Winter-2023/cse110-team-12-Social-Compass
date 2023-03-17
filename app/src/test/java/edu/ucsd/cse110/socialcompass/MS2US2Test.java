package edu.ucsd.cse110.socialcompass;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertNotNull;

import android.app.AlertDialog;
import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

/**
 * Tests for inputting and saving user name, showing up properly on other friend's end
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
    public void testValidUsernameIDInput() {
        scenario.onActivity(activity -> {
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");

            // make sure that the alert has popped up
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // check that the UID is created
            assertNotNull(Utilities.getUID());
        });
    }
}
