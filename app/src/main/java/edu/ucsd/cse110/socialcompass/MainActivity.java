package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static FriendDatabase db;
    private static FriendListItemDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this.getApplicationContext();
        db = FriendDatabase.getSingleton(context);
        var dao = db.friendListItemDao();
        List<FriendListItem> users = dao.getAll();

        if (users.size() == 0) {
            initNewUser();
        }
    }

    public static FriendListItemDao getDao()
    {
        return dao;
    }



    // This method should only be called one time EVER - for initializing brand new users.
    private void initNewUser() {
        //TODO: Ask for location permission and build initial HashMap for storing data
        Utilities.showUserNamePromptAlert(this, "Please enter your name",db);

    }

    public void onSeeFriendsClicked(View view) {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }
}