package com.example.mu_project_capstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.fragments.HomeFragment;
import com.example.mu_project_capstone.fragments.MapFragment;
import com.example.mu_project_capstone.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            switch (item.getItemId()){
                case R.id.action_map:
                    selectedFragment = new MapFragment();
                    break;
                case R.id.action_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                default:
                    selectedFragment = new HomeFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, selectedFragment).commit();
            return true;
        });
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }
}