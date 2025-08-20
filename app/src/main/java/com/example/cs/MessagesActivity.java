package com.example.cs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private List<Message> messageList;
    private FloatingActionButton fabNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_messages);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize FloatingActionButton
        fabNewMessage = findViewById(R.id.fab_new_message);
        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle new message action (e.g., open a new chat screen)
                Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        // Sample Data (Replace with actual messages from database)
        messageList = new ArrayList<>();
        messageList.add(new Message("user1_id", "user2_id", "John Doe", "Hey! Is the book still available?", "10:30 AM"));
        messageList.add(new Message("user2_id", "user1_id", "Alice", "I'm interested in your laptop.", "9:45 AM"));


        // Set Adapter
        adapter = new MessagesAdapter(this, messageList);
        recyclerView.setAdapter(adapter);
    }
}
