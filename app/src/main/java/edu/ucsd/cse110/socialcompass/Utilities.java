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
    // Prompts the user with an AlertDialog to input location name and coordinates
    public static void showAlertDialog(LocationListActivity activity, String message) {
        SharedPreferences preferences = activity.getSharedPreferences("mainPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        // Load the dialog layout for prompting the User
        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserView = inflater.inflate(R.layout.dialog_userprompt, null);

        // get edit texts
        EditText homeCoords = promptUserView.findViewById(R.id.inputCoordsHome);
        EditText homeLabel = promptUserView.findViewById(R.id.inputLabelHome);
        EditText friendCoords = promptUserView.findViewById(R.id.inputCoordsFriend);
        EditText friendLabel = promptUserView.findViewById(R.id.inputLabelFriend);
        EditText parentCoords = promptUserView.findViewById(R.id.inputCoordsParent);
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

                        // save user input to shared preferences
                        editor.putString("homeLabel", homeLabel.getText().toString());
                        editor.putString("homeCoords", homeCoords.getText().toString());
                        editor.putString("friendLabel", friendLabel.getText().toString());
                        editor.putString("friendCoords", friendCoords.getText().toString());
                        editor.putString("parentLabel", parentLabel.getText().toString());
                        editor.putString("parentCoords", parentCoords.getText().toString());

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
        if (parts.length != 2) {
            return null;
        }

        double latitude = Double.parseDouble(parts[0]);
        double longitude = Double.parseDouble(parts[1]);
        // check that latitude and longitude are valid
        if (latitude < -90 || latitude > 90) {
            return null;
        }
        if (longitude < -180 || longitude > 180) {
            return null;
        }

        coordinates[0] = latitude;
        coordinates[1] = longitude;
        return coordinates;
    }
}
