package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.parse.ParseUser;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;

public class ServiceSeekingActivity extends AppCompatActivity {

    EditText etFirstNameSignup;
    EditText etLastNameSignup;
    EditText etZipcodeSignup;
    EditText etEmailSignup;
    EditText etPasswordSignup;
    EditText etUserHourlyBudget;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_seeking);

        etFirstNameSignup = findViewById(R.id.etFirstNameSignup);
        etLastNameSignup = findViewById(R.id.etLastNameSignup);
        etZipcodeSignup = findViewById(R.id.etZipcode);
        etEmailSignup = findViewById(R.id.etEmailSignup);
        etPasswordSignup = findViewById(R.id.etPasswordSignup);
        etUserHourlyBudget = findViewById(R.id.etUserHourlyBudget);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            String firstName = etFirstNameSignup.getText().toString();
            String lastName = etLastNameSignup.getText().toString();
            String zipcode = etZipcodeSignup.getText().toString();
            String email = etEmailSignup.getText().toString();
            String password = etPasswordSignup.getText().toString();
            String hourlyBudget = etUserHourlyBudget.getText().toString();

            signupUser(firstName, lastName, zipcode, email, password, hourlyBudget);
        });
    }

    private void signupUser(String firstName, String lastName, String zipcode, String email, String password, String hourlyBudget) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(email);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put(SERVICE_SEEKER_FIRST_NAME_KEY, firstName);
        newUser.put(SERVICE_SEEKER_LAST_NAME_KEY, lastName);
        newUser.put(SERVICE_SEEKER_ZIPCODE_KEY, zipcode);
        newUser.put(IS_SERVICE_SEEKER, true);
        newUser.put(SERVICE_SEEKER_HOURLY_BUDGET_KEY, hourlyBudget);

        newUser.signUpInBackground(exception -> {
            if (exception == null) {
                Toast.makeText(ServiceSeekingActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                goMainActivity();
            } else {
                Toast.makeText(ServiceSeekingActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}