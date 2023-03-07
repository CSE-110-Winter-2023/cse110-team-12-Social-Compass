package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class TestShareUID {
    @Test
    public void test_copy_button_copies_UID(){
        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            // Create UID and call the AlertDialog that contains the copy button
            String uniqueID = UUID.randomUUID().toString();
            Utilities.showCopyUIDAlert(activity, "Copy UID", uniqueID);

            // make sure that the alert has popped up
            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull(alertDialog);

            // find the copy button and simulate a click
            TextView copyButton = alertDialog.findViewById(R.id.copy);
            copyButton.performClick();


            ClipboardManager clipboard = (ClipboardManager) activity.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            assertTrue(clipboard.hasPrimaryClip());

            // Get the ClipData from the clipboard
            ClipData clipData = clipboard.getPrimaryClip();

            // Get the first item from the ClipData
            ClipData.Item item = clipData.getItemAt(0);

            // Get the text from the ClipData item
            String text = item.getText().toString();

            // check that the text on the clipboard is the same as the UID generated for this test
            assertEquals(uniqueID, text);
        });
    }

}
