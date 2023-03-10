package edu.ucsd.cse110.socialcompass.db;

import android.util.Pair;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Database for all locations
 */
@Entity(tableName = "locations")
public class Location {

    /**
     * Add a location, given label and coordinates
     */
    public Location(String label, double latitude, double longitude) {
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    private int locationId = 0;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    public int getLocationId() { return locationId; }

    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String toString() {
        return label + " " + latitude + ", " + longitude;
    }

}
