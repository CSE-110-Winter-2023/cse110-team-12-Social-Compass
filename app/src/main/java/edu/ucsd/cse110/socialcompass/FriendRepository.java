//package edu.ucsd.cse110.socialcompass;
//
//import android.util.Log;
//
//import androidx.annotation.MainThread;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MediatorLiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import static android.content.ContentValues.TAG;
//
//import java.time.Instant;
//import java.io.IOException;
//import java.time.Instant;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//import edu.ucsd.cse110.socialcompass.FriendAPI;
//import edu.ucsd.cse110.socialcompass.FriendListItem;
//import edu.ucsd.cse110.socialcompass.FriendListItemDao;
//
//public class FriendRepository {
//    private final FriendListItemDao dao;
//    private ScheduledFuture<?> poller; // what could this be for... hmm?
//    private FriendAPI api;
//
//    public FriendRepository(FriendListItemDao dao) {
//        this.dao = dao;
//        this.api = new FriendAPI();
//    }
//
//    // Synced Methods
//    // ==============
//
//    /**
//     * This is where the magic happens. This method will return a LiveData object that will be
//     * updated when the note is updated either locally or remotely on the server. Our activities
//     * however will only need to observe this one LiveData object, and don't need to care where
//     * it comes from!
//     *
//     * This method will always prefer the newest version of the note.
//     *
//     * @param title the title of the note
//     * @return a LiveData object that will be updated when the note is updated locally or remotely.
//     */
//    public LiveData<FriendListItem> getSynced(String title) {
//        var friend = new MediatorLiveData<FriendListItem>();
//
//        Observer<FriendListItem> updateFromRemote = theirFriend -> {
//            var ourFriend = friend.getValue();
//            if (theirFriend == null) return; // do nothing
//            if (ourFriend == null || ourFriend.updated_at < theirFriend.updated_at) {
//                upsertLocal(theirFriend);
//            }
//        };
//
//        // If we get a local update, pass it on.
//        friend.addSource(getLocal(title), friend::postValue);
//        // If we get a remote update, update the local version (triggering the above observer)
//        friend.addSource(getRemote(title), updateFromRemote);
//
//        return friend;
//    }
//
//    public void upsertSynced(FriendListItem friend) {
//        upsertLocal(friend);
//        upsertRemote(friend);
//    }
//
//    // Local Methods
//    // =============
//
//    public FriendListItem getLocal(long id) {
//        return dao.get(id);
//    }
//
//    public LiveData<List<FriendListItem>> getAllLocal() {
//        return dao.getAllLive();
//    }
//
//    public void upsertLocal(FriendListItem friend) {
//        friend.version = friend.version + 1;
//        dao.upsert(friend);
//    }
//
//    public void deleteLocal(FriendListItem friend) {
//        dao.delete(friend);
//    }
//
////    public boolean existsLocal(String title) {
////        return dao.exists(title);
////    }
//
//    // Remote Methods
//    // ==============
//
//    public LiveData<FriendListItem> getRemote(String title) {
//        // TODO: Implement getRemote!
//        // TODO: Set up polling background thread (MutableLiveData?)
//        // TODO: Refer to TimerService from https://github.com/DylanLukes/CSE-110-WI23-Demo5-V2.
//
//        // Start by fetching the note from the server _once_ and feeding it into MutableLiveData.
//        // Then, set up a background thread that will poll the server every 3 seconds.
//
//        // You may (but don't have to) want to cache the LiveData's for each title, so that
//        // you don't create a new polling thread every time you call getRemote with the same title.
//        // You don't need to worry about killing background threads.
//        if (this.poller != null && !this.poller.isCancelled()) {
//            poller.cancel(true);
//        }
//        MutableLiveData<Note> remoteNote = new MutableLiveData<>();
//
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            Note latestNote = NoteAPI.provide().getNote(title);
//            if (remoteNote.getValue() == null || latestNote.version > remoteNote.getValue().version) {
//                //upsertSynced(latestNote);
//                upsertRemote(latestNote);
//            }
//            remoteNote.postValue(latestNote);
//        }, 0, 3, TimeUnit.SECONDS);
//
//        return remoteNote;
//
//    }
//
//
////    @MainThread
////    public LiveData<Note> getRemote(String title) {
////        MutableLiveData<Note> remoteNote = new MutableLiveData<>();
////
////        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
////        executor.scheduleAtFixedRate(() -> {
////            Note latestNote = NoteAPI.provide().getNote(title);
////            remoteNote.postValue(latestNote);
////        }, 0, 3, TimeUnit.SECONDS);
////
////        return remoteNote;
////    }
//
//    public void upsertRemote(Note note) {
//        // TODO: Implement upsertRemote!
//        api.putNoteAsync(note);
//    }
//}
