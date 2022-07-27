package com.example.mu_project_capstone.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.mu_project_capstone.models.ContractorListing;
import com.example.mu_project_capstone.adapters.ContractorListingsAdapter;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.activities.SearchActivity;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CardView cvSearchBar;
    private RecyclerView rvContractorListings;
    private List<ContractorListing> contractorListings;
    private ContractorListingsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        Activity HomeFragmentActivity = getActivity();

        rvContractorListings = getView().findViewById(R.id.rvContractorListings);
        contractorListings = new ArrayList<>();
        adapter = new ContractorListingsAdapter(contractorListings, context);
        rvContractorListings.setAdapter(adapter);
        rvContractorListings.setLayoutManager(new LinearLayoutManager(context));
        queryContractorListings();

        cvSearchBar = getView().findViewById(R.id.cvSearchBar);
        cvSearchBar.setOnClickListener(v -> {
            Intent searchIntent = new Intent(context, SearchActivity.class);
            startActivity(searchIntent);
            HomeFragmentActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.no_change);
        });
    }

    private void queryContractorListings() {
        ParseQuery<ContractorListing> query = ParseQuery.getQuery(ContractorListing.class);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                return;
            }
            contractorListings.addAll(objects);
            adapter.notifyDataSetChanged();
        });
    }
}