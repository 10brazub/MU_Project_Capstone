package com.example.mu_project_capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mu_project_capstone.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.example.mu_project_capstone.keys.ConstantsKeys.*;

public class ServiceRequestsAdapter extends RecyclerView.Adapter<ServiceRequestsAdapter.ViewHolder> {

    private Context context;
    private JSONArray serviceRequests;

    public ServiceRequestsAdapter(JSONArray serviceRequests, Context context) {
        this.serviceRequests = serviceRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONArray userRequest = (JSONArray) serviceRequests.get(position);
            holder.bind(userRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return serviceRequests.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRequestName;
        private TextView tvRequestZipcode;
        private TextView tvRequestDate;
        private TextView tvRequestTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRequestName = itemView.findViewById(R.id.tvRequestName);
            tvRequestZipcode = itemView.findViewById(R.id.tvRequestZipcode);
            tvRequestDate = itemView.findViewById(R.id.tvRequestDate);
            tvRequestTime = itemView.findViewById(R.id.tvRequestTime);
        }

        public void bind(JSONArray userRequest) throws JSONException {

            String userRequestName = userRequest.getString(USER_REQUEST_NAME_INDEX);
            String userRequestZipcode = userRequest.getString(USER_REQUEST_ZIPCODE_INDEX);
            String userRequestDate = userRequest.getString(USER_REQUEST_DATE_INDEX);
            String userRequestStartHour = userRequest.get(USER_REQUEST_START_HOUR_INDEX).toString();

            int userRequestLastIdx = userRequest.length() - 1;
            int endHourIntervalValue = (Integer) userRequest.get(userRequestLastIdx) + 1;
            String userRequestEndHour = String.valueOf(endHourIntervalValue);
            String userRequestTimeInterval = userRequestStartHour + "-" + userRequestEndHour;

            tvRequestName.setText(userRequestName);
            tvRequestZipcode.setText(userRequestZipcode);
            tvRequestDate.setText(userRequestDate);
            tvRequestTime.setText(userRequestTimeInterval);
        }
    }
}
