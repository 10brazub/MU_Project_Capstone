package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.parse.ParseUser;

public class ServiceSeekingActivity extends AppCompatActivity {

    EditText etFirstNameSignup;
    EditText etLastNameSignup;
    EditText etZipcode;
    EditText etEmailSignup;
    EditText etPasswordSignup;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_seeking);

        etFirstNameSignup = findViewById(R.id.etFirstNameSignup);
        etLastNameSignup = findViewById(R.id.etLastNameSignup);
        etZipcode = findViewById(R.id.etZipcode);
        etEmailSignup = findViewById(R.id.etEmailSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            String firstName = etFirstNameSignup.getText().toString();
            String lastName = etLastNameSignup.getText().toString();
            String zipcode = etZipcode.getText().toString();
            String email = etEmailSignup.getText().toString();
            String password = etPasswordSignup.getText().toString();

            signupUser(firstName, lastName, zipcode, email, password);
        });
    }

    private void signupUser(String firstName, String lastName, String zipcode, String email, String password) {
        // Creates a new parse user object
        ParseUser user = new ParseUser();

        //Fills new user row with details
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("zipcode", zipcode);
        user.put("serviceSeeker", true);

        user.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(ServiceSeekingActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                goMainActivity();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(ServiceSeekingActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}