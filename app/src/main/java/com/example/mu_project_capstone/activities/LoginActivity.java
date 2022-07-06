package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mu_project_capstone.R;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //If there is a user logged in they will be sent to home instead of having to login again
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etLoginEmail = findViewById(R.id.etEmail);
        etLoginPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            Log.i(TAG, "onClick login button");
            String email = etLoginEmail.getText().toString();
            String password = etLoginPassword.getText().toString();
            loginUser(email, password);
        });

        tvSignup.setPaintFlags(tvSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSignup.setOnClickListener(v -> {
            tvSignup.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.hyperlink_purple));
            Intent choosePathIntent = new Intent(this, ChoosePathActivity.class);
            startActivity(choosePathIntent);
        });
    }

    private void loginUser(String email, String password) {
        Log.i(TAG, "Trying to login user " + email);
        ParseUser.logInInBackground(email, password, (user, exception) -> {
            if (exception != null) {
                Log.e(TAG, "issue with login", exception);
                Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
            Toast.makeText(LoginActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
        });
    }

    private void goMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}