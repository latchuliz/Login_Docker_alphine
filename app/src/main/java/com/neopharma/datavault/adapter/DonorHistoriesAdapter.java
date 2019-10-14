package com.neopharma.datavault.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.databinding.AdapterDonorHistoryListBinding;

import java.util.List;

public class DonorHistoriesAdapter extends RecyclerView.Adapter<DonorHistoriesAdapter.ViewHolder> {

    private List<FieldTest> list;

    public DonorHistoriesAdapter(List<FieldTest> list) {
        this.list = list;
    }

    @Override
    public DonorHistoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_donor_history_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterDonorHistoryListBinding binding;

        public ViewHolder(AdapterDonorHistoryListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FieldTest test) {
            binding.setVariable(BR.fieldtest, test);
            binding.executePendingBindings();
        }
    }
}
