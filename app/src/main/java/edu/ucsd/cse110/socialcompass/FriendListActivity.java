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
    }
}