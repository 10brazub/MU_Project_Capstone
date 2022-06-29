package com.example.mu_project_capstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private List<ContractorListing> contractorListings;
    private ContractorListingsAdapter adapter;
    private RecyclerView rvContractorListings;

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
    }

    private void queryContractorListings() {
        ParseQuery<ContractorListing> query = ParseQuery.getQuery(ContractorListing.class);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Log.e(TAG, "query went wrong", e);
                return;
            }

            for (ContractorListing contractorListingItem: objects) {
                Log.i(TAG, "First Name: " + contractorListingItem.getKeyContractorFirstName());
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