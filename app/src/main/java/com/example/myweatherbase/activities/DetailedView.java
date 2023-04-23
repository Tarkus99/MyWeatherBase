package com.example.myweatherbase.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherbase.R;

public class DetailedView extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        List l = (List) getIntent().getExtras().getSerializable("list");

        textView = findViewById(R.id.textView);
        textView.setText(l.clouds.toString());
    }
}