package com.example.mu_project_capstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.fragments.AvailabilityFragment;
import com.example.mu_project_capstone.fragments.ContractorProfileFragment;
import com.example.mu_project_capstone.fragments.HomeFragment;
import com.example.mu_project_capstone.fragments.MapFragment;
import com.example.mu_project_capstone.fragments.ProfileFragment;
import com.example.mu_project_capstone.fragments.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class ContractorMainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_main);

        BottomNavigationView contractorBottomNavigation = findViewById(R.id.contractorBottomNavigation);
        contractorBottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            switch (item.getItemId()){
                case R.id.action_availability:
                    selectedFragment = new AvailabilityFragment();
                    break;
                case R.id.action_requests:
                    selectedFragment = new RequestsFragment();
                    break;
                default:
                    selectedFragment = new ContractorProfileFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContractorContainer, selectedFragment).commit();
            return true;
        });
        contractorBottomNavigation.setSelectedItemId(R.id.action_profile);
    }
}