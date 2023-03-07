package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FriendListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private FriendListViewModel viewModel;
    private EditText uidEditText;
    private Button addButton;

    FriendDatabase db;
    FriendListItemDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        db = FriendDatabase.getSingleton(getApplication());
        dao = db.friendListItemDao();

        viewModel = new ViewModelProvider(this)
                .get(FriendListViewModel.class);

        FriendListAdapter adapter = new FriendListAdapter();
        adapter.setHasStableIds(true);
        viewModel.getFriendListItems().observe(this, adapter::setFriendListItems);

        recyclerView = findViewById(R.id.friend_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        uidEditText = findViewById(R.id.UID_text);
        addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = uidEditText.getText().toString();
                insertFriendListItem(uid);
                uidEditText.setText("");
            }
        });
    }

    public void insertFriendListItem(String uid) {
        //FriendDatabase db = FriendDatabase.getSingleton(getApplication());
        //FriendListItemDao dao = db.friendListItemDao();
        int order = dao.getOrderForAppend();

        //Not done until we do Story 5, "Unknown" should be the name corresponding to UID
        //Need to search for the name corresponding to the UID otherwise cannot find the user
        //TEST THIS AFTER STORY 5
        dao.insert(new FriendListItem("Unknown", uid, order));
    }

}