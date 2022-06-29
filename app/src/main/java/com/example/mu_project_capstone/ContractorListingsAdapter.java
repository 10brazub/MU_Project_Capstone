package com.example.mu_project_capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContractorListingsAdapter extends RecyclerView.Adapter<ContractorListingsAdapter.ViewHolder> {

    private List<ContractorListing> contractorListings;
    private Context context;


    public ContractorListingsAdapter(List<ContractorListing> contractorListings, Context context) {
        this.contractorListings= contractorListings;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContractorFirstName;
        private TextView tvContractorLastName;

        public ViewHolder (View itemView) {
            super(itemView);

            tvContractorFirstName = itemView.findViewById(R.id.tvContractorFirstName);
            tvContractorLastName = itemView.findViewById(R.id.tvContractorLastName);
        }

        public void bind(ContractorListing contractorListing) {
            tvContractorFirstName.setText(contractorListing.getKeyContractorFirstName());
            tvContractorLastName.setText(contractorListing.getKeyContractorLastName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_contractor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ContractorListing contractorListing = contractorListings.get(position);
        holder.bind(contractorListing);

    }

    @Override
    public int getItemCount() {
        return contractorListings.size();
    }
}
