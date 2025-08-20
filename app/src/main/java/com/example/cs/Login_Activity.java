package com.example.cs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_Activity extends AppCompatActivity {

    EditText edUsername, edPassword;
    TextView tv;
    Button btn;
    FirebaseAuth mAuth;  // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        tv = findViewById(R.id.textViewNewUser);
        btn = findViewById(R.id.buttonLogin);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
//        try {
//            if (mAuth.getCurrentUser().getUid() != null) {
//                navigateToHome();
//            }
//        } catch (Exception e) {
//            navigateToHome();
//        }
//        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
//        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

//        if (isLoggedIn) {
//            navigateToHome();
//        }

        btn.setOnClickListener(view -> {
            String username = edUsername.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });

        tv.setOnClickListener(view -> startActivity(new Intent(Login_Activity.this, RegisterActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Login_Activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                saveLoginState(email);
                navigateToHome();
            } else {
                Toast.makeText(Login_Activity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginState(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", email);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void navigateToHome() {
        startActivity(new Intent(Login_Activity.this, MainActivity.class));
        finish();
    }
}
