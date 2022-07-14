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

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_up);
    }

//
//    public void getContractorDescriptions(String userQuery) {
//        ParseQuery<ParseObject> contractorDescriptionQuery = new ParseQuery<>("ContractorListing");
//        contractorDescriptionQuery.findInBackground((listObjects, e) -> {
//            if (e != null) {
//                e.printStackTrace();
//            } else {
//
//                Map<String, Double> mapContractorID = new HashMap<>();
//
//                for (ParseObject contractorListing : listObjects) {
//                    String contractorDescription = contractorListing.get(ServiceProviderDescriptionKey).toString();
//
//                    List<String> contractorDescriptionList = new ArrayList<>(Arrays.asList(contractorDescription.split(" ")));
//                    allContractorDescriptions.add(contractorDescriptionList);
//                }
//
//                double allContractorDescriptionSize = allContractorDescriptions.size();
//
//
//                String objectID;
//                for (ParseObject contractorListing : listObjects) {
//                    objectID = contractorListing.getObjectId();
//                    String contractorDescription = contractorListing.get(ServiceProviderDescriptionKey).toString();
//                    List<String> contractorDescriptionList = new ArrayList<>(Arrays.asList(contractorDescription.split(" ")));
//                    double score = tfidfCalculation(contractorDescriptionList, allContractorDescriptions, userQuery, allContractorDescriptionSize);
//
//                    if (score > 0.0) {
//                        mapContractorID.put(objectID, score);
//                    }
//
//                }
//                Log.i("MAP", "getContractorDescriptions: " + mapContractorID);
//
////                for (List<String> description: allContractorDescriptions){
////                    double score = tfidfCalculation(description, allContractorDescriptions, userQuery, allContractorDescriptionSize);
////                    Log.i("SCORE", "getContractorDescriptions: " + description + " " + score);
////                }
//
//            }
//        });
//    }

//    public double termFrequency(List<String> contractorDescription, String userQuery) {
//        double queryInstances = 0;
//        double contractorDescriptionSize = contractorDescription.size();
//
//        for (String wordInDescription : contractorDescription) {
//            if (userQuery.equalsIgnoreCase(wordInDescription)) {
//                queryInstances++;
//            }
//        }
//
//        double termFrequency = queryInstances / contractorDescriptionSize;
//        return termFrequency;
//    }
//
//    public double inverseDocumentFrequency(List<List<String>> allContractorDescriptions, String userQuery, double size) {
//        double numDescripContainingQuery = 0;
//
//        for (List<String> contractorDescription : allContractorDescriptions) {
//            for (String wordInDescription : contractorDescription) {
//                if (userQuery.equalsIgnoreCase(wordInDescription)) {
//                    numDescripContainingQuery++;
//                    break;
//                }
//            }
//        }
//        return Math.log(size / numDescripContainingQuery);
//    }
//
//    public double tfidfCalculation(List<String> contractorDescription, List<List<String>> allContractorDescriptions, String userQuery, double size) {
//        double contractorDescriptionScore = termFrequency(contractorDescription, userQuery) * inverseDocumentFrequency(allContractorDescriptions, userQuery, size);
//        return contractorDescriptionScore;
//    }

}















