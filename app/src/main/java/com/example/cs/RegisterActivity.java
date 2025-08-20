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

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edPassword, edEmail, edConfirm;
    TextView tv;
    Button btn;
    FirebaseAuth mAuth;  // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edConfirm = findViewById(R.id.editTextRegComfirmPassword);
        edPassword = findViewById(R.id.editTextRegPassword);
        tv = findViewById(R.id.textViewExistingUser);
        btn = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        tv.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, Login_Activity.class)));

        btn.setOnClickListener(view -> {
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString();
            String confirmPassword = edConfirm.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fill all the above details", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPassword)) {
                    if (isValidPassword(password)) {
                        registerUser(email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long and include letters, digits, and special characters.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser(String email, String password) {
        if (email.contains("@student.mes.ac.in")) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        saveEmail(email);
                        startActivity(new Intent(RegisterActivity.this, Login_Activity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(RegisterActivity.this, "Use College Email Id Only", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEmail(String email) {
        SharedPreferences sp = getSharedPreferences("Emailprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("email", email);
        edit.apply();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasLetter = false, hasDigit = false, hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if ("!@#$%^&*()-_=+[]{};:'\",.<>?/\\|".indexOf(c) >= 0) hasSpecialChar = true;
        }
        return hasLetter && hasDigit && hasSpecialChar;
    }
}
