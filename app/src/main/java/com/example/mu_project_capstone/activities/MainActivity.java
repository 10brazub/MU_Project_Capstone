package com.example.mu_project_capstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.mu_project_capstone.ContractorListingsAdapter;
import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ContractorListing> contractorListings;
    private ContractorListingsAdapter adapter;
    private RecyclerView rvContractorListings;
    private BottomNavigationView bottomNavigation;
    private CardView cvSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvContractorListings = findViewById(R.id.rvContractorListings);
        contractorListings = new ArrayList<>();
        adapter = new ContractorListingsAdapter(contractorListings, this);
        rvContractorListings.setAdapter(adapter);
        rvContractorListings.setLayoutManager(new LinearLayoutManager(this));
        queryContractorListings();

        cvSearchBar = findViewById(R.id.cvSearchBar);
        cvSearchBar.setOnClickListener(v -> {

            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);

        });

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_home:
                    Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_chat:
                    Toast.makeText(MainActivity.this, "CHAT", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_profile:
                    Toast.makeText(MainActivity.this, "PROFILE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void queryContractorListings() {
        ParseQuery<ContractorListing> query = ParseQuery.getQuery(ContractorListing.class);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                return;
            }
            contractorListings.addAll(objects);
            adapter.notifyDataSetChanged();

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem logoutItem) {

        if (logoutItem.getItemId() == R.id.miLogout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            goLoginActivity();
            return true;
        }
        return super.onOptionsItemSelected(logoutItem);
    }

    private void goLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}