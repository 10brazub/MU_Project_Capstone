package com.example.mu_project_capstone.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mu_project_capstone.models.ContractorAvailability;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.adapters.ServiceRequestsAdapter;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class RequestsFragment extends Fragment {

    RecyclerView rvRequests;
    ServiceRequestsAdapter adapter;
    JSONArray serviceRequests;
    Context context;

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

        context = getContext();
        rvRequests = getView().findViewById(R.id.rvRequests);
        serviceRequests = new JSONArray();
        adapter = new ServiceRequestsAdapter(serviceRequests, context);
        rvRequests.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
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
                Toast.makeText(context, "Request Deleted", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getAdapterPosition();
                serviceRequests.remove(position);
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvRequests);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getAdapterPosition();
                serviceRequests.remove(position);
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(simpleItemTouchCallback1);
        itemTouchHelper1.attachToRecyclerView(rvRequests);
    }

    private void queryServiceRequests() {
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, ParseUser.getCurrentUser());
        query.findInBackground((contractorAvailabilityList, e) -> {
            if (e == null) {
                ContractorAvailability currentContractorAvailability = contractorAvailabilityList.get(0);
                JSONArray arraySundayRequests = currentContractorAvailability.getSundayRequests();
                List<JSONArray> listSundayRequests = jsonToListSundayEntries(arraySundayRequests);
                sortList(listSundayRequests);

                for (int i=0; i < listSundayRequests.size();i++) {
                    serviceRequests.put(listSundayRequests.get(i));
                }

                adapter.notifyDataSetChanged();

//                try {
//                    rankSundayRequests(listSundayRequests);
//                } catch (JSONException ex) {
//                    ex.printStackTrace();
//                }

                // I had planned to take in the list of requests and sort it based on
                // what the service seekers are willing to pay and the amount of hours requested
                // by the users to recommend the requests the contractor should accept based on time
                // and pay.
            }else {
                Toast.makeText(context, "Could Not Retrieve Requests At This Time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private List<JSONArray> jsonToListSundayEntries(JSONArray arraySundayRequests) {
        List<JSONArray> listSundayRequests = new ArrayList<>();

        for (int i=0; i < arraySundayRequests.length(); i++) {
            try {
                JSONArray request = (JSONArray) arraySundayRequests.get(i);
                listSundayRequests.add(request);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        return listSundayRequests;
    }

    private void sortList(List<JSONArray> listSundayRequests) {
        listSundayRequests.sort((a, b) -> {
            try {
                return ((Integer) a.get(a.length() - 1) - (Integer) b.get(b.length() - 1));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return 0;
        });
    }

    private void rankSundayRequests(List<JSONArray> listSundayRequests) throws JSONException {
        Pair<Integer, Integer> startToEndHour;
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int ans = 0;

        for(JSONArray currentRequest : listSundayRequests){
            Integer hoursRequested = currentRequest.length() - 4;
            Integer requesterHourlyBudget = Integer.parseInt(currentRequest.get(1).toString());
            int potentialRequestProfit = hoursRequested * requesterHourlyBudget;

            Integer startHour = (Integer) currentRequest.get(4);
            Integer entryTillStartTime = map.floorKey(startHour);

            int maxProfitTillStartTime = entryTillStartTime == null ? 0 : map.get(entryTillStartTime);
            ans = Math.max(ans, maxProfitTillStartTime + potentialRequestProfit);
            map.put((Integer) currentRequest.get(currentRequest.length() - 1) + 1, ans);
        }
    }

}