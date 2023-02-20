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

/**
 * Adapter class should
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private List<LocationListItem> locationItems = Collections.emptyList();

    public void setLocationListItems(List<LocationListItem> newLocationItems) {
        this.locationItems.clear();
        this.locationItems = newLocationItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setLocationItem(locationItems.get(position));
    }

    @Override
    public int getItemCount() { return locationItems.size(); }

    @Override
    public long getItemId(int position) { return locationItems.get(position).id; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EditText editLabel;
        private final EditText editCoords;
        private LocationListItem locationItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.editLabel = itemView.findViewById(R.id.inputLabel);
            this.editCoords = itemView.findViewById(R.id.inputCoords);
        }

        public LocationListItem getLocationItem() { return locationItem; }

        public void setLocationItem(@NonNull LocationListItem locationItem) {
            this.locationItem = locationItem;
            this.editLabel.setText(locationItem.label);
            this.editCoords.setText(locationItem.coords);
        }
    }
}
