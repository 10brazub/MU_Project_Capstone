package com.example.mu_project_capstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.activities.LoginActivity;
import com.parse.ParseUser;
import static com.example.mu_project_capstone.ParseObjectKeys.*;

public class ProfileFragment extends Fragment {

    TextView tvUserFirstName;
    TextView tvUserLastName;
    TextView tvUserEmail;
    TextView tvUserZipcode;
    Button btnUserLogout;
    ParseUser currentUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserFirstName = view.findViewById(R.id.tvUserFirstName);
        tvUserLastName = view.findViewById(R.id.tvUserLastName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserZipcode = view.findViewById(R.id.tvUserZipcode);
        btnUserLogout = view.findViewById(R.id.btnUserLogout);

        currentUser = ParseUser.getCurrentUser();
        String currentUserFirstName = currentUser.get(SERVICE_SEEKER_FIRST_NAME_KEY).toString();
        String currentUserLastName = currentUser.get(SERVICE_SEEKER_LAST_NAME_KEY).toString();
        String currentUserEmail = currentUser.getEmail();
        String currentUserZipcode = currentUser.get(SERVICE_PROVIDER_ZIPCODE_KEY).toString();

        tvUserFirstName.setText(currentUserFirstName);
        tvUserLastName.setText(currentUserLastName);
        tvUserEmail.setText(currentUserEmail);
        tvUserZipcode.setText(currentUserZipcode);
        
        btnUserLogout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        ParseUser.logOut();
        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
        goLoginActivity();
    }

    private void goLoginActivity() {
        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();
    }
}