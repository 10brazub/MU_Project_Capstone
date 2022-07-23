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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.activities.LoginActivity;
import com.parse.ParseUser;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;

public class ContractorProfileFragment extends Fragment {

    ImageView ivContractorPhoto;
    TextView tvProfileFirstName;
    TextView tvProfileLastName;
    TextView tvProfileDescription;
    Button btnProfileLogout;

    public ContractorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contractor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivContractorPhoto = view.findViewById(R.id.ivContractorPhoto);
        tvProfileFirstName = view.findViewById(R.id.tvProfileFirstName);
        tvProfileLastName = view.findViewById(R.id.tvProfileLastName);
        tvProfileDescription = view.findViewById(R.id.tvProfileDescription);
        btnProfileLogout = view.findViewById(R.id.btnProfileLogout);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentUserFirstName = currentUser.get(SERVICE_PROVIDER_FIRST_NAME_KEY).toString();
        String currentUserLastName = currentUser.get(SERVICE_PROVIDER_LAST_NAME_KEY).toString();
        String currentUserDescription = currentUser.get(SERVICE_PROVIDER_DESCRIPTION_KEY).toString();

        ivContractorPhoto.setImageDrawable(getResources().getDrawable(R.drawable.icons8_user_90));
        tvProfileFirstName.setText(currentUserFirstName);
        tvProfileLastName.setText(currentUserLastName);
        tvProfileDescription.setText(currentUserDescription);
        btnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

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