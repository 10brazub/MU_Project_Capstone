package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.parse.ParseUser;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().get(IS_SERVICE_SEEKER).equals(false)) {
                goContractorMainActivity();
            } else {
                goMainActivity();
            }
        }

        etLoginEmail = findViewById(R.id.etEmail);
        etLoginPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String enteredEmail = etLoginEmail.getText().toString();
            String enteredPassword = etLoginPassword.getText().toString();
            loginUser(enteredEmail, enteredPassword);
        });

        tvSignup.setPaintFlags(tvSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSignup.setOnClickListener(v -> {
            tvSignup.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.hyperlink_purple));
            Intent choosePathIntent = new Intent(this, ChoosePathActivity.class);
            startActivity(choosePathIntent);
        });
    }

    private void loginUser(String email, String password) {
        ParseUser.logInInBackground(email, password, (user, exception) -> {
            if (exception != null) {
                Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ParseUser.getCurrentUser().get(IS_SERVICE_SEEKER).equals(false)) {
                goContractorMainActivity();
            } else {
                goMainActivity();
            }
            Toast.makeText(LoginActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
        });
    }

    private void goContractorMainActivity() {
        Intent contractorMainActivityIntent = new Intent(this, ContractorMainActivity.class);
        startActivity(contractorMainActivityIntent);
        finish();
    }

    private void goMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}