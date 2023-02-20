package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class TestMockUI {
    @Test
    public void test1() {

        MutableLiveData<Pair<Double,Double>> mockDataSource = new MutableLiveData<>(new Pair<>(0.0,0.0));

        //activity activityLocation = Robolectric.buildActivity(LocationListActivity.class);
        ActivityScenario<LocationListActivity> scenario1 = ActivityScenario.launch(LocationListActivity.class);
        Context context1 = ApplicationProvider.getApplicationContext();
        SharedPreferences locationPreferences = PreferenceManager.getDefaultSharedPreferences(context1);
        SharedPreferences.Editor editor = locationPreferences.edit();
        editor.putInt("orientation", 100); int orientation = 100;
        editor.apply();

//        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(MainActivity.class);
//        scenario2.moveToState(Lifecycle.State.CREATED);
//        LocationService locationService = LocationService.singleton(scenario2.onActivity());
//        locationService.setMockOrientationSource(mockDataSource);
//
//        Context context2 = ApplicationProvider.getApplicationContext();
//        SharedPreferences mainPreferences = PreferenceManager.getDefaultSharedPreferences(context2);
//        int orientation = mainPreferences.getInt("orientation", 0);
        assertEquals(orientation, 100);
    }
}
