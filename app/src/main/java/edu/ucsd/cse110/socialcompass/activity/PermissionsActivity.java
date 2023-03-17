package edu.ucsd.cse110.socialcompass.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.socialcompass.R;

public class PermissionsActivity extends AppCompatActivity {

    @Override
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    protected void onCreate(Bundle savedInstanceState) {
        // Check if user is new
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        
    }

}
