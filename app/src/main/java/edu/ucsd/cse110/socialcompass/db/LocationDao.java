package edu.ucsd.cse110.socialcompass.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.ucsd.cse110.socialcompass.LocationListItem;
import edu.ucsd.cse110.socialcompass.db.Location;

/**
 * Data accessing for each location
 */
@Dao
public interface LocationDao {
    @Transaction
    @Query("SELECT * FROM `locations` WHERE label=:id")
    Location get(long id);

    @Query("SELECT * FROM 'locations' ORDER BY 'order'")
    List<Location> getAll();

    @Update
    int update(Location location);

    @Delete
    int delete(Location location);
}
