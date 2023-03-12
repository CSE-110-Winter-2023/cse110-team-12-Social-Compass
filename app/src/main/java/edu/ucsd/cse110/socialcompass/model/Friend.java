package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

@Entity(tableName = "friends")
public class Friend {
    // Public fields
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id; // id for database

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "uid")
    public String uid;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;
    public int order;

    // Constructor matching fields above
    public Friend(@NonNull String name, @NonNull String uid, int order) {
        this.name = name;
        this.uid = uid;
        this.order = order;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getUid() { return uid; }

    public void setUID(String uid) { this.uid = uid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public void setLatitude(double latitude) {
        locationService.getLocation().observe(this, loc -> {
            self.setLatitude(locationService.getLocation().getValue().first);
            self.setLongitude(locationService.getLocation().getValue().second);
            Log.i("Location", self.latitude + "," + self.longitude);
        });
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Factory method for creating Friend from JSON file
    public static Friend fromJSON(String json) { return new Gson().fromJson(json, Friend.class);}

    // Factory method for converting Friend content to JSON file
    public String toJSON() {
        System.out.println(new Gson().toJson(this));
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
}
