package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.ContractorListingsAdapter;
import com.example.mu_project_capstone.R;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.example.mu_project_capstone.ConstantsKeys.*;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private List<ContractorListing> contractorListings;
    private ContractorListingsAdapter adapter;
    Map<ContractorListing, Double> sortedScoresMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        
        Context context = getBaseContext();
        rvSearchResults = findViewById(R.id.rvSearchResults);
        contractorListings = new ArrayList<>();
        adapter = new ContractorListingsAdapter(contractorListings, SearchResultsActivity.this);
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(context));

        Bundle userQueryExtra = getIntent().getExtras();
        if (userQueryExtra != null) {
            sortedScoresMap = (Map<ContractorListing, Double>) userQueryExtra.get(SORTED_SCORES_MAP);
            populateRecyclerView(sortedScoresMap);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    private void populateRecyclerView(Map<ContractorListing, Double> sortedScoresMap) {
        contractorListings.addAll(sortedScoresMap.keySet());
        adapter.notifyDataSetChanged();
    }
}