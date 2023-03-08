package edu.ucsd.cse110.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friend;
    private final FriendRepository repo;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);
        var dao = db.getDao();
        this.repo = new FriendRepository(dao);
    }

    public LiveData<Friend> getFriend(String uid) {
        // The returned live data should update whenever there is a change in
        // the user's location, or when the server returns an updated location
        // for any of the user's friends.
        // Polling interval: 1s.
        if (friend == null) {
            friend = repo.getSynced(uid);
        }
        return friend;
    }
}
