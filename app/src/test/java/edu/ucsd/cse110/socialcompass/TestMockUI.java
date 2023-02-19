package edu.ucsd.cse110.socialcompass;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestMockUI {
    @Test
    public void test1() {
        //ctivity activityLocation = Robolectric.buildActivity(LocationListActivity.class);
        var scenario1 = ActivityScenario.launch(LocationListActivity.class);
        var context1 = ApplicationProvider.getApplicationContext();
        SharedPreferences locationPreferences = PreferenceManager.getDefaultSharedPreferences(context1);
        SharedPreferences.Editor editor = locationPreferences.edit();
        editor.putInt("orientation", 100);
        editor.apply();

        var scenario2 = ActivityScenario.launch(MainActivity.class);
        var context2 = ApplicationProvider.getApplicationContext();
        SharedPreferences mainPreferences = PreferenceManager.getDefaultSharedPreferences(context2);
        int orientation = mainPreferences.getInt("orientation", 0);
        assertEquals(orientation, 100);
    }
}
