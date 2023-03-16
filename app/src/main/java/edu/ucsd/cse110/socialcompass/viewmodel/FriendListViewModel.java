package edu.ucsd.cse110.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;

public class FriendListViewModel extends AndroidViewModel {
    private LiveData<List<Friend>> friends;
    public final FriendRepository repo;
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

    public boolean existsLocal(String uid) { return repo.existsLocal(uid); }

    public boolean existsRemote(String uid) {
        try {
            return repo.existsRemote(uid);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Friend friend) {repo.upsertSynced(friend);}

    public LiveData<List<Friend>> getAll() {
        return dao.getAll();
    }
    public void delete(Friend friend) {repo.deleteLocal(friend);}

    public void saveLocal(Friend friend) {
        repo.upsertLocal(friend);
    }

    public void syncLocal(Friend friend) {
        repo.getSynced(friend.getUid());
    }

}
