package edu.ucsd.cse110.socialcompass.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

//@Database(entities = {FriendListItem.class}, version=1)
@Database(entities = {Friend.class}, version=8, exportSchema = false)
public abstract class FriendDatabase extends RoomDatabase {
    //private static FriendDatabase singleton = null;
    private volatile static FriendDatabase instance = null;

    public abstract FriendDao getDao();

    public synchronized static FriendDatabase provide(Context context) {
        if (instance == null) {
            instance = FriendDatabase.makeDatabase(context);
        }
        return instance;
    }

    private static FriendDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class, "friend_app.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(FriendDatabase testDatabase) {
        if (instance != null) {
            instance.close();
        }
        instance = testDatabase;
    }
}
