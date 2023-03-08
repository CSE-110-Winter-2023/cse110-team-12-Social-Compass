package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendListItemDao;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest {
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
    public void testImport() {
        FriendListItem item1 = new FriendListItem("Josephina", "253647", 0);
        FriendListItem item2 = new FriendListItem("Jackson", "125690", 1);

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        // Check that these have all been inserted with unique IDs.
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        FriendListItem insertedItem = new FriendListItem("Josephina", "253647", 0);
        long id = dao.insert(insertedItem);

        FriendListItem item = dao.get(id);
        assertEquals(id, item.id);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.uid, item.uid);
        assertEquals(insertedItem.order, item.order);
    }

    @Test
    public void testUpdate() {
        FriendListItem item = new FriendListItem("Josephina", "253647", 0);
        long id = dao.insert(item);

        item = dao.get(id);
        item.name = "Lindsey";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals("Lindsey", item.name);
    }

    @Test
    public void testDelete() {
        FriendListItem item = new FriendListItem("Josephina", "253647", 0);
        long id = dao.insert(item);

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }
}
