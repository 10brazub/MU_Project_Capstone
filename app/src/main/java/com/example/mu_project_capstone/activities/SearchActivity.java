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
import static com.example.mu_project_capstone.ConstantsKeys.*;

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

    /**
     * Starts the search and assigns a certain score for each contractor using userQuery
     * that will then be sent through multiple functions to calculate based on relevance.
     * The first function is idf(inverse document frequency) to get an idf value which is
     * first found using userQuery which calculates the number of documents(Contractor Job Descriptions)
     * that contain the userQuery. If there are no documents that contain the userQuery all
     * calculation stops to avoid unnecessary computation. Instead a popup text is displayed
     * to the user letting them know that their query yielded no results. However, if there
     * is at least one document that contains userQuery then calculation continues. Documents
     * are then passed through the tf(term frequency) function which calculates a value based
     * on the number of times userQuery appears relative to the size of the description. That
     * tf value is then multiplied with the idf value and a score is the outcome. If the score
     * is greater than 0, the contractor listing is paired with the received score in a hashmap.
     * That hashmap is then passed into the sortMap function to rank the contractor listings based
     * on highest score. The sorted hashmap is then returned to populate the recycler view of the
     * next activity to display the search results.
     * @param userQuery - User input into search bar for the type of service they need
     */
    public void getDescriptionScores(String userQuery) {
        ParseQuery<ContractorListing> contractorDescriptionQuery = ParseQuery.getQuery(ContractorListing.class);
        contractorDescriptionQuery.findInBackground((contractorListingList, e) -> {

            double idfValue = getIdfScore(contractorListingList, userQuery);
            if (idfValue == Double.POSITIVE_INFINITY) {
                LayoutInflater layoutInflater = (LayoutInflater) SearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupWindowView = layoutInflater.inflate(R.layout.popup_window_no_search_results,null);
                popupWindow = new PopupWindow(popupWindowView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(searchLayout, Gravity.CENTER, 0, 0);
                Handler handler = new Handler();
                handler.postDelayed(() -> popupWindow.dismiss(), 3000);
            } else {
                Map<ContractorListing, Double> contractorScoresMap = new HashMap<>();
                for (ContractorListing listing : contractorListingList) {
                    double tfValue = getTfScore(listing, userQuery);
                    double score = getTfIdfScore(tfValue, idfValue);
                    if (score > 0.0) {
                        contractorScoresMap.put(listing, score);
                    }
                }
                Map<ContractorListing, Double> sortedScoresMap = sortMap(contractorScoresMap);
                Intent searchResultsIntent = new Intent(getBaseContext(), SearchResultsActivity.class);
                searchResultsIntent.putExtra(SORTED_SCORES_MAP, (Serializable) sortedScoresMap);
                startActivity(searchResultsIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.no_change);
            }
        });
    }


    private double getIdfScore(List<ContractorListing> contractorListingList, String userQuery) {
        double numOfContractorListings = contractorListingList.size();
        double numDocsContainingQuery = 0;
        String contractorDescription;

        for (ContractorListing listing : contractorListingList) {
            contractorDescription = listing.getKeyContractorDescription();
            List<String> contractorDescriptionTerms = new ArrayList<>(Arrays.asList(contractorDescription.split(" ")));
            for (String word : contractorDescriptionTerms) {
                if (userQuery.equalsIgnoreCase(word)) {
                    numDocsContainingQuery++;
                    break;
                }
            }
        }

        return Math.log(numOfContractorListings / numDocsContainingQuery);
    }

    private double getTfScore(ContractorListing contractorListing, String userQuery) {
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

    private double getTfIdfScore(double tfValue, double idfValue) {
        return tfValue * idfValue;
    }

    private Map<ContractorListing, Double> sortMap(Map<ContractorListing, Double> contractorScoresMap) {

        Map<ContractorListing, Double> sortedMap = contractorScoresMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }
}















