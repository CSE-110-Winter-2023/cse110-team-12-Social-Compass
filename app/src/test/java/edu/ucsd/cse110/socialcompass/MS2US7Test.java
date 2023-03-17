//package edu.ucsd.cse110.socialcompass;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.widget.EditText;
//
//import androidx.room.Room;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.robolectric.shadows.ShadowAlertDialog;
//
//import edu.ucsd.cse110.socialcompass.activity.MainActivity;
//import edu.ucsd.cse110.socialcompass.model.Friend;
//import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
//
///**
// * Tests for color icons for GPS activity indicator
// */
//public class MS2US7Test {
//    FriendDatabase db;
//    ActivityScenario<MainActivity> scenario;
//
//    @Before
//    public void init() {
//        scenario = ActivityScenario.launch(MainActivity.class);
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//    }
//
//    @After
//    public void teardown() {
//        db.close();
//    }
//
//    @Test
//    public void testIconColorLive() {
//
//    }
//
//    @Test
//    public void testIconColorInactive() {
//
//    }
//}
