package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.mu_project_capstone.R;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";
    SearchView svUserQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        svUserQuery = findViewById(R.id.svUserQuery);
        svUserQuery.requestFocus();


        svUserQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }
}