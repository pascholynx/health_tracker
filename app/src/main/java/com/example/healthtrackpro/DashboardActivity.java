package com.example.healthtrackpro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    private TextView stepsCountTextView;
    private TextView bloodPressureTextView;
    private TextView heartRateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize TextViews
        stepsCountTextView = findViewById(R.id.stepsCountTextView);
        bloodPressureTextView = findViewById(R.id.bloodPressureTextView);
        heartRateTextView = findViewById(R.id.heartRateTextView);

        // Retrieve and display data (Replace with your actual data retrieval logic)
        int stepsCount = getStepsCountFromSensor(); // Replace this with actual step count retrieval
        String bloodPressure = getBloodPressureData(); // Replace this with actual blood pressure retrieval
        String heartRate = getHeartRateData(); // Replace this with actual heart rate retrieval

        // Update TextViews with retrieved data
        stepsCountTextView.setText(String.valueOf(stepsCount));
        bloodPressureTextView.setText(bloodPressure);
        heartRateTextView.setText(heartRate);
    }

    // Methods to retrieve data - Replace these with your actual data retrieval methods
    private int getStepsCountFromSensor() {
        // Simulated step count for demonstration
        return 5000;
    }

    private String getBloodPressureData() {
        // Simulated blood pressure for demonstration
        return "120/80 mmHg";
    }

    private String getHeartRateData() {
        // Simulated heart rate for demonstration
        return "70 bpm";
    }
}