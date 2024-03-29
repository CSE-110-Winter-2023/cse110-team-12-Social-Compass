package edu.ucsd.cse110.socialcompass.model;

import androidx.annotation.AnyThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FriendRepository {

    private final FriendDao dao;
    private final FriendAPI api;
    private ScheduledFuture<?> poller;
    private LiveData<List<Friend>> friendList;
    private final Map<String, ScheduledFuture<?>> pollerMap = new ConcurrentHashMap<>();


    public FriendRepository(FriendDao dao) {
        api = FriendAPI.provide();
        this.dao = dao;
        this.friendList = dao.getAll();
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

    @AnyThread
    public boolean existsRemote(String uid) throws ExecutionException, InterruptedException {
        var executor = Executors.newSingleThreadExecutor();
        var code = executor.submit(() -> api.getFriendCode(uid));
        return code.get() == 200;
    }

    public LiveData<Friend> getRemote(String uid) {
        // Cancel any previous poller if it exists.
        ScheduledFuture<?> previousPoller = pollerMap.get(uid);
        if (previousPoller != null && !previousPoller.isCancelled()) {
            previousPoller.cancel(true);
        }

        var friend = new MutableLiveData<Friend>();

        // Set up a ScheduledThreadPoolExecutor that will poll the server every second.
        var executor = new ScheduledThreadPoolExecutor(1);
        ScheduledFuture<?> poller = executor.scheduleAtFixedRate(() -> {
            Friend getFriend = Friend.fromJSON(api.getFriend(uid));
            getFriend.uid = getFriend.public_code;
            if(friend.getValue() == null || getFriend.latitude != friend.getValue().latitude
                    || getFriend.longitude != friend.getValue().longitude){
                upsertLocal(getFriend);
            }
            friend.postValue(getFriend);
        }, 0, 1000, TimeUnit.MILLISECONDS);

        // Add the poller to the map
        pollerMap.put(uid, poller);

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
