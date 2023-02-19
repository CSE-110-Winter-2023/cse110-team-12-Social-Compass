package edu.ucsd.cse110.socialcompass.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.ucsd.cse110.socialcompass.LocationListItem;

@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    private static LocationDatabase singletonInstance;

    public static LocationDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, LocationDatabase.class, "locations" +
                            ".db")
                    .allowMainThreadQueries()
                    .build();
        }
        return singletonInstance;
    }
    public static void useTestSingleton(Context context) {
        singletonInstance = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public abstract LocationDao locationDao();
}