package edu.ucsd.cse110.socialcompass;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FriendListViewModel extends AndroidViewModel {
    private LiveData<List<FriendListItem>> friendListItems;
    private final FriendListItemDao friendListItemDao;

    public FriendListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        FriendDatabase db = FriendDatabase.getSingleton(context);
        friendListItemDao = db.friendListItemDao();
    }

    public LiveData<List<FriendListItem>> getFriendListItems() {
        if (friendListItems == null) {
            loadUsers();
        }
        return friendListItems;
    }

    private void loadUsers() { friendListItems = friendListItemDao.getAllLive(); }
}
