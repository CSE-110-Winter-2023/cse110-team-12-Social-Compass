package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.AlertDialog;
import android.content.Context;

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

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest {
    private FriendDao dao;
    private FriendDatabase db;
    ActivityScenario<MainActivity> scenario;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testImport() {
        Friend item1 = new Friend("Josephina", "253647", 0);
        Friend item2 = new Friend("Jackson", "125690", 1);

        long id1 = dao.upsert(item1);
        long id2 = dao.upsert(item2);

        // Check that these have all been inserted with unique IDs.
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        String uid = "253647";
        Friend insertedItem = new Friend("Josephina", uid, 0);
        long id = dao.upsert(insertedItem);

        Friend item = dao.get(uid).getValue();
        assertEquals(id, item.id);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.uid, item.uid);
        assertEquals(insertedItem.order, item.order);
    }

    @Test
    public void testUpdate() {
        String uid = "253647";
        Friend item = new Friend("Josephina", uid, 0);
        long id = dao.upsert(item);

        item = dao.get(uid).getValue();
        item.name = "Lindsey";
        long itemsUpdated = dao.upsert(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(uid).getValue();
        assertNotNull(item);
        assertEquals("Lindsey", item.name);
    }

    @Test
    public void testDelete() {
        String uid = "253647";
        Friend item = new Friend("Josephina", uid, 0);
        long id = dao.upsert(item);

        item = dao.get(uid).getValue();
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(uid).getValue());
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
}
