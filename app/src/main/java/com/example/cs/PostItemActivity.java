package com.example.cs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PostItemActivity extends AppCompatActivity {

    private EditText itemName, itemDescription;
    private ImageView itemImage;
    private Uri imageUri;
    private Button uploadButton;
    Toolbar toolbar;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    itemImage.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upload New Item");
        setSupportActionBar(toolbar);

        itemName = findViewById(R.id.editTextItemName);
        itemDescription = findViewById(R.id.editTextItemDescription);
        itemImage = findViewById(R.id.imageViewItem);
        uploadButton = findViewById(R.id.buttonUpload);

        // Initialize Firebase
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Image picker
        itemImage.setOnClickListener(v -> pickImage());

        // Upload button click
        uploadButton.setOnClickListener(v -> uploadItem());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadItem() {
        String name = itemName.getText().toString().trim();
        String description = itemDescription.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all details and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auth.getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(PostItemActivity.this, Login_Activity.class));
            finish();
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        StorageReference storageRef = storage.getReference().child("UploadedImages/" + System.currentTimeMillis() + ".jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveItemToFirestore(name, description, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("FirebaseStorage", "Upload failed", e);
                });
    }

    private void saveItemToFirestore(String name, String description, String imageUrl) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("description", description);
        item.put("imageUrl", imageUrl);
        item.put("userId", auth.getCurrentUser().getUid());

        firestore.collection("UploadedItems").add(item)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Item Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostItemActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Firestore", "Failed to add item", e);
                });
    }
}
