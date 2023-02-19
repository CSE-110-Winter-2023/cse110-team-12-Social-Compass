package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.app.AlertDialog;

public class LabelWindow {

    // Creates an AlertDialog when a house icon (custom button) is clicked
    // and displays the label inputted by the user on start up.
    public static void showLabel(Activity activity, String message) {
        AlertDialog.Builder label = new AlertDialog.Builder(activity);

        label
                .setTitle("Label")
                .setMessage(message)
                .setPositiveButton("Close", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = label.create();
        alertDialog.show();

    }

}
