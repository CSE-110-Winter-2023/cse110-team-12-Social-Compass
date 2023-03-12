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

public class FriendListViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friends;
    private final FriendRepository repo;
    private FriendDao dao;

    public FriendListViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);
        this.dao = db.getDao();
        this.repo = new FriendRepository(dao);
    }

    /**
     * Get a friend from the database
     * @return a LiveData object that will be updated when the friend's location change.
     */
    public LiveData<Friend> getFriend(String uid) {
//        return repo.getLocal(uid);
        return repo.getRemote(uid);
    }

    /**
     * Load all friends from the database.
     * @return a LiveData object that will be updated when any friend locations change.
     */
    public LiveData<List<Friend>> getFriends() {
        if (friends == null) {
            friends = repo.getAllLocal();
        }
        return friends;
    }

    public void save(Friend friend) {repo.upsertSynced(friend);}

    public LiveData<List<Friend>> getAll() {
        return dao.getAll();
    }

    public long upsert(Friend friend) {
        return dao.upsert(friend);
    }
}
