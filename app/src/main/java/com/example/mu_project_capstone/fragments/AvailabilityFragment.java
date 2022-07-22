package com.example.mu_project_capstone.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.mu_project_capstone.ContractorAvailability;
import com.example.mu_project_capstone.R;
import com.google.android.libraries.places.internal.zzady;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.example.mu_project_capstone.ParseObjectKeys.*;

public class AvailabilityFragment extends Fragment {

    EditText etFromHour;
    EditText etToHour;
    EditText etFromAmPm;
    EditText etToAmPm;
    Button btnSaveAvailability;
    CardView cvSunday;

    public AvailabilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvSunday = getView().findViewById(R.id.cvSunday);
        cvSunday.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    removeSundayAvailability();
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        etFromHour = getView().findViewById(R.id.etFromHour);
        etToHour = getView().findViewById(R.id.etToHour);
        etFromAmPm = getView().findViewById(R.id.etFromAmPm);
        etToAmPm = getView().findViewById(R.id.etToAmPm);
        btnSaveAvailability = getView().findViewById(R.id.btnSaveAvailability);

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, currentUser);

        btnSaveAvailability.setOnClickListener(v -> {
            String fromHour = etFromHour.getText().toString();
            String toHour = etToHour.getText().toString();
            JSONArray array = new JSONArray();

            if (!fromHour.isEmpty() && !toHour.isEmpty()) {

                int fromHourValue = Integer.parseInt(fromHour);
                int toHourValue = Integer.parseInt(toHour);
                int intervalValue = toHourValue - fromHourValue;

                for (int i=0; i < intervalValue; i++) {
                    List<Integer> newArray = new ArrayList<>();
                    newArray.add(fromHourValue);
                    newArray.add(fromHourValue + 1);
                    array.put(newArray);
                    fromHourValue++;
                }

                query.findInBackground((contractorAvailabilityList, e) -> {
                    for(ContractorAvailability contractorAvailability: contractorAvailabilityList) {
                        contractorAvailability.setSundayAvailability(array);
                        contractorAvailability.saveInBackground();
                        Toast.makeText(getContext(), "SAVED", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void setDate(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, currentUser);
        query.findInBackground((contractorAvailabilityList, e) -> {
            if(contractorAvailabilityList != null) {
                ContractorAvailability current = contractorAvailabilityList.get(0);
                JSONArray array = current.getSundayAvailability();
                if (array == null) {
                    return;
                }
                try {
                    int endIntervalIndex = array.length() - 1;

                    JSONArray startInterval = (JSONArray) array.get(0);
                    String startHour = startInterval.get(0).toString();

                    JSONArray endInterval = (JSONArray) array.get(endIntervalIndex);
                    String endHour = endInterval.get(1).toString();

                    etFromHour.setText(startHour);
                    etToHour.setText(endHour);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }else {
                e.printStackTrace();
            }
        });

    }

    public void removeSundayAvailability(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ContractorAvailability> query = ParseQuery.getQuery(CONTRACTOR_AVAILABILITY_CLASS);
        query.whereEqualTo(CONTRACTOR_AVAILABILITY_USER_KEY, currentUser);
        query.findInBackground((contractorAvailabilities, e) -> {
            ContractorAvailability current = contractorAvailabilities.get(0);
            current.remove(CONTRACTOR_AVAILABILITY_SUNDAY_KEY);
            current.saveInBackground();
            etFromHour.setText("");
            etToHour.setText("");
            Toast.makeText(getContext(), "REMOVED", Toast.LENGTH_SHORT).show();
        });
    }





}