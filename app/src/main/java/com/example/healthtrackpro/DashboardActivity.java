package com.example.healthtrackpro;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements SensorEventListener {

    private TextView username;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor accelerometerSensor;

    private TextView stepsTextView;
    private TextView distanceTextView;
    private TextView energyTextView;

    private ProgressBar stepProgressBar;
    private ProgressBar distanceProgressBar;
    private ProgressBar energyProgressBar;
    private int stepsCount = 0;

    private double MagnitudePrevious = 0;
    // Constant for an average step length in meters
    private static final float AVERAGE_STEP_LENGTH = 0.76f;

    // Constant for MET (Metabolic Equivalent of Task) value
    private static final float MET_VALUE = 3.5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize the Layout elements
        username = findViewById(R.id.username);
        stepsTextView = findViewById(R.id.stepsTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        energyTextView = findViewById(R.id.energyTextView);

        stepProgressBar = findViewById(R.id.stepProgressBar);
        distanceProgressBar = findViewById(R.id.distanceProgressBar);
        energyProgressBar = findViewById(R.id.energyProgressBar);

        ImageView logoutImageView = findViewById(R.id.imageView3);

        //Log out button
        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the main activity
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        if (currentUser != null) {
            // Get the current user's UID
            String userId = currentUser.getUid();

            // Retrieve the username from Firebase Database using the UID
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve the username and set it in the TextView
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("username").getValue(String.class);
                        username.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }

        //Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepsCount = (int) event.values[0];
            stepsTextView.setText(stepsCount + " / 200 steps ");
            updateDistanceAndEnergy();
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleAccelerometerData(event.values);
        }
    }

    private void handleAccelerometerData(float[] values) {
        // Handle accelerometer data as needed
        // Example: Detect steps based on acceleration changes
        float x_acceleration = values[0];
        float y_acceleration = values[1];
        float z_acceleration = values[2];

        double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
        double MagnitudeDelta = Magnitude - MagnitudePrevious;
        MagnitudePrevious = Magnitude;

        if (MagnitudeDelta > 6){
            stepsCount++;
            stepsTextView.setText(stepsCount + " / 200 steps ");
            updateDistanceAndEnergy();
        }
    }

    private void updateDistanceAndEnergy() {
        // Calculate distance based on step count and average step length
        float distance = stepsCount * AVERAGE_STEP_LENGTH;
        distanceTextView.setText(distance + " meters");

        // Calculate energy expenditure based on MET value
        float energy = stepsCount * MET_VALUE;
        energyTextView.setText(energy + " / 2000 kcal");

        // Define scaling factors for mapping values to progress bar range
        float distanceScale = 0.2f;  
        float energyScale = 0.1f;   
        float stepscale = 0.5f;

        // Map calculated values to progress bar ranges
        int distanceProgress = (int) (distance * distanceScale);
        int energyProgress = (int) (energy * energyScale);
        int stepProgress = (int) (stepsCount * stepscale);

        // Update progress bars
        stepProgressBar.setProgress(stepProgress);
        distanceProgressBar.setProgress(distanceProgress);
        energyProgressBar.setProgress(energyProgress);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            stepsCount = 0;
        }
    }
}

