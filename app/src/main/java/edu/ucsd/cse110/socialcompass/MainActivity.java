package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import edu.ucsd.cse110.socialcompass.db.Location;
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
        LocationDatabase.useTestSingleton(this);
        db = LocationDatabase.singleton(this);
        locationDao = db.locationDao();

        /**
         * for (entry e : map) {
         *  if (e != null && e == Utilities.getHomeName()) {
         *      //make new object
         *  }
         *  }
         */

        HashMap<String, Pair<Double, Double>> map = Utilities.getHashMap();
        //saveHashMap();
        /*Location myHome = new Location(Utilities.getHomeName(),
                Objects.requireNonNull(map.get(Utilities.getHomeName())).first, Objects.requireNonNull(map.get(Utilities.getHomeName())).second);
        myHome.setLocationId(locationDao.maxId() + 1);
        locationDao.insert(myHome);

        Location myFriend = new Location(Utilities.getFriendName(),
                Objects.requireNonNull(map.get(Utilities.getFriendName())).first, Objects.requireNonNull(map.get(Utilities.getFriendName())).second);
        myFriend.setLocationId(locationDao.maxId() + 1);
        locationDao.insert(myFriend);

        Location myParent = new Location(Utilities.getParentName(),
                Objects.requireNonNull(map.get(Utilities.getParentName())).first, Objects.requireNonNull(map.get(Utilities.getParentName())).second);
        myParent.setLocationId(locationDao.maxId() + 1);
        locationDao.insert(myParent);*/
    }

    public void loadProfile() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        // check if this is a new user, and if so, initialize their sharedPreferences
        Boolean newUser = preferences.getBoolean("newUser", true);
        initNewUser();
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

    public void saveData() {

    }

    /*public HashMap<String, Pair<Double, Double>> getArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);

        //NOTE: if shared preference is null, the method return empty Hashset and not null
        Set<String> set = sp.getStringSet("list", new HashSet<String>());

        return new ArrayList<String>(set);
    }*/

    public void onStop() {
        saveData();
        super.onStop();
    }
}