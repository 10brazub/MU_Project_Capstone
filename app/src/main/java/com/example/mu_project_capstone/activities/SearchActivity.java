package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;
import com.example.mu_project_capstone.R;

public class SearchActivity extends AppCompatActivity {

    EditText etUserSearchInput;
    EditText etUserZipcodeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etUserSearchInput = findViewById(R.id.etUserSeachInput);
        etUserZipcodeInput = findViewById(R.id.etUserZipcodeInput);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}