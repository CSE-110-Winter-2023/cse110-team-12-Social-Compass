package edu.ucsd.cse110.socialcompass.model;

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

import edu.ucsd.cse110.socialcompass.Utilities;

@Entity(tableName = "friends")
public class Friend {
    // Public fields
    @PrimaryKey(autoGenerate = true)
    public long id; // id for database

    @NonNull
    public String name, uid;
    public boolean isFriend;
    public int order;

    // Constructor matching fields above
    public Friend(@NonNull String name, @NonNull String uid, int order) {
        this.name = name;
        this.uid = uid;
        this.order = order;
        this.isFriend = !uid.equals(Utilities.getUID());
    }

    // Factory method for loading our JSON
    public static List<Friend> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Friend>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Factory method for creating Friend from JSON file
    public static Friend fromJSON(String json) { return new Gson().fromJson(json, Friend.class);}

    // Factory method for converting Friend content to JSON file
    public String toJSON() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                ", order=" + order +
                '}';
    }

    public String getUid() {
        return this.uid;
    }
}