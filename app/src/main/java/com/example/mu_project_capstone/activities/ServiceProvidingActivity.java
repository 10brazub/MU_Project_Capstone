package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mu_project_capstone.ContractorAvailability;
import com.example.mu_project_capstone.ContractorListing;
import com.example.mu_project_capstone.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.example.mu_project_capstone.ParseObjectKeys.*;

public class ServiceProvidingActivity extends AppCompatActivity {

    EditText etConFirstNameSignup;
    EditText etConLastNameSignup;
    EditText etConDescriptionSignup;
    EditText etConZipcodeSignup;
    EditText etConEmailSignup;
    EditText etConPasswordSignup;
    Button btnConSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providing);

        etConFirstNameSignup = findViewById(R.id.etConFirstNameSignup);
        etConLastNameSignup = findViewById(R.id.etConLastNameSIgnup);
        etConDescriptionSignup = findViewById(R.id.etConDescriptionSignup);
        etConZipcodeSignup = findViewById(R.id.etConZipcodeSignup);
        etConEmailSignup = findViewById(R.id.etConEmailSignup);
        etConPasswordSignup = findViewById(R.id.etConPasswordSignup);
        btnConSignup = findViewById(R.id.btnConSignup);

        btnConSignup.setOnClickListener(v -> {
            String firstName = etConFirstNameSignup.getText().toString();
            String lastName = etConLastNameSignup.getText().toString();
            String description = etConDescriptionSignup.getText().toString();
            String zipcode = etConZipcodeSignup.getText().toString();
            String email = etConEmailSignup.getText().toString();
            String password = etConPasswordSignup.getText().toString();

            signupUser(firstName, lastName, description, zipcode, email, password);
        });
    }

    private void signupUser(String firstName, String lastName, String description, String zipcode, String email, String password) {
        ParseUser newUser = new ParseUser();
        ContractorListing contractorListing = new ContractorListing();

        newUser.setUsername(email);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put(ServiceProviderFirstNameKey, firstName);
        newUser.put(ServiceProviderLastNameKey, lastName);
        newUser.put(ServiceProviderZipcodeKey, zipcode);
        newUser.put(ServiceProviderDescriptionKey, description);
        newUser.put(IsServiceSeeker, false);

        contractorListing.setKeyContractorFirstName(firstName);
        contractorListing.setKeyContractorLastName(lastName);
        contractorListing.setKeyContractorDescription(description);
        contractorListing.setKeyContractorZipcode(zipcode);
        contractorListing.setKeyContractorUser(newUser);

        newUser.signUpInBackground(exception -> {
            if (exception == null) {
                contractorListing.saveInBackground(e -> {
                    if (e == null) {
                        Toast.makeText(this, "Listing made", Toast.LENGTH_SHORT).show();
                        initializeContractorAvailability(newUser);
                        goContractorMainActivity();
                    }
                    else {
                        Toast.makeText(this, "Listing failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ServiceProvidingActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeContractorAvailability(ParseUser newUser) {
        ContractorAvailability contractorAvailability = new ContractorAvailability();
        contractorAvailability.setKeyUser(newUser);
        contractorAvailability.saveInBackground();
    }

    private void goContractorMainActivity() {
        Intent contractorMainActivityIntent = new Intent(this, ContractorMainActivity.class);
        startActivity(contractorMainActivityIntent);
        finish();
    }
}