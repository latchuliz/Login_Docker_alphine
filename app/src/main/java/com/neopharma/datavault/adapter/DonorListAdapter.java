package com.neopharma.datavault.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.AdapterDonorTaskListBinding;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.utility.Store;

import java.util.List;

public class DonorListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Donor> donors;
    private ViewDonorClick viewDonorClick;
    List<String> takenFieldTestDonors;

    public DonorListAdapter(Context mContext, List<Donor> donorData, List<String> takenFieldTestDonors) {
        this.context = mContext;
        this.donors = donorData;
        this.takenFieldTestDonors = takenFieldTestDonors;
    }

    public void setOnClickListener(ViewDonorClick viewDonorClick) {
        this.viewDonorClick = viewDonorClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_donor_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = ((ViewHolder) viewHolder);

        final Donor donor = donors.get(position);
        holder.bind(donor);
    }

    @Override
    public int getItemCount() {
        return donors.size();
    }

    public interface ViewDonorClick {
        void onDonorClickListener(View view, Donor donor);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterDonorTaskListBinding binding;

        public ViewHolder(AdapterDonorTaskListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Donor donor) {
            switch (donor.status) {
                case Constants.DonorStatus.NEW:
                    binding.getStart.setText(R.string.temp_get_start);
                    break;
                case Constants.DonorStatus.DELETED:
                    binding.getStart.setText(R.string.temp_deleted);
                    break;
                default:
                    binding.getStart.setText(R.string.temp_complete);
            }

            binding.getStart.setOnClickListener(view -> {
                if (Constants.DonorStatus.NEW.equalsIgnoreCase(donor.status) &&
                        !binding.getStart.getText().toString().equalsIgnoreCase(context.getString(R.string.temp_complete))) {
                    viewDonorClick.onDonorClickListener(view, donor);
                }
            });

            if (!takenFieldTestDonors.isEmpty() && takenFieldTestDonors.contains(donor.id)) {
                binding.getStart.setText(R.string.temp_complete);
            }

            if (Store.isAllDonor) {
                binding.taskName.setVisibility(View.VISIBLE);
            }
            binding.setVariable(BR.donorData, donor);
            binding.executePendingBindings();
        }
    }
}
