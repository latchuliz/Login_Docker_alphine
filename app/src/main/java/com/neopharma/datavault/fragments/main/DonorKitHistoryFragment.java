package com.neopharma.datavault.fragments.main;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.DonorHistoriesAdapter;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.databinding.FragmentDonorHistoryBinding;
import com.neopharma.datavault.fragments.viewmodel.DonorHistoryViewModel;
import com.neopharma.datavault.model.Constants.FragmentType;

import java.util.ArrayList;

import javax.inject.Inject;

public class DonorKitHistoryFragment extends MainFragment<FragmentDonorHistoryBinding> {

    @Inject
    public DonorHistoryViewModel viewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_donor_history;
    }

    @Override
    public String getToolbarTitle() {
        return FragmentType.DONOR_KIT_HISTORY;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(FragmentType.DONOR_KIT_HISTORY);
        showBackIcon();
        loadHistory();
    }

    private void loadHistory() {
        ArrayList<FieldTest> list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DonorHistoriesAdapter adapter = new DonorHistoriesAdapter(list);

        getViewDataBinding().historyList.setLayoutManager(layoutManager);
        getViewDataBinding().historyList.setAdapter(adapter);

        viewModel.getPendingDonorsLive().observe(this, res -> {
            if (res == null || res.isEmpty()) {
                hideToolBarItem();
                getViewDataBinding().noRecord.setVisibility(View.VISIBLE);
                getViewDataBinding().mainContent.setVisibility(View.GONE);
            } else {
                showToolbarSync();
                getViewDataBinding().mainContent.setVisibility(View.VISIBLE);
                getViewDataBinding().noRecord.setVisibility(View.GONE);
                list.addAll(res);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
