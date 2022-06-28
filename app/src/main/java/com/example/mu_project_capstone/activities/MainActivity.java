package com.example.mu_project_capstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem logoutItem) {

        if (logoutItem.getItemId() == R.id.miLogout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            goLoginActivity();
            return true;
        }
        return super.onOptionsItemSelected(logoutItem);
    }

    private void goLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}