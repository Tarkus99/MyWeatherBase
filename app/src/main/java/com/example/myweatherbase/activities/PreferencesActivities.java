package com.example.myweatherbase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myweatherbase.R;

public class PreferencesActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SelectCity.activity.initialize();
        finish();
    }
}