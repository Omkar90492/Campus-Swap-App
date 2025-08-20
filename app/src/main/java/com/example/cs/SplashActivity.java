package com.example.cs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    String user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        try {
            user = firebaseAuth.getCurrentUser().getUid();
        } catch (NullPointerException exception) {
            firebaseAuth.signOut();
            user = null;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(user != null) {
                   startActivity(new Intent(SplashActivity.this, MainActivity.class));
                   finish();
               }else {
                   startActivity(new Intent(SplashActivity.this, Login_Activity.class));
               }
            }
        }, 3000);
    }


}