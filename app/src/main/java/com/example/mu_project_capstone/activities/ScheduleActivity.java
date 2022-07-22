package com.example.mu_project_capstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import com.example.mu_project_capstone.ContractorAvailability;
import com.example.mu_project_capstone.ContractorAvailabilityAdapter;
import com.example.mu_project_capstone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;
import static com.example.mu_project_capstone.ParseObjectKeys.*;

public class ScheduleActivity extends AppCompatActivity {

    CalendarView calvScheduler;
    RecyclerView rvTimeSlots;
    ContractorAvailabilityAdapter adapter;
    List<Object> contractorTimes;
    Button btnSubmitTimes;
    ContractorAvailability current;
    String chosenMonth, chosenDay, chosenYear;
    String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ParseUser contractorUser = getIntent().getParcelableExtra("currContractor");

        calvScheduler = findViewById(R.id.calvScheduler);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        contractorTimes = new ArrayList<>();
        adapter = new ContractorAvailabilityAdapter(contractorTimes, getBaseContext());
        rvTimeSlots.setAdapter(adapter);
        rvTimeSlots.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        calvScheduler.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                showContractorAvailability(dayOfWeek, contractorUser);
                chosenMonth = String.valueOf(month + 1);
                chosenDay = String.valueOf(dayOfMonth);
                chosenYear = String.valueOf(year);
                StringJoiner dateString = new StringJoiner("/");
                dateString.add(chosenMonth);
                dateString.add(chosenDay);
                dateString.add(chosenYear);
                chosenDate = dateString.toString();

            }
        });

        btnSubmitTimes = findViewById(R.id.btnSubmitTimes);
        btnSubmitTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( adapter.selectedTimes.size() > 0) {

                    JSONArray currentSundayRequests = current.getSundayRequests();
                    JSONArray selectedTimes = new JSONArray();
                    selectedTimes.put(ParseUser.getCurrentUser().get(ServiceSeekerFirstNameKey));
                    selectedTimes.put(ParseUser.getCurrentUser().get(ServiceSeekerZipcodeKey));
                    selectedTimes.put(chosenDate);
                    for (Integer selectedTime : adapter.selectedTimes) {
                        selectedTimes.put(selectedTime);
                    }
                    currentSundayRequests.put(selectedTimes);
                    current.setSundayRequests(currentSundayRequests);
                    current.saveInBackground();
                    Toast.makeText(ScheduleActivity.this, "Requests Sent!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void showContractorAvailability(int dayOfWeek, ParseUser contractorUser) {

        switch (dayOfWeek) {
            case 1:
                querySundayAvailability(contractorUser);
                break;
            case 2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void querySundayAvailability(ParseUser contractorUser) {
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery("ContractorAvailability");
        query.whereEqualTo("user", contractorUser);
        query.findInBackground(new FindCallback<ContractorAvailability>() {
            @Override
            public void done(List<ContractorAvailability> objects, ParseException e) {
                current = objects.get(0);
                JSONArray array = current.getSundayAvailability();
                contractorTimes.clear();
                for (int i=0; i < array.length(); i++) {
                    try {
                        contractorTimes.add(array.get(i));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}