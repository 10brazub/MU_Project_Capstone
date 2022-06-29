package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mu_project_capstone.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

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
        // Creates a new parse user object
        ParseUser newUser = new ParseUser();
        ParseObject contractorListing = ParseObject.create("ContractorListing");

        //Fills new user row with details
        newUser.setUsername(email);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put("firstName", firstName);
        newUser.put("lastName", lastName);
        newUser.put("zipcode", zipcode);
        newUser.put("serviceSeeker", false);

        contractorListing.put("firstName", firstName);
        contractorListing.put("lastName", lastName);
        contractorListing.put("description", description);

        contractorListing.saveInBackground(e -> {
            if (e == null) {
                Toast.makeText(this, "Listing made", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Listing failed", Toast.LENGTH_SHORT).show();
            }

        });

        newUser.signUpInBackground(exception -> {
            if (exception == null) {
                Toast.makeText(ServiceProvidingActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                goMainActivity();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(ServiceProvidingActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}