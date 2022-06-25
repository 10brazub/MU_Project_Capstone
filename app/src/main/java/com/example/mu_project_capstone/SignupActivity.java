package com.example.mu_project_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    EditText etSignupFirstName;
    EditText etSignupLastName;
    EditText etSignupEmail;
    EditText etSignupPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etSignupFirstName = findViewById(R.id.etSignupFirstName);
        etSignupLastName = findViewById(R.id.etSignupLastName);
        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            String firstName = etSignupFirstName.getText().toString();
            String lastName = etSignupLastName.getText().toString();
            String email = etSignupEmail.getText().toString();
            String password = etSignupPassword.getText().toString();

            signupUser(firstName, lastName, email, password);
        });
    }

    private void signupUser(String firstName, String lastName, String email, String password) {
        // Creates a new parse user object
        ParseUser user = new ParseUser();

        //Fills new user row with details
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        user.signUpInBackground(e -> {
            if (e == null) {
                // Hooray! Let them use the app now.
                Toast.makeText(SignupActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                goChoosePathActivity();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e(TAG, "Sign up failed", e);
                Toast.makeText(SignupActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goChoosePathActivity() {
        Intent i = new Intent(this, ChoosePathActivity.class);
        startActivity(i);
        finish();
    }

//    private void goMainActivity() {
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//        finish();
//    }
}