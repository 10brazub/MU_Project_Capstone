package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.mu_project_capstone.R;

public class ChoosePathActivity extends AppCompatActivity {

    Button btnServiceSeeking;
    Button btnServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_path);

        btnServiceSeeking = findViewById(R.id.btnServiceSeeking);
        btnServiceProvider = findViewById(R.id.btnServiceProvider);

        btnServiceSeeking.setOnClickListener(v -> {
            Intent i = new Intent(this, ServiceSeekingActivity.class);
            startActivity(i);
        });

        btnServiceProvider.setOnClickListener(v -> {
            Intent i = new Intent(this, ServiceProvidingActivity.class);
            startActivity(i);
        });
    }
}