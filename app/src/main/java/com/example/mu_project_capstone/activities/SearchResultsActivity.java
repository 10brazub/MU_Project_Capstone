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

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private List<ContractorListing> contractorListings;
    private ContractorListingsAdapter adapter;
    String userQuery;

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
            userQuery = userQueryExtra.getString("userQueryExtra");
            getDescriptionScores(userQuery);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }
    
    public void getDescriptionScores(String userQuery) {
        ParseQuery<ContractorListing> contractorDescriptionQuery = ParseQuery.getQuery(ContractorListing.class);
        contractorDescriptionQuery.findInBackground((objects, e) -> {

            double idfValue = idf(objects, userQuery);

            Map<ContractorListing, Double> rankingMap = new HashMap<>();

            for (ContractorListing listing : objects) {
                double tfValue = tf(listing, userQuery);
                double score = tfIdfScore(tfValue, idfValue);
                if (score > 0.0) {
                    rankingMap.put(listing, score);
                }
            }
            Map<ContractorListing, Double> sortedMap = sortMap(rankingMap);
            populateRecyclerView(sortedMap);
        });
    }

    private double idf(List<ContractorListing> objects, String userQuery) {
        double numOfContractorListings = objects.size();
        double numDocsWithQuery = 0;
        String contractorDescription;

        for (ContractorListing listing : objects) {
            contractorDescription = listing.getKeyContractorDescription();
            List<String> contractorDescriptionTerms = new ArrayList<>(Arrays.asList(contractorDescription.split(" ")));

            for (String word : contractorDescriptionTerms) {
                if (userQuery.equalsIgnoreCase(word)) {
                    numDocsWithQuery++;
                    break;
                }
            }
        }

        return Math.log(numOfContractorListings / numDocsWithQuery);
    }

    private double tf(ContractorListing contractorListing, String userQuery) {
        String contractorDescription = contractorListing.getKeyContractorDescription();
        List<String> contractorDescriptionTerms = new ArrayList<>(Arrays.asList(contractorDescription.split(" ")));
        double queryOccurrences = 0;
        double contractorDescriptionSize = contractorDescriptionTerms.size();

        for (String word : contractorDescriptionTerms) {
            if (userQuery.equalsIgnoreCase(word)) {
                queryOccurrences++;
            }
        }

        return queryOccurrences / contractorDescriptionSize;
    }

    private double tfIdfScore(double tfValue, double idfValue) {
        return tfValue * idfValue;
    }

    private Map<ContractorListing, Double> sortMap(Map<ContractorListing, Double> rankingMap) {

        Map<ContractorListing, Double> sortedMap = rankingMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }

    private void populateRecyclerView(Map<ContractorListing, Double> sortedMap) {
        contractorListings.addAll(sortedMap.keySet());
        adapter.notifyDataSetChanged();
    }



}