package edu.ucsd.cse110.socialcompass.db;

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
    public Location(String label, String coordinates) {
        this.label = label;
        this.coordinates = coordinates;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    private int locationId = 0;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "coordinates")
    private String coordinates;

    public int getLocationId() { return locationId; }

    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public String getCoordinates() { return coordinates; }

    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public String toString() {
        return label + " " + coordinates;
    }
}
