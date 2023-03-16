package edu.ucsd.cse110.socialcompass.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
    }

    /** Utility method to create an intent for this activity. */
    public static Intent intentFor(Context context, Friend friend) {
        var intent = new Intent(context, FriendActivity.class);
        intent.putExtra("friend_name", friend.name);
        return intent;
    }
}