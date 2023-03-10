package edu.ucsd.cse110.socialcompass.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private final FriendRepository repo;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);
        var dao = db.getDao();
        this.repo = new FriendRepository(dao);
    }

    public LiveData<Friend> getLocation(String uid) {
        return repo.getLocal(uid);
    }
}
