package com.example.healthtrackpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextUsername = findViewById(R.id.usernameInput);
        editTextPassword = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.signUpLink);

        // Button click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on Login button click
                loginUser();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on Sign Up link click
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    // Method to handle user login
    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Perform login logic here (e.g., validate input, authenticate user, etc.)
        // For demonstration purposes, display a Toast message
        Toast.makeText(this, "Login Logic Here: \nUsername: " + username +
                "\nPassword: " + password, Toast.LENGTH_LONG).show();
    }
}
