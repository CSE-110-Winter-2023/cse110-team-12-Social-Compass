package edu.ucsd.cse110.socialcompass;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "friend_list_items")
public class FriendListItem {
    // Public fields
    @PrimaryKey(autoGenerate = true)
    public long id; // id for database

    @NonNull
    public String name, uid;
    public int order;

    // Constructor matching fields above
    public FriendListItem(@NonNull String name, @NonNull String uid, int order) {
        this.name = name;
        this.uid = uid;
        this.order = order;
    }

    // Factory method for loading our JSON
    public static List<FriendListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<FriendListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                ", order=" + order +
                '}';
    }
}
