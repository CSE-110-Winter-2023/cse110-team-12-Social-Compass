package edu.ucsd.cse110.socialcompass;

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
@Database(entities = {FriendListItem.class}, version=2)
public abstract class FriendDatabase extends RoomDatabase {
    private static FriendDatabase singleton = null;

    public abstract FriendListItemDao friendListItemDao();

    public synchronized static FriendDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = FriendDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static FriendDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class, "friend_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<FriendListItem> friends = FriendListItem
                                    .loadJSON(context, "test_friends.json");
                            getSingleton(context).friendListItemDao().insertAll(friends);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(FriendDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }

    public static void useTestSingleton(Context context) {
        singleton = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
    }
}
