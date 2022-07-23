package com.example.mu_project_capstone.fragments;

import android.content.Context;
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
import com.example.mu_project_capstone.models.ContractorAvailability;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.adapters.ServiceRequestsAdapter;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;

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
    }

    private void queryServiceRequests() {
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, ParseUser.getCurrentUser());
        query.findInBackground((contractorAvailabilityList, e) -> {
            if (e == null) {
                ContractorAvailability currentContractor = contractorAvailabilityList.get(0);
                JSONArray listRequests = currentContractor.getSundayRequests();

                for (int i=0; i < listRequests.length(); i++) {
                    try {
                        serviceRequests.put(listRequests.get(i));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

                // I had planned to take in the list of requests and sort it based on
                // what the service seekers are willing to pay and the amount of hours requested
                // by the users to recommend the requests the contractor should accept based on time
                // and pay.
                //sortedServiceRequests(serviceRequests);

                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(context, "Could Not Retrieve Requests At This Time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }


    // This function would take each user requests hourly budget and the hours they requested
    // and be put into a container and would be sorted based on the most amount of money a contractor
    // as efficiently as possible
    //
    //sortedServiceRequests(serviceRequests) {
    //  for (int i=0; i < serviceRequests.length(); i++) {
    //          JSONArray userRequestData = (JSONArray) serviceRequests.get(i);
    //          start hour
    //          end hour
    //          int hours = userRequestData.length() - 4;
    //          int userHourlyBudget = Integer.parseInt(userRequestData.get(1).toString());
    //          total profit = hours * userHourlyBudget
    //           jobScheduling(startHour, endHour, profit)
    //    }
    // }
    //public int jobScheduling(startHour, endHour, profit) {
    //        maxJsProfit = 0;
    //        Pair arr = new Pair()
    //        for (int i = 0; i < arr.length; i++) {
    //          arr[i] = new Pair(startTime[i], endTime[i], profit[i]);
    //        }
    //        Arrays.sort(arr,(a,b)->Integer.compare(a,b));
    //        return maxJsProfit;
    // }


}