package edu.ucsd.cse110.socialcompass.model;

import androidx.annotation.NonNull;
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

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;
    public int order;

    // Constructor matching fields above
    public Friend(@NonNull String label, @NonNull String uid, double latitude, double longitude, int order) {
        this.label = label;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.order = order;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getUid() { return uid; }

    public void setUID(String uid) { this.uid = uid; }

    public String getLabel() { return label; }

    public void setLabel(String name) { this.label = name; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    // Factory method for creating Friend from JSON file
    public static Friend fromJSON(String json) { return new Gson().fromJson(json, Friend.class);}

    // Factory method for converting Friend content to JSON file
    public String toJSON() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "name='" + label + '\'' +
                ", uid=" + uid +
                ", order=" + order +
                '}';
    }
}
