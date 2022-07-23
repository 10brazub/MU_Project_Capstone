package com.example.mu_project_capstone.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mu_project_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class ContractorAvailabilityAdapter extends RecyclerView.Adapter<ContractorAvailabilityAdapter.ViewHolder> {

    private List<Object> contractorAvailabilities;
    private Context context;
    public List<Integer> selectedTimes = new ArrayList<>();

    public ContractorAvailabilityAdapter(List<Object> contractorAvailabilities, Context context) {
        this.contractorAvailabilities = contractorAvailabilities;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFromHourSlot;
        private TextView tvToHourSlot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFromHourSlot = itemView.findViewById(R.id.tvFromHourSlot);
            tvToHourSlot = itemView.findViewById(R.id.tvToHourSlot);

            itemView.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                CardView clickedSlot = view.findViewById(R.id.cvTimeSlot);
                if (clickedSlot.getCardBackgroundColor() == ColorStateList.valueOf(Color.parseColor("#89CFF0"))) {
                    clickedSlot.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    selectedTimes.removeIf(n -> (n == Integer.parseInt(tvFromHourSlot.getText().toString())));
                } else {
                    clickedSlot.setCardBackgroundColor(Color.parseColor("#89CFF0"));
                    selectedTimes.add(Integer.parseInt(tvFromHourSlot.getText().toString()));
                }
            }
        }

        public void bind(Object timeInterval) throws JSONException {
            JSONArray jsonArray = (JSONArray) timeInterval;
            String startHour = jsonArray.get(0).toString();
            String endHour = jsonArray.get(1).toString();

            tvFromHourSlot.setText(startHour);
            tvToHourSlot.setText(endHour);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        CardView slot = view.findViewById(R.id.cvTimeSlot);
        slot.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object timeInterval  = contractorAvailabilities.get(position);
        try {
            holder.bind(timeInterval);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return contractorAvailabilities.size();
    }
}
