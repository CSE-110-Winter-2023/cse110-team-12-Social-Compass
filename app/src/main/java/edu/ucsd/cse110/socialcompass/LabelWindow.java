package edu.ucsd.cse110.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;

public class LabelWindow {

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
