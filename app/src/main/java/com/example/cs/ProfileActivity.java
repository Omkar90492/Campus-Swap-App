package com.example.cs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPhone, editTextDepartment, editTextEmail, editTextYear;
    private Button buttonSave, buttonLogout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    Toolbar toolbar;
    private FirebaseUser user;
    FirebaseStorage firebaseStorage;
    ImageView profile_image;
    Uri AvatarImageURI = null;
    String imageUrl = null;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        profile_image = findViewById(R.id.profile_image);
        // Initialize UI elements
        editTextUsername = findViewById(R.id.editText_username);
        editTextPhone = findViewById(R.id.editText_phone);
        editTextDepartment = findViewById(R.id.editText_department);
        editTextEmail = findViewById(R.id.editText_email);
        editTextYear = findViewById(R.id.editText_year);
        buttonSave = findViewById(R.id.button_save);
        buttonLogout = findViewById(R.id.button_logout);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(Intent.createChooser(intent, "Select Profile Picture"));

            }
        });
        if (user != null) {
            editTextEmail.setText(user.getEmail()); // Display Email
            loadUserProfile(); // Fetch data from Firestore
        }

        // Save Updated Data
        buttonSave.setOnClickListener(v -> saveUserProfile());

        // Logout Button
        buttonLogout.setOnClickListener(v -> {
            Log.d("TAG", "onCreate: Logout");
            firebaseAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, Login_Activity.class));
            finish(); // Close activity
        });
    }

    @SuppressLint("CheckResult")
    private void loadUserProfile() {
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userName = documentSnapshot.getString("username");
                editTextUsername.setText(documentSnapshot.getString("username"));
                editTextPhone.setText(documentSnapshot.getString("phone"));
                editTextDepartment.setText(documentSnapshot.getString("department"));
                editTextYear.setText(documentSnapshot.getString("year"));
//                profile_image.setImageURI(Uri.parse(documentSnapshot.getString("ProfileUrl")));
                Glide.with(ProfileActivity.this).load(documentSnapshot.getString("ProfileUrl")).into(profile_image);
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void saveUserProfile() {
        uploadImageToFirebase(AvatarImageURI, new UploadImageToFirebaseCallback() {
            @Override
            public void onCallback(Boolean IsUploaded) {
                if (IsUploaded) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // Check if user is logged in
                    if (user == null) {
                        Toast.makeText(ProfileActivity.this, "User not logged in!", Toast.LENGTH_SHORT).show();
                        return; // Stop execution to prevent crash
                    }

                    String userId = user.getUid();  // Now it's safe to call getUid()
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Get values from input fields
                    String username = editTextUsername.getText().toString().trim();
                    userName = username;
                    String phone = editTextPhone.getText().toString().trim();
                    String department = editTextDepartment.getText().toString().trim();
                    String email = editTextEmail.getText().toString().trim();
                    String year = editTextYear.getText().toString().trim();

                    // Create a HashMap to store user data
                    Map<String, Object> userProfile = new HashMap<>();
                    userProfile.put("username", username);
                    userProfile.put("phone", phone);
                    userProfile.put("department", department);
                    userProfile.put("email", email);
                    userProfile.put("year", year);
                    userProfile.put("ProfileUrl", imageUrl);


                    // Save data to Firestore
                    db.collection("Users").document(userId)
                            .set(userProfile)
                            .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to update profile" + e.getMessage(), Toast.LENGTH_SHORT).show());

                }
            }
        });

    }


    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        AvatarImageURI = result.getData().getData();
                        // Handle the selected image URI (e.g., display it in an ImageView)
                        profile_image.setImageURI(AvatarImageURI);

                    }
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void uploadImageToFirebase(Uri imageUri, UploadImageToFirebaseCallback uploadImageToFirebaseCallback) {

        if (imageUri != null) {
            // Define the file path and name in Firebase Storage
            String name = "";


            name = userName;

            StorageReference imageRef = firebaseStorage.getReference("UserAvatar").child(name + ".jpg");

            // Start the upload
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL after a successful upload
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // The image is successfully uploaded and you have the download URL
                            imageUrl = downloadUri.toString();
                            uploadImageToFirebaseCallback.onCallback(true);
//                            Toast.makeText(RegisterActivity.this, "Upload successful! URL: " + imageUrl, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the failure
//                    register_progress_bar.setVisibility(View.GONE);
//                    register_btn.setCheckable(true);
                    Toast.makeText(ProfileActivity.this, "User Profile Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
//            register_progress_bar.setVisibility(View.GONE);
//            register_btn.setCheckable(true);
            Toast.makeText(ProfileActivity.this, "Select Profile Picture", Toast.LENGTH_SHORT).show();
        }
    }

    interface UploadImageToFirebaseCallback {
        public void onCallback(Boolean IsUploaded);
    }
}



