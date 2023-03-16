package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.AlertDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;

/**
 * Tests for adding UIDs saving users to the database
 */
@RunWith(AndroidJUnit4.class)
public class MS2US1Test {
    private FriendDao dao;
    private FriendDatabase db;
    ActivityScenario<MainActivity> scenario;
    private FriendListViewModel friendListViewModel;

    @Before
    public void createDb() {
        scenario = ActivityScenario.launch(MainActivity.class);
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
        friendListViewModel = new FriendListViewModel(ApplicationProvider.getApplicationContext());
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testValidInput() {

    }

    @Test
    public void testInvalidInput() {

    }



    @Test
    public void testImport() {
        Friend item1 = new Friend("Josephina", "253647", 0, 0, 1);
        Friend item2 = new Friend("Jackson", "125690", 1, 1, 2);

        long id1 = dao.upsert(item1);
        long id2 = dao.upsert(item2);

        // Check that these have all been inserted with unique IDs.
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        String uid = "253647";
        Friend insertedItem = new Friend("Josephina", uid, 0, 0, 1);
        long id = dao.upsert(insertedItem);

        Friend item = dao.get(uid).getValue();
        assertEquals(id, item.id);
        assertEquals(insertedItem.getName(), item.getName());
        assertEquals(insertedItem.getUid(), item.getUid());
        assertEquals(insertedItem.order, item.order);
    }

    @Test
    public void testUpdate() {
        String uid = "253647";
        Friend item = new Friend("Josephina", uid, 0, 0, 1);
        long id = dao.upsert(item);

        item = dao.get(uid).getValue();
        item.setName("Lindsey");
        long itemsUpdated = dao.upsert(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(uid).getValue();
        assertNotNull(item);
        assertEquals("Lindsey", item.getName());
    }

    @Test
    public void testDelete() {
        String uid = "253647";
        Friend item = new Friend("Josephina", uid, 0, 0, 1);
        long id = dao.upsert(item);

        item = dao.get(uid).getValue();
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
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(uid).getValue());
    }

    @Test
    public void testValidUsernameID() {
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
