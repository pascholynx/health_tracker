package com.example.healthtrackpro;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class myApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
