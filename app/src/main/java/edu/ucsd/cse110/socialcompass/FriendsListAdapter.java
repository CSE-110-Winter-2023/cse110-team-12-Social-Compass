package edu.ucsd.cse110.socialcompass;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<FriendsListItem> locationItems = Collections.emptyList();

    public void setLocationListItems(List<FriendsListItem> newLocationItems) {
        this.locationItems.clear();
        this.locationItems = newLocationItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EditText editLabel;
        private final EditText editCoords;
        private FriendsListItem locationItem;;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.editLabel = itemView.findViewById(R.id.inputLabel);
            this.editCoords = itemView.findViewById(R.id.inputCoords);
        }

        public FriendsListItem getLocationItem() { return locationItem; }

        public void setLocationItem(FriendsListItem locationItem) {
            this.locationItem = locationItem;
            this.editLabel.setText(locationItem.label);
            this.editCoords.setText(locationItem.coords);
        }
    }
}
