package com.example.cs;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView; // ✅ Correct Import

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class BrowseListingsActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    private RecyclerView recyclerView;
    private Browser_Listing_Adapter adapter;
    private List<Listing> listingList;
    private SearchView searchView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_listings);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("UploadedItems");

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listingList = new ArrayList<>();
        loadListings(); // Fetch data

        adapter = new Browser_Listing_Adapter(this, listingList);
        recyclerView.setAdapter(adapter);

        // 🔹 SearchView Listener for Filtering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterList(newText);
                return false;
            }
        });


    }

    private void loadListings() {
        // Show ProgressBar when loading
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Sample Data (Replace with actual data retrieval later)
//        listingList.add(new Listing("Laptop", "Dell Inspiron 15, good condition", "https://example.com/laptop.jpg"));
//        listingList.add(new Listing("Headphones", "Sony WH-1000XM4, noise canceling", "https://example.com/headphones.jpg"));

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot item : task.getResult().getDocuments()) {
                        Listing listing = item.toObject(Listing.class);
                        if (listing != null) {
                            listingList.add(listing);
                        }
                    }
                    adapter = new Browser_Listing_Adapter(BrowseListingsActivity.this, listingList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("FirestoreError", "Error getting documents", task.getException());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        // Hide ProgressBar after loading
        progressBar.setVisibility(ProgressBar.GONE);
    }
}
