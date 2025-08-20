package com.example.cs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // Initialize and set click listeners
        findViewById(R.id.btn_post_item).setOnClickListener(v -> openActivity(PostItemActivity.class));
        findViewById(R.id.btn_browse_listings).setOnClickListener(v -> openActivity(BrowseListingsActivity.class));
        findViewById(R.id.btn_my_listings).setOnClickListener(v -> openActivity(MyListingsActivity.class));
        findViewById(R.id.btn_messages).setOnClickListener(v -> openActivity(MessagesActivity.class));
        findViewById(R.id.btn_profile).setOnClickListener(v -> openActivity(ProfileActivity.class));
//        findViewById(R.id.btn_register).setOnClickListener(v -> openActivity(RegisterActivity.class));
//        findViewById(R.id.btn_login).setOnClickListener(v -> openActivity(Login_Activity.class));


    }

    // Method to open the selected activity
    private void openActivity(Class<?> activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));

    }

}

