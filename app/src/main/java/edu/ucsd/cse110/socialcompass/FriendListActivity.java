package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class FriendListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private FriendListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        viewModel = new ViewModelProvider(this)
                .get(FriendListViewModel.class);

        FriendListAdapter adapter = new FriendListAdapter();
        adapter.setHasStableIds(true);
        viewModel.getFriendListItems().observe(this, adapter::setFriendListItems);

        recyclerView = findViewById(R.id.friend_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        uidEditText = findViewById(R.id.uid_text);
        addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = uidEditText.getText().toString();
                System.out.println("banana");
                insertFriendListItem(uid);
                uidEditText.setText("");
            }
        });
    }

    public void insertFriendListItem(String uid) {
        FriendDatabase db = FriendDatabase.getSingleton(getApplication());
        FriendListItemDao dao = db.friendListItemDao();
        //dao = getDao();
        if (dao == null) {
            throw new IllegalStateException("dao is null");
        }
        int order = dao.getOrderForAppend();

        //Not done until we do Story 5, "Unknown" should be the name corresponding to UID
        //Need to search for the name corresponding to the UID otherwise cannot find the user
        //TEST THIS AFTER STORY 5
        dao.insert(new FriendListItem("Unknown", uid, order));
        //Log.i("tag", "Size is " + dao.getAll().size());
        System.out.println("Size is: " + dao.getFriendList().size());
        if (dao.getAll().size() == 1) {
            isInserted = true;
        }
    }

    public static boolean checkInsert() {
        return isInserted;
    }

}