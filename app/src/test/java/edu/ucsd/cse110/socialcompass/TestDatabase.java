package edu.ucsd.cse110.socialcompass;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class TestDatabase {
    /*private LocationListItem.LocationListItemDao dao;
    private LocationListItem.LocationDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocationListItem.LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.locationListItemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }*/
}
