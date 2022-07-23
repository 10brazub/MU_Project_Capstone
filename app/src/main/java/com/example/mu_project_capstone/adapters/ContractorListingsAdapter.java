package com.example.mu_project_capstone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mu_project_capstone.models.ContractorListing;
import com.example.mu_project_capstone.R;
import com.example.mu_project_capstone.activities.ItemPostDetailsActivity;
import java.util.List;
import static com.example.mu_project_capstone.keys.ConstantsKeys.*;

public class ContractorListingsAdapter extends RecyclerView.Adapter<ContractorListingsAdapter.ViewHolder> {

    private List<ContractorListing> contractorListings;
    private Context context;

    public ContractorListingsAdapter(List<ContractorListing> contractorListings, Context context) {
        this.contractorListings = contractorListings;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivContractorPicture;
        private TextView tvContractorFirstName;
        private TextView tvContractorLastName;
        private TextView tvContractorDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ivContractorPicture = itemView.findViewById(R.id.ivContractorPicture);
            tvContractorFirstName = itemView.findViewById(R.id.tvContractorFirstName);
            tvContractorLastName = itemView.findViewById(R.id.tvContractorLastName);
            tvContractorDescription = itemView.findViewById(R.id.tvContractorDescription);

            itemView.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ContractorListing contractorListing = contractorListings.get(position);
                Intent contractorDetailIntent = new Intent(context, ItemPostDetailsActivity.class);
                contractorDetailIntent.putExtra(CONTRACTOR_LISTING_DETAILS, contractorListing);
                context.startActivity(contractorDetailIntent);
            }
        }

        public void bind(ContractorListing contractorListing) {
            ivContractorPicture.setImageDrawable(ivContractorPicture.getResources().getDrawable(R.drawable.icons8_user_90));
            tvContractorFirstName.setText(contractorListing.getKeyContractorFirstName());
            tvContractorLastName.setText(contractorListing.getKeyContractorLastName());
            tvContractorDescription.setText(contractorListing.getKeyContractorDescription());
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
