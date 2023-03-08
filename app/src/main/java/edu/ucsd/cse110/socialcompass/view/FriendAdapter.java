package edu.ucsd.cse110.socialcompass.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.model.Friend;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> friends = Collections.emptyList();
    private Consumer<Friend> onFriendClicked;

    public void setOnFriendClickListener(Consumer<Friend> onFriendClicked) {
        this.onFriendClicked = onFriendClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final TextView nameView;
        public final TextView uidView;
        public final TextView locationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            // Populate the text views...
            this.nameView = itemView.findViewById(R.id.name_text);
            this.uidView = itemView.findViewById(R.id.uid_text);
            this.locationView = itemView.findViewById(R.id.location_text);
        }

        public void bind(Friend friend) {
            nameView.setText(friend.name);
            uidView.setText(friend.uid);
            //TODO: set locationView text

            itemView.setOnClickListener(v -> onFriendClicked.accept(friend));
        }
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        var friend = friends.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public long getItemId(int position) {return friends.get(position).id;}

}
