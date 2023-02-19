package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

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
