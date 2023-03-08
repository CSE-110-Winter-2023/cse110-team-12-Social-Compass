package edu.ucsd.cse110.socialcompass;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.text.AllCapsTransformationMethod;

import java.util.HashMap;
import java.util.UUID;

import edu.ucsd.cse110.socialcompass.db.Location;

/**
 * This class stores all the helper methods for showing alerts and dialogs, as well as taking in
 * inputs. Its sole purpose is to handle all user input and display messages accordingly.
 */
public class Utilities {
    static String uniqueID;
    /**
     * Alert that shows when the
     * @param activity
     * @param message
     */

    public static void showUserNamePromptAlert(MainActivity activity, String message, FriendDatabase db) {

        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserNameView = inflater.inflate(R.layout.dialog_user_name_prompt, null);

        // get edit texts for user's name
        EditText userName = promptUserNameView.findViewById(R.id.inputName);
        uniqueID = UUID.randomUUID().toString();
        alertBuilder
                .setView(promptUserNameView)
                .setTitle("Username")
                .setMessage(message)
                .setPositiveButton("Submit", (dialog, id) -> {
                    String name = userName.getText().toString();
                    FriendListItem user = new FriendListItem(name,uniqueID,-1);
                    //not sure if this is correct
                    db.friendListItemDao().insert(user);
                    dialog.cancel();
                    showCopyUIDAlert(activity, "User UID", uniqueID);
                })
                .setCancelable(false);


        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }
    public static void showInitFriendAlert(MainActivity activity, String message)
    {
        android.app.AlertDialog.Builder alertBuilder2 = new android.app.AlertDialog.Builder(activity);

        LayoutInflater inflater2 = LayoutInflater.from(activity);
        //View promptUserNameView2 = inflater2.inflate(R.layout.dialog_user_name_prompt, null);
        View promptUserNameView2 = inflater2.inflate(R.layout.dialog_init_friend_prompt, null);
        //TextView userName = promptUserNameView.findViewById(R.id.inputName);

        alertBuilder2
                .setView(promptUserNameView2)
                .setTitle("You can now enter your friend's UID")
                //.setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismiss the AlertDialog and start FriendListActivity
                        Intent intent = new Intent(activity, FriendListActivity.class);
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false);

        android.app.AlertDialog alertDialog2 = alertBuilder2.create();
        alertDialog2.show();

    }

    public static void showCopyUIDAlert(MainActivity activity, String message, String uid) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View copyUIDView = inflater.inflate(R.layout.copy_uid_prompt, null);

        TextView uidTextView  = copyUIDView.findViewById(R.id.uid);
        uidTextView.setText(uid);

        TextView copyButton = (TextView) copyUIDView.findViewById(R.id.copy);

        // copy UID to clipboard
        copyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View var1) {
                ClipboardManager clipboard = (ClipboardManager) var1.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String label = "uid";
                String text = uidTextView.getText().toString();
                ClipData clip = ClipData.newPlainText(label, text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(var1.getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder
                .setView(copyUIDView)
                .setTitle("Welcome!")
                .setMessage(message)
                //.setPositiveButton("Continue", (dialog, id) -> {
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showInitFriendAlert(activity, "Make FRiend");
                    }
                })
                .setCancelable(false);

        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * First alert that activates when the app is started up the first time
     * @param activity Activity page to which the dialog should appear
     * @param message Message to display when the dialog appears
     */
    private static void showFirstAlertHelper(MainActivity activity, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}