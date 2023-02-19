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

    public void onAddInputClicked(View view) {
        Utilities.showAlertDialog(this, "Here is your saved data.");
    }

    public void onSetOrientationClicked(View view) {
        SharedPreferences preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("orientation", 0);
        editor.apply();

        this.finish();
    }
}