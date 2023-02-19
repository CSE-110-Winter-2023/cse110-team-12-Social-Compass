package edu.ucsd.cse110.socialcompass;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "location_list_items")
public class LocationListItem {

    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    @NonNull
    public String label, coords;

    public LocationListItem(@NonNull String label, @NonNull String coords) {
        this.label = label;
        this.coords = coords;
    }

    public static List<LocationListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocationListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "LocationListItem{" +
                "label='" + label + '\'' +
                ", coords=" + coords +
                '}';
    }
}
