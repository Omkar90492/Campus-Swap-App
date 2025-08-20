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

public class MyListingsActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    private RecyclerView recyclerView;
    private ListingsAdapter adapter;
    private List<Listing> listingList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_listings);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("UploadedItems");

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listingList = new ArrayList<>();
        loadListings(); // Fetch data

        adapter = new ListingsAdapter(this, listingList);
        recyclerView.setAdapter(adapter);


    }

    private void loadListings() {

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
                    adapter = new ListingsAdapter(MyListingsActivity.this, listingList);
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
    }
}
