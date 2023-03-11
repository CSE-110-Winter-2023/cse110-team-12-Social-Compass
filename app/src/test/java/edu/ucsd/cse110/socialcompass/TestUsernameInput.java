package edu.ucsd.cse110.socialcompass;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;

import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

/**
 * Tests for Milestone 2, Stories 2 and 3
 */
@RunWith(RobolectricTestRunner.class)
public class TestUsernameInput {
    FriendDatabase db;
    private FriendDao dao;
    ActivityScenario<MainActivity> scenario;
    FriendViewModel friendViewModel;
    FriendRepository friendRepository;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
        friendViewModel = new FriendViewModel(getApplicationContext());
        this.friendRepository = new FriendRepository(dao);
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void testValidUsernameIDAndCopy() {
        scenario.onActivity(activity -> {
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);

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
            Utilities.showUserNamePromptAlert((MainActivity) activity, "Please enter your name", db);

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
//            var user = new MediatorLiveData<Friend>();
//            Friend friend = new Friend(name, uid, -1);
//            user.postValue(friend);
//
//            Observer<Friend> obs = actualUser -> {
//                var localUser = user.getValue();
//                if (actualUser == null) return; // do nothing
//                if (localUser == null) {
//                    dao.upsert(actualUser);
//                }
//            };
//            user.addSource(dao.get(uid), user::postValue);
//            db.noti

            Friend friend = new Friend(name, uid, -1);
            dao.upsert(friend);

            assertEquals(friend.uid, dao.getAll().getValue().size());
            //dao.get(uid).equals()
            // check that the user is stored in the database
            //assertEquals("Sam", dao.get(uid).name);
            //assertEquals(Utilities.getUID(), dao.get(uid).getValue().uid);
            //assertEquals(-1, dao.get(uid).getValue().order);
        });
    }

    @Test
    public void testCopyUid() {
        scenario.onActivity(activity -> {

            String uniqueID = UUID.randomUUID().toString();

            //Open the alert console with a given uid and make sure the console has popped up
            Utilities.showCopyUIDAlert(activity, "Copy UID", uniqueID);
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // find the copy button and simulate a click
            TextView copyButton = alertDialog.findViewById(R.id.copy);
            copyButton.performClick();

            //Check if the data in the clipboard matches the uid
            final ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            manager.addPrimaryClipChangedListener(() -> assertEquals(manager.getPrimaryClip().toString(), uniqueID));
        });
    }
}
