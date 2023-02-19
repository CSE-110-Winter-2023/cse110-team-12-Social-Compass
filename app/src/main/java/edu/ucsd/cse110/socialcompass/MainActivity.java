package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;

import android.view.View;
import android.widget.TextView;

import edu.ucsd.cse110.socialcompass.db.LocationDao;
import edu.ucsd.cse110.socialcompass.db.LocationDatabase;

public class MainActivity extends AppCompatActivity {
    protected LocationDatabase db;
    protected LocationDao locationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadProfile();
    }

    public void loadProfile() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        // check if this is a new user, and if so, initialize their sharedPreferences
        Boolean newUser = preferences.getBoolean("newUser", true);
        if (newUser) {
            initNewUser();
            newUser = false;
        }

    }

    // This method should only be called one time EVER - for initializing brand new users.
    public void initNewUser() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Prompt user to input their home coordinates
        Utilities.showAlertDialog(this, "Please input your Home, Friend, and Parent coordinates");

        //below line is set to "true" for testing purposes
        editor.putBoolean("newUser", true);
        editor.apply();
    }

    public void onAddLocationClicked(View view) {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        Utilities.showAlertDialog(this, "Add Location Coordinates");

        Intent intent = new Intent(this, LocationListActivity.class);
        startActivity(intent);
    }
}