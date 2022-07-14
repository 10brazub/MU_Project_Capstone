package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.R;

public class ItemPostDetailsActivity extends AppCompatActivity {

    TextView tvConFirstNameDetail;
    TextView tvConLastNameDetail;
    TextView tvConDescriptionDetail;
    ContractorListing contractorListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_post_details);

        tvConFirstNameDetail = findViewById(R.id.tvConFirstNameDetail);
        tvConLastNameDetail = findViewById(R.id.tvConLastNameDetail);
        tvConDescriptionDetail = findViewById(R.id.tvConDescriptionDetail);

        //handle for null
        contractorListing = getIntent().getParcelableExtra("ContractorListingDetails");
        if (contractorListing != null) {

            tvConFirstNameDetail.setText(contractorListing.getKeyContractorFirstName());
            tvConLastNameDetail.setText(contractorListing.getKeyContractorLastName());
            tvConDescriptionDetail.setText(contractorListing.getKeyContractorDescription());

        }
    }
}