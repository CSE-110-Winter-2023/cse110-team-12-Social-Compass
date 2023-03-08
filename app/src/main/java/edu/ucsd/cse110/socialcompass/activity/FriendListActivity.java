package edu.ucsd.cse110.socialcompass.activity;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import edu.ucsd.cse110.socialcompass.model.Friend;
import edu.ucsd.cse110.socialcompass.view.FriendAdapter;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendListViewModel;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.model.FriendDatabase;
import edu.ucsd.cse110.socialcompass.model.FriendDao;
import edu.ucsd.cse110.socialcompass.viewmodel.FriendViewModel;

public class FriendListActivity extends AppCompatActivity {
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public RecyclerView recyclerView;
    private FriendListViewModel viewModel;
    private EditText uidEditText;
    private Button addButton;

    FriendDatabase db;
    //FriendListItemDao dao;

    static boolean isInserted = false;
    FriendDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        var viewModel = setupViewModel();
        var adapter = setupAdapter(viewModel);

        setupViews(viewModel, adapter);
//        dao = MainActivity.getDao();
//
//        viewModel = new ViewModelProvider(this)
//                .get(FriendListViewModel.class);
//
//        FriendListAdapter adapter = new FriendListAdapter();
//        adapter.setHasStableIds(true);
//        viewModel.getFriendListItems().observe(this, adapter::setFriendListItems);
//
//        recyclerView = findViewById(R.id.friend_items);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//
//        uidEditText = findViewById(R.id.UID_text);
//        addButton = findViewById(R.id.add_btn);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String uid = uidEditText.getText().toString();
//                System.out.println("banana");
//                insertFriendListItem(uid);
//                uidEditText.setText("");
//            }
//        });
    }

    private FriendListViewModel setupViewModel() {
        return new ViewModelProvider(this).get(FriendListViewModel.class);
    }

    @NonNull
    private FriendAdapter setupAdapter(FriendListViewModel viewModel) {
        FriendAdapter adapter = new FriendAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnFriendClickListener(friend -> onFriendClicked(friend, viewModel));
        viewModel.getFriends().observe(this, adapter::setFriends);
        return adapter;
    }

    private void setupViews(FriendListViewModel viewModel, FriendAdapter adapter) {
        setupRecycler(adapter);
        setupInput(viewModel);
        setupAddUIDButton(viewModel);
    }

    // Override the @VisibleForTesting annotation to allow access from this (and only this) method.
    @SuppressLint("RestrictedApi")
    private void setupRecycler(FriendAdapter adapter) {
        // We store the recycler view in a field _only_ because we will want to access it in tests.
        recyclerView = findViewById(R.id.recycler_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void setupInput(FriendListViewModel viewModel) {
        var input = (EditText) findViewById(R.id.UID_text);
        input.setOnEditorActionListener((view, actionId, event) -> {
            // If the event isn't "done" or "enter", do nothing.
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return false;
            }

            // Otherwise, create a new note, persist it...
            var uid = input.getText().toString();
            var friend = viewModel.getFriend(uid);

            // ...wait for the database to finish persisting it...
            friend.observe(this, friendEntity -> {
                // ...stop observing.
                friend.removeObservers(this);
            });

            return true;
        });
    }

    private void setupAddUIDButton(FriendListViewModel viewModel) {
        var input = (EditText) findViewById(R.id.UID_text);
        var addUIDButton = findViewById(R.id.addUID_btn);
        addUIDButton.setOnClickListener((View v) -> {
            String uid = input.getText().toString();
            var friend = viewModel.getFriend(uid).getValue();
            assert friend != null;

            friend.uid = uid;

            viewModel.save(friend);
        });
    }

    private void onFriendClicked(Friend friend, FriendListViewModel viewModel) {
        Log.d("FriendAdapter", "Opened friend " + friend.name);
        var intent = FriendActivity.intentFor(this, friend);
        startActivity(intent);
    }

//    public void insertFriendListItem(String uid) {
//        //FriendDatabase db = FriendDatabase.getSingleton(getApplication());
//        //FriendListItemDao dao = db.friendListItemDao();
//        //dao = getDao();
//        if (dao == null) {
//            throw new IllegalStateException("dao is null");
//        }
//        int order = dao.getOrderForAppend();
//
//        //Not done until we do Story 5, "Unknown" should be the name corresponding to UID
//        //Need to search for the name corresponding to the UID otherwise cannot find the user
//        //TEST THIS AGAIN AFTER STORY 5
//        dao.insert(new FriendListItem("Unknown", uid, order));
//        Log.i("tag", "Size is " + dao.getAll().size());
//        //System.out.println("Size is: " + dao.getFriendList().size());
//        if (dao.getAll().size() == 1) {
//            isInserted = true;
//        }
//    }

    public static boolean checkInsert() {
        return isInserted;
    }

}