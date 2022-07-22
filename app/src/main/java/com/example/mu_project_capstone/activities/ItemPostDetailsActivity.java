package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.R;
import com.parse.ParseUser;
import static com.example.mu_project_capstone.ConstantsKeys.*;

public class ItemPostDetailsActivity extends AppCompatActivity {

    TextView tvConFirstNameDetail;
    TextView tvConLastNameDetail;
    TextView tvConDescriptionDetail;
    Button btnSchedule;
    ContractorListing contractorListing;
    ParseUser chosenContractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_post_details);

        tvConFirstNameDetail = findViewById(R.id.tvConFirstNameDetail);
        tvConLastNameDetail = findViewById(R.id.tvConLastNameDetail);
        tvConDescriptionDetail = findViewById(R.id.tvConDescriptionDetail);
        btnSchedule = findViewById(R.id.btnSchedule);

        //handle for null
        contractorListing = getIntent().getParcelableExtra(CONTRACTOR_LISTING_DETAILS);
        if (contractorListing != null) {
            tvConFirstNameDetail.setText(contractorListing.getKeyContractorFirstName());
            tvConLastNameDetail.setText(contractorListing.getKeyContractorLastName());
            tvConDescriptionDetail.setText(contractorListing.getKeyContractorDescription());
            chosenContractor = contractorListing.getKeyContractorUser();
        }

        btnSchedule.setOnClickListener(v -> {
            Intent scheduleActivityIntent = new Intent(this, ScheduleActivity.class);
            scheduleActivityIntent.putExtra(CONTRACTOR_LISTING_USER, chosenContractor);
            startActivity(scheduleActivityIntent);
        });
    }
}