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

            String userRequestName = userRequest.get(0).toString();
            String userRequestZipcode = userRequest.get(2).toString();
            String userRequestDate = userRequest.get(3).toString();
            String userRequestTime = "";
            for (int i=4; i < userRequest.length(); i++) {
                int selectedTime = (int) userRequest.get(i);
                String startHour = String.valueOf(selectedTime);
                String endHour = String.valueOf(selectedTime + 1);
                if (i == userRequest.length() - 1) {
                    String timeInterval =  startHour + " - " + endHour;
                    userRequestTime += timeInterval;
                } else {
                    String timeInterval =  startHour + " - " + endHour + ", ";
                    userRequestTime += timeInterval;
                }
            }

            tvRequestName.setText(userRequestName);
            tvRequestZipcode.setText(userRequestZipcode);
            tvRequestDate.setText(userRequestDate);
            tvRequestTime.setText(userRequestTime);
        }
    }
}
