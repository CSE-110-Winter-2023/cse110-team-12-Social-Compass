package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "friends")
public class Friend {
    // Public fields
    public long id; // id for database

    @ColumnInfo(name = "name")
    @SerializedName("label")
    public String name;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "public_code")
    @SerializedName("public_code")
    public String public_code;

    @NonNull
    @ColumnInfo(name = "uid")
    @SerializedName("private_code")
    public String uid;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;
    public int order;

    // Constructor matching fields above
    public Friend(@NonNull String name, @NonNull String uid, double latitude, double longitude, int order) {
        this.name = name;
        this.uid = uid;
        this.public_code = uid;
        this.latitude = latitude;
        this.longitude = longitude;
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
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
}
