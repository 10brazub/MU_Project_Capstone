package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.fragments.AvailabilityFragment;
import com.example.mu_project_capstone.fragments.ContractorProfileFragment;
import com.example.mu_project_capstone.fragments.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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