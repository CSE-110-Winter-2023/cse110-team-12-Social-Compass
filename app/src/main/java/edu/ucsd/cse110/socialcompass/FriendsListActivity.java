package edu.ucsd.cse110.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class FriendsListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
    }

//    FriendsListAdapter adapter = new FriendsListAdapter();
//        adapter.setHasStableIds(true);
//
//    recyclerView = findViewById(R.id.location_items);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        adapter.setLocationListItems(FriendsListItem.loadJSON(this, "locations.json"));
}