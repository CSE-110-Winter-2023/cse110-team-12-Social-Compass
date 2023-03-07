package edu.ucsd.cse110.socialcompass;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendListItemDao {
    @Insert
    List<Long> insertAll(List<FriendListItem> friendListItem);

    @Insert
    long insert(FriendListItem friendListItem);

    @Query("SELECT * FROM `friend_list_items` WHERE `id`=:id")
    FriendListItem get(long id);

    @Query("SELECT * FROM `friend_list_items` ORDER BY `order`")
    LiveData<List<FriendListItem>> getAllLive();

    @Query("SELECT * FROM `friend_list_items` ORDER BY `order`")
    List<FriendListItem> getAll();

    //@Query("SELECT `order` + 1 FROM `friend_list_items` ORDER BY `order` DESC LIMIT 1")
    @Query("SELECT `order` FROM `friend_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Update
    int update(FriendListItem friendListItem);

    @Delete
    int delete(FriendListItem friendListItem);

//    @Query("SELECT * FROM friend_list_items WHERE uid = :uid")
//    FriendListItem getFriendListItem(String uid);
//    @Query("SELECT * FROM friend_list_items WHERE isFriend = 'true'")
//    List<FriendListItem> getFriendList();
}
