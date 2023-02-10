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

                        //get the inputted text and apply to preferences
                        /*TODO: implement method for converting inputCoords to (Float, Float) format
                            and write test cases for checking that the input is valid.
                         */
                        EditText inputCoords = promptUserView.findViewById(R.id.inputCoords);
                        editor.putString("homeCoords", inputCoords.getText().toString());
                        editor.apply();

                        // (Below 2 lines for testing purposes) Write home coordinates to textview
                        String homeCoords = preferences.getString("homeCoords", "Default");
                        TextView testCoords = activity.findViewById(R.id.testCoords);
                        testCoords.setText(homeCoords);
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
