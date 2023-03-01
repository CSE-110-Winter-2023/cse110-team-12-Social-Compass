package edu.ucsd.cse110.socialcompass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<FriendListItem> friendItems = Collections.emptyList();

    public void setFriendListItems(List<FriendListItem> newFriendItems) {
        this.friendItems.clear();
        this.friendItems = newFriendItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFriendItem(friendItems.get(position));
    }

    @Override
    public int getItemCount() {
        return friendItems.size();
    }

    @Override
    public long getItemId(int position) {return friendItems.get(position).id;}

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView friendName;
        private final TextView friendUID;
        private FriendListItem friendItem;;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friendName = itemView.findViewById(R.id.friend_item_name);
            this.friendUID = itemView.findViewById(R.id.friend_item_uid);
        }

        public FriendListItem getFriendItem() { return friendItem; }

        public void setFriendItem(FriendListItem friendItem) {
            this.friendItem = friendItem;
            this.friendName.setText(friendItem.name);
            this.friendUID.setText(friendItem.uid);
        }
    }
}
