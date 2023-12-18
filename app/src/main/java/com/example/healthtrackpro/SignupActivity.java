package com.example.healthtrackpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button getStartedButton;
    private TextView logInLink;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        editTextUsername = findViewById(R.id.editTextText);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        getStartedButton = findViewById(R.id.button);
        logInLink = findViewById(R.id.logInLink);

        // Button click listeners
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        logInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void signUpUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmailAddress.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserDetails(user.getUid(), username, email);
                            Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to LoginActivity after successful signup
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(SignupActivity.this, "Sign up failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveUserDetails(String userId, String username, String email) {
        // Create a User object with username and email
        User newUser = new User(username, email);

        // Get reference to the 'users' node in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Save the user details under the userId in the 'users' node
        usersRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User details saved successfully
                        Toast.makeText(SignupActivity.this, "User details saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed to save user details
                        Toast.makeText(SignupActivity.this, "Failed to save user details. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
