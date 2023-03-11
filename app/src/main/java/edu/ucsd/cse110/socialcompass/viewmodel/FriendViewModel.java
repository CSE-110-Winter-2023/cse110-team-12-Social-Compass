package edu.ucsd.cse110.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;

public class FriendViewModel extends AndroidViewModel {
    private LiveData<Friend> friend;
    private final FriendRepository repo;
    private FriendDatabase db;
    private FriendDao dao;


    public FriendViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        this.db = FriendDatabase.provide(context);
        this.dao = db.getDao();
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
