package edu.ucsd.cse110.socialcompass;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.UUID;

import edu.ucsd.cse110.socialcompass.activity.FriendListActivity;
import edu.ucsd.cse110.socialcompass.activity.MainActivity;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.services.LocationService;

/**
 * This class stores all the helper methods for showing alerts and dialogs, as well as taking in
 * inputs. Its sole purpose is to handle all user input and display messages accordingly.
 */
public class Utilities {
    static String uniqueID;
    static String uniqueName;

    /**
     * Alert to display an error when adding a UID that does not exist.
     * @param message to display
     */
    public static void showErrorAlert(FriendListActivity activity, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Okay", (dialog, id) -> {
                    dialog.dismiss();
                })
                .setCancelable(false);
        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static void showUserNamePromptAlert(MainActivity activity, String message) {

        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserNameView = inflater.inflate(R.layout.dialog_user_name_prompt, null);

        // get edit texts for user's name
        EditText userName = promptUserNameView.findViewById(R.id.inputName);
        uniqueID = UUID.randomUUID().toString();

        // save user's UID to their shared preferences
        SharedPreferences preferences = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("myUID", uniqueID);
        editor.apply();

        alertBuilder
                .setView(promptUserNameView)
                .setTitle("Username")
                .setMessage(message)
                .setPositiveButton("Submit", (dialog, id) -> {
                    String name = userName.getText().toString();
                    uniqueName = name;

                    //save name to user's shared preferences
                    //add a new "Friend" for self in onCreate of FriendListActivity
                    editor.putString("myName", name);
                    editor.apply();

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
        View promptUserNameView2 = inflater2.inflate(R.layout.dialog_init_friend_prompt, null);

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
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showInitFriendAlert(activity, "Make Friend");
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

    public static String getUID(){
        return uniqueID;
    }

    public static String formatLocation(double latitude, double longitude) {
        return String.format(Locale.US, "%.0f° %.0f' %.0f\" N, %.0f° %.0f' %.0f\" W",
                Math.abs(latitude), Math.abs(latitude % 1) * 60, Math.abs(latitude % 1 % 1) * 60,
                Math.abs(longitude), Math.abs(longitude % 1) * 60, Math.abs(longitude % 1 % 1) * 60);
    }

    public static String getName() {
        return uniqueName;
    }

    public static double recalculateDistance(double userLat, double userLong, double friendLat, double friendLong) {
        float[] results = new float[2];
        Location.distanceBetween(userLat, userLong,
                friendLat, friendLong, results);
        return LocationService.metersToMiles(results[0]);
    }

    // used to calculate which zone the friends lie in, your start value will be the inner zone,
    // ex: if you want to calculate which zone in between zone 1 and zone 2, start will be 1,
    // number will be the distance
    public static double roundToLowestMultiple(double start, double number, double multiple) {
        double result =  Math.floor(number / multiple) * multiple;
        return start + Math.round(result * 100.0) / 100.0;
    }

    // used to return the zone, haven't implemented zone 3 and 4 since we don't need it for story 5
    public static int getFriendZone(double distance){
        if(distance >= Constants.ZONE0 & distance < Constants.ZONE1){
            return (Constants.HASHMAPZONE1).get(Utilities.roundToLowestMultiple(0,distance,0.2));
        } else if(distance >= Constants.ZONE1 & distance < Constants.ZONE2){
            return (Constants.HASHMAPZONE2).get(Utilities.roundToLowestMultiple(1.0,distance,1.8));
        }else if(distance >= Constants.ZONE2 & distance < Constants.ZONE3){
            return 535;
        }else {
            return 535;
        }
    }
}