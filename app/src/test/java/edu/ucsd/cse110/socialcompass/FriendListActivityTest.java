package edu.ucsd.cse110.socialcompass;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FriendListActivityTest {
    private FriendDatabase db;
    private FriendListItemDao dao;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                FriendDatabase.class).build();
        dao = db.friendListItemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertFriendListItem() throws Exception {
        FriendListActivity activity = new FriendListActivity();
        activity.db = db;
        activity.dao = dao;

        String testUid = "1234";
        activity.insertFriendListItem(testUid);

        FriendListItem returnedItem = dao.getFriendListItem(testUid);
        assertEquals(testUid, returnedItem.getUid());
    }
}
