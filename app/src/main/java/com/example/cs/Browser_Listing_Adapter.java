package com.example.cs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.content.Intent;



public class Browser_Listing_Adapter extends RecyclerView.Adapter<Browser_Listing_Adapter.ViewHolder> {

    private List<Listing> listings;
    private List<Listing> fullList; // Backup list for filtering
    private Context context;

    public Browser_Listing_Adapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
        this.fullList = new ArrayList<>(listings); // Copy original list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listing listing = listings.get(position);
        holder.itemName.setText(listing.getName());
        holder.itemDescription.setText(listing.getDescription());

        // 🔹 Load image using Glide
        Glide.with(context)
                .load(listing.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.itemImage);

        holder.btnSwap.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);

            // Ensure context is an Activity before starting a new Activity
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription;
        ImageView itemImage;
        Button btnSwap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textView_item_name);
            itemDescription = itemView.findViewById(R.id.textView_item_description);
            itemImage = itemView.findViewById(R.id.imageView_item);
            btnSwap = itemView.findViewById(R.id.btnSwap);
        }
    }

    // 🔹 Method to filter list based on SearchView input
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(String query) {
        listings.clear();
        if (query.isEmpty()) {
            listings.addAll(fullList); // Reset list
        } else {
            for (Listing item : fullList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    listings.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
