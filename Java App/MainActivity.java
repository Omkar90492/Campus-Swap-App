
package com.campusswap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);

        loginButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
