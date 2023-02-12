package edu.ucsd.cse110.socialcompass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Utilities {
    // Prompts the user with an AlertDialog to input location name and coordinates
    public static void showAlertDialog(MainActivity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        // Load the dialog layout for prompting the User
        LayoutInflater inflater = LayoutInflater.from(activity);
        View promptUserView = inflater.inflate(R.layout.dialog_userprompt, null);

        alertBuilder
                .setView(promptUserView)
                .setTitle("Welcome")
                .setMessage(message)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        // get user's input from the EditText field
                        EditText inputCoords = promptUserView.findViewById(R.id.inputCoords);
                        String input = inputCoords.getText().toString();

                        double[] coordinates = parseCoordinates(input); // parse the input

                        // if input is not null, then it is valid, so update preferences
                        if (coordinates != null)
                        {
                            editor.putString("homeCoords", inputCoords.getText().toString());
                            editor.apply();

                            // (Below 2 lines for testing purposes) Write home coordinates to textview
                            String homeCoords = preferences.getString("homeCoords", "Default");
                            TextView testCoords = activity.findViewById(R.id.testCoords);
                            testCoords.setText(homeCoords);
                        }
                        else { showAlert(activity,"Invalid Input"); }
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // Displays alert dialog with a message to the user
    private static void showAlert(MainActivity activity, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        android.app.AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // Takes input from user and returns (latitude, longitude) coordinates as doubles
    public static double[] parseCoordinates(String input) {
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
        coordinates[1] = latitude;
        return coordinates;
    }
}
