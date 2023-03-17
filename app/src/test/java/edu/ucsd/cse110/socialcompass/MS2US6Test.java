//package edu.ucsd.cse110.socialcompass;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import android.app.AlertDialog;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.location.LocationManager;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.room.Room;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.robolectric.shadows.ShadowAlertDialog;
//
//import static org.mockito.Mockito.when;
//
//import edu.ucsd.cse110.socialcompass.activity.MainActivity;
//import edu.ucsd.cse110.socialcompass.model.Friend;
//import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
//
///**
// * Tests for detecting last active time for GPS indicator
// */
//@RunWith(AndroidJUnit4.class)
//public class MS2US6Test {
//    FriendDatabase db;
//    ActivityScenario<MainActivity> scenario;
//    Context context;
//
//    @Before
//    public void init() {
//        scenario = ActivityScenario.launch(MainActivity.class);
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        this.context = ApplicationProvider.getApplicationContext();
//    }
//
//    @After
//    public void teardown() {
//        db.close();
//    }
//
//    @Test
//    public void testDisplayGPSWhenLive() {
//        scenario.onActivity(activity -> {
//            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//
//            // set the username
//            EditText username = alertDialog.findViewById(R.id.inputName);
//            String name = "Sam";
//            username.setText(name);
//
//            LocationManager mockLocationManager = Mockito.mock(LocationManager.class);
//            //Mockito.when(context.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
//            //Mockito.when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
//        });
//    }
//
//    @Test
//    public void testDisplayGPSShortInactive() {
//        scenario.onActivity(activity -> {
//            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//
//            // set the username
//            EditText username = alertDialog.findViewById(R.id.inputName);
//            String name = "Sam";
//            username.setText(name);
//
//            // check that the name is set and that the alert is clicked
//            assertEquals("Sam", username.getText().toString());
//            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());
//
//
//        });
//    }
//
//    @Test
//    public void testDisplayGPSLongInactive() {
//        scenario.onActivity(activity -> {
//            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name");
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//
//            // set the username
//            EditText username = alertDialog.findViewById(R.id.inputName);
//            String name = "Sam";
//            username.setText(name);
//
//            // check that the name is set and that the alert is clicked
//            assertEquals("Sam", username.getText().toString());
//            assertTrue(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick());
//
//
//        });
//    }
//
//}
