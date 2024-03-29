package edu.ucsd.cse110.socialcompass.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Friend>> friends;

    private final FriendRepository repo;
    private FriendDao dao;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);
        dao = db.getDao();
        this.repo = new FriendRepository(dao);
    }

    /**
     * Get a friend from the database
     * @return a LiveData object that will be updated when the friend's location change.
     */
    public LiveData<Friend> getFriend(String uid) {

        return repo.getLocal(uid);
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

    /**
     *
     * @return a LiveData object that will be updated when any friend locations change.
     */
    public LiveData<List<Friend>> getFriendsWithinZone(Double inner, Double outer) {
        if (friends == null) {
            friends = dao.get_users_within_zone(inner,outer);
        }
        return friends;
    }
}
