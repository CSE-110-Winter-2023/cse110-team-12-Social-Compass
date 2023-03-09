package edu.ucsd.cse110.socialcompass.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FriendRepository {

    private final FriendDao dao;
    private final FriendAPI api;
    private ScheduledFuture<?> poller;

    public FriendRepository(FriendDao dao) {
        api = FriendAPI.provide();
        this.dao = dao;
    }

    // Synced Methods

    public LiveData<Friend> getSynced(String uid) {
        var friend = new MediatorLiveData<Friend>();

        Observer<Friend> updateFromRemote = actualFriend -> {
            // localFriend is the Friend object from the perspective of our local database
            // actualFriend is the true Friend Object being observed remotely
            var localFriend = friend.getValue();
            if (actualFriend == null) return; // do nothing
            if (localFriend == null) {
                upsertLocal(actualFriend);
            }
        };

        // If we get a local update, pass it on.
        friend.addSource(getLocal(uid), friend::postValue);
        // If we get a remote update, update the local version (triggering the above observer)
        friend.addSource(getRemote(uid), updateFromRemote);

        return friend;
    }

    public void upsertSynced(Friend friend) {
        upsertLocal(friend);
        upsertRemote(friend);
    }

    // Local Methods

    public LiveData<Friend> getLocal(String uid) {
        return dao.get(uid);
    }

    public LiveData<List<Friend>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(Friend friend) {
        //TODO: update the time that friend was last updated (similar to incrementVersion)
        dao.upsert(friend);
    }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(String uid) {
        return dao.exists(uid);
    }

    // Remote Methods
    // ==============

    public LiveData<Friend> getRemote(String uid) {
        // Cancel any previous poller if it exists.
        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }

        var friend = new MutableLiveData<Friend>();

        // Set up a background thread that will poll the server every second.
        var executor = Executors.newSingleThreadScheduledExecutor();
        poller = executor.scheduleAtFixedRate(() -> {
            //TODO: change this part to update location values for friend
            friend.postValue(Friend.fromJSON(api.getFriend(uid)));
        }, 0, 1000, TimeUnit.MILLISECONDS);

        return friend;
    }

    /**
     * Should only be upserting the user's self location.
     * @ensure friend.uid = sharedPreferences.get("myUID", "")
     */
    public void upsertRemote(Friend friend) {
        var executor = Executors.newSingleThreadExecutor();
        executor.execute( () -> {
            api.putFriend(friend);
        });
    }
}