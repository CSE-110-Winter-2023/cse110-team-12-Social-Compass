//package edu.ucsd.cse110.socialcompass;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import android.app.AlertDialog;
//import android.content.Context;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Room;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.shadows.ShadowAlertDialog;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
//import edu.ucsd.cse110.socialcompass.activity.MainActivity;
//import edu.ucsd.cse110.socialcompass.model.Friend;
//import edu.ucsd.cse110.socialcompass.model.FriendDao;
//import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
//import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
//
///**
// * Tests for adding UIDs saving users to the database
// */
//@RunWith(AndroidJUnit4.class)
//public class MS2US1Test {
//    private FriendDao dao;
//    private FriendDatabase db;
//    ActivityScenario<MainActivity> scenario;
//    private FriendListViewModel friendListViewModel;
//
//    @Before
//    public void createDb() {
//        scenario = ActivityScenario.launch(MainActivity.class);
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.getDao();
//        friendListViewModel = new FriendListViewModel(ApplicationProvider.getApplicationContext());
//    }
//
//    @After
//    public void closeDb() {
//        db.close();
//    }
//
//}
