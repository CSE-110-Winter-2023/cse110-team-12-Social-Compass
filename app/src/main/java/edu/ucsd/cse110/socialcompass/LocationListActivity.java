package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Class for editing list of locations/labels once the user has initially entered their data
 */
public class LocationListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        // initialize the LocationList Ad
        LocationListAdapter adapter = new LocationListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.location_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setLocationListItems(LocationListItem.loadJSON(this, "locations.json"));
    }

    public void onBackClicked(View view) {
        this.finish();
    }

    /**
     * Dialog that appears when the user confirms to save their inputted data
     * @param view The view that should appear when user inputs data
     */
    public void onAddInputClicked(View view) {
        Utilities.showAlertDialog(this, "Here is your saved data.");
    }

    /**
     * Mock UI testing for editing coordinates in a quick way
     * @param view The view that should appear when user inputs data
     */
    public void onSetOrientationClicked(View view) {
        SharedPreferences preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("orientation", 0);
        editor.apply();

        this.finish();
    }
}