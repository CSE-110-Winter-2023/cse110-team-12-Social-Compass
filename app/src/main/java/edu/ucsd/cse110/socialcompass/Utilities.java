package edu.ucsd.cse110.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;

import edu.ucsd.cse110.socialcompass.db.Location;


public class Utilities {

    private static boolean success = false;
    private static HashMap<String, Pair<Double, Double>> locations = new HashMap<>();
    private static String homeName = "";
    private static String friendName = "";
    private static String parentName = "";



    // Prompts the user with an AlertDialog to input location name and coordinates
    public static void showAlertDialog(LocationListActivity activity, String message) {
        SharedPreferences preferences = activity.getSharedPreferences("mainPrefs", Context.MODE_PRIVATE);
        //SharedPreferences preferences = activity.getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        // Load the dialog layout for prompting the User
        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserView = inflater.inflate(R.layout.dialog_userprompt, null);


        // get edit texts
        EditText homeCoords = promptUserView.findViewById(R.id.inputCoordsHome);

        //String homeInputCoords = homeCoords.getText().toString();

        //double[] homeCoordinates = parseCoordinates(homeInputCoords); // parse the input

        EditText homeLabel = promptUserView.findViewById(R.id.inputLabelHome);

        //String homeLabelInput = homeLabel.getText().toString();

        // get friend info
        EditText friendCoords = promptUserView.findViewById(R.id.inputCoordsFriend);
        //String friendInputCoords = friendCoords.getText().toString();

        //double[] friendCoordinates = parseCoordinates(friendInputCoords);

        EditText friendLabel = promptUserView.findViewById(R.id.inputLabelFriend);
        //String friendLabelInput = friendLabel.getText().toString();

        // get parent info
        EditText parentCoords = promptUserView.findViewById(R.id.inputCoordsParent);
        //String parentInputCoords = parentCoords.getText().toString();

        //double[] parentCoordinates = parseCoordinates(parentInputCoords);

        EditText parentLabel = promptUserView.findViewById(R.id.inputLabelParent);

        // get strings from shared preferences
        String homeLabelInput = preferences.getString("homeLabel", "");
        String homeInputCoords = preferences.getString("homeCoords", "");
        String friendLabelInput = preferences.getString("friendLabel", "");
        String friendInputCoords = preferences.getString("friendCoords", "");
        String parentLabelInput = preferences.getString("parentLabel", "");
        String parentInputCoords = preferences.getString("parentCoords", "");

        // set text in the edit texts
        homeLabel.setText(homeLabelInput);
        homeCoords.setText(homeInputCoords);
        friendLabel.setText(friendLabelInput);
        friendCoords.setText(friendInputCoords);
        parentLabel.setText(parentLabelInput);
        parentCoords.setText(parentInputCoords);

        alertBuilder
                .setView(promptUserView)
                .setTitle("Welcome")
                .setMessage(message)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences preferences = activity.getSharedPreferences("mainPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        // home editing
                        editor.putString("homeLabel", homeLabel.getText().toString());
                        editor.putString("homeCoords", homeCoords.getText().toString());

                        // friend editing
                        editor.putString("friendLabel", friendLabel.getText().toString());
                        editor.putString("friendCoords", friendCoords.getText().toString());

                        // parent editing
                        editor.putString("parentLabel", parentLabel.getText().toString());
                        editor.putString("parentCoords", parentCoords.getText().toString());

//                        String changeHomeCoords = preferences.getString("homeCoords",
//                                "Default");
//                        String changeHomeLabel = preferences.getString("homeLabel", "Default");
//                        TextView viewHomeCoords = activity.findViewById(R.id.homeCoords);
//                        TextView viewHomeLabel = activity.findViewById(R.id.homeLabel);
//                        viewHomeCoords.setText(homeInputCoords);
//                        viewHomeLabel.setText(homeLabelInput);
//
//
//                        // (Below 2 lines for testing purposes) Write home coordinates to textview
//                        String changeFriendCoords = preferences.getString("friendCoords",
//                                "Default");
//                        String changeFriendLabel = preferences.getString("friendLabel",
//                                "Default");
//                        TextView viewCoordsFriend = activity.findViewById(R.id.friendCoords);
//                        TextView viewLabelFriend = activity.findViewById(R.id.friendLabel);
//                        viewCoordsFriend.setText(friendInputCoords);
//                        viewLabelFriend.setText(friendLabelInput);
//
//                        // (Below 2 lines for testing purposes) Write home coordinates to textview
//                        String changeParentCoords = preferences.getString("parentCoords",
//                                "Default");
//                        String changeParentLabel = preferences.getString("parentLabel",
//                                "Default");
//                        TextView viewCoordsParent = activity.findViewById(R.id.parentCoords);
//                        TextView viewLabelParent = activity.findViewById(R.id.parentLabel);
//                        viewCoordsParent.setText(parentInputCoords);
//                        viewLabelParent.setText(parentLabelInput);

                        showAlert(activity, "Data Saved Successfully");

                        editor.putBoolean("newUser", false);
                        editor.apply();
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // Displays alert dialog with a message to the user
    private static void showAlert(LocationListActivity activity, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // Takes input from user and returns (latitude, longitude) coordinates as doubles
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

    public static HashMap<String, Pair<Double, Double>> getHashMap() {
        return locations;
    }

    public static String getHomeName() {
        return homeName;
    }

    public static String getFriendName() {
        return friendName;
    }

    public static String getParentName() {
        return parentName;
    }

    public static double[] extractCoords;
}
