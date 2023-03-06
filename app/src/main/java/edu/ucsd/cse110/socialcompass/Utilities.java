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

    /**
     * Alert that shows when the
     * @param activity
     * @param message
     */
    public static void showUserNamePromptAlert(MainActivity activity, String message, FriendDatabase db) {

        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserNameView = inflater.inflate(R.layout.dialog_user_name_prompt, null);
        uniqueID = UUID.randomUUID().toString();
        // get edit texts for user's name
        EditText userName = promptUserNameView.findViewById(R.id.inputName);
        String uniqueID = UUID.randomUUID().toString();
        alertBuilder
                .setView(promptUserNameView)
                .setTitle("Enter Your Name")
                .setMessage(message)
                .setPositiveButton("Submit", (dialog, id) -> {
                    String name = userName.getText().toString();
                    FriendListItem user = new FriendListItem(name,uniqueID,-1);
                    //not sure if this is correct
                    db.friendListItemDao().insert(user);

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

    /**
     * Helper method which parses the coordinate inputs for easier processing
     * @param input coordinates as an input string
     * @return 2-element array for storing the corresponding latitude and longitude
     */
    public static double[] parseCoordinates(String input) {
        input = input.replaceAll("[\\()]", "");

        double[] coordinates = new double[2];
        String[] parts = input.split(", ");

        // check input is split into two parts separated by a comma
        if (parts.length != 2) { return null; }

        double latitude = Double.parseDouble(parts[0]);
        double longitude = Double.parseDouble(parts[1]);
        // check that latitude and longitude are valid
        if ( latitude < -90 || latitude > 90) { return null; }
        if ( longitude < -180 || longitude > 180) { return null; }

        coordinates[0] = latitude;
        coordinates[1] = longitude;
        return coordinates;
    }
}
