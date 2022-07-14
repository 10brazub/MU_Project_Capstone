package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.mu_project_capstone.R;

public class SearchActivity extends AppCompatActivity {

    EditText etUserSearchInput;
    EditText etUserZipcodeInput;
    Button btnUserSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        etUserSearchInput = findViewById(R.id.etUserSeachInput);
        etUserZipcodeInput = findViewById(R.id.etUserZipcodeInput);
        btnUserSearch = findViewById(R.id.btnUserSearch);

        btnUserSearch.setOnClickListener(v -> {
            String userQuery = etUserSearchInput.getText().toString();
            Intent searchResultsIntent = new Intent(getBaseContext(), SearchResultsActivity.class);
            searchResultsIntent.putExtra("userQueryExtra", userQuery);
            startActivity(searchResultsIntent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.no_change);

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_up);
    }

}















