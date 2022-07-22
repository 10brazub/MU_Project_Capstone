package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    EditText etUserSearchInput;
    EditText etUserZipcodeInput;
    Button btnUserSearch;
    PopupWindow popupWindow;
    ConstraintLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchLayout = findViewById(R.id.searchLayout);

        etUserSearchInput = findViewById(R.id.etUserSeachInput);
        etUserZipcodeInput = findViewById(R.id.etUserZipcodeInput);
        btnUserSearch = findViewById(R.id.btnUserSearch);
        etUserSearchInput.requestFocus();

        btnUserSearch.setOnClickListener(v -> {
            String userQuery = etUserSearchInput.getText().toString();
            getDescriptionScores(userQuery);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_up);
    }

    public void getDescriptionScores(String userQuery) {
        ParseQuery<ContractorListing> contractorDescriptionQuery = ParseQuery.getQuery(ContractorListing.class);
        contractorDescriptionQuery.findInBackground((objects, e) -> {

            double idfValue = idf(objects, userQuery);
            if (idfValue == Double.POSITIVE_INFINITY) {
                LayoutInflater layoutInflater = (LayoutInflater) SearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup_window_no_search_results,null);
                popupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(searchLayout, Gravity.CENTER, 0, 0);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        popupWindow.dismiss();
                    }
                }, 3000);

            } else {
                Map<ContractorListing, Double> rankingMap = new HashMap<>();

                for (ContractorListing listing : objects) {
                    double tfValue = tf(listing, userQuery);
                    double score = tfIdfScore(tfValue, idfValue);
                    if (score > 0.0) {
                        rankingMap.put(listing, score);
                    }
                }
                Map<ContractorListing, Double> sortedMap = sortMap(rankingMap);

                Intent searchResultsIntent = new Intent(getBaseContext(), SearchResultsActivity.class);
                searchResultsIntent.putExtra("userQueryResults", (Serializable) sortedMap);
                startActivity(searchResultsIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.no_change);
            }
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



}















