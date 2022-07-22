package com.example.mu_project_capstone.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mu_project_capstone.ContractorAvailability;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.ServiceRequestsAdapter;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;
import static com.example.mu_project_capstone.ParseObjectKeys.*;

public class RequestsFragment extends Fragment {

    RecyclerView rvRequests;
    ServiceRequestsAdapter adapter;
    JSONArray serviceRequests;

    public RequestsFragment() {
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
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRequests = getView().findViewById(R.id.rvRequests);
        serviceRequests = new JSONArray();
        adapter = new ServiceRequestsAdapter(serviceRequests, getContext());
        rvRequests.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvRequests.setLayoutManager(linearLayoutManager);
        queryServiceRequests();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getAdapterPosition();
                serviceRequests.remove(position);
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvRequests);
    }

    private void queryServiceRequests() {
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, ParseUser.getCurrentUser());
        query.findInBackground((contractorAvailabilityList, e) -> {
            if (e == null) {
                ContractorAvailability currentCon = contractorAvailabilityList.get(0);
                JSONArray listRequests = currentCon.getSundayRequests();

                for (int i=0; i < listRequests.length(); i++) {
                    try {
                        serviceRequests.put(listRequests.get(i));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getContext(), "Could Not Retrieve Requests At This Time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}