package edu.ucsd.cse110.socialcompass;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;


@RunWith(AndroidJUnit4.class)
public class FriendListActivityTest {
    private FriendListItemDao dao;
    private FriendDatabase db;

    @Before
    public void createDb() {
        var scenario = ActivityScenario.launch(MainActivity.class);
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

    /*@Test
    public void testInsertFriendListItem() {
        String uid = "testUid";
        FriendListActivity activity = new FriendListActivity();
        activity.insertFriendListItem(uid);

        List<FriendListItem> items = dao.getFriendList();
        assertTrue(FriendListActivity.checkInsert());
        //assertEquals(1, items.size());
        //ssertEquals(uid, items.get(0).getUid());
    }*/

    @Test
    public void testInsertFriendListItem() {
        String uid = "testUid";
        FriendListActivity activity = new FriendListActivity();
        activity.dao = dao;
        activity.insertFriendListItem(uid);

        List<FriendListItem> items = dao.getAll();
        assertEquals(1, items.size());
        assertEquals(uid, items.get(0).getUid());
    }
    /*@Test
    public void testInsertFriendListItem() {
        String uid = "testUid";
        FriendListActivity activity = new FriendListActivity();


        List<FriendListItem> items = dao.getAll();
        //assertTrue(FriendListActivity.checkInsert());
        assertEquals(0, items.size());

        activity.insertFriendListItem(uid);
        assertEquals(uid, items.get(0).getUid());
    }*/
}
//@RunWith(AndroidJUnit4.class)
//public class FriendListActivityTest {
//    private FriendDatabase db;
//    private FriendListItemDao dao;
//
//    @Before
//    public void createDb() {
//        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
//                FriendDatabase.class).build();
//        dao = db.friendListItemDao();
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        db.close();
//    }
//
//    @Test
//    public void insertFriendListItem() throws Exception {
//        FriendListActivity activity = new FriendListActivity();
//        activity.db = db;
//        activity.dao = dao;
//
//        String testUid = "1234";
//        activity.insertFriendListItem(testUid);
//
//        FriendListItem returnedItem = dao.getFriendListItem(testUid);
//        assertEquals(testUid, returnedItem.getUid());
//    }
//}
