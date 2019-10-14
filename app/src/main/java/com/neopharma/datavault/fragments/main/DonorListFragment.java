package com.neopharma.datavault.fragments.main;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.DonorListAdapter;
import com.neopharma.datavault.databinding.FragmentDonorListBinding;
import com.neopharma.datavault.fragments.viewmodel.DonorListViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.model.donor.DonorTask;
import com.neopharma.datavault.utility.Store;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DonorListFragment extends MainFragment<FragmentDonorListBinding> {

    @Inject
    public DonorListViewModel viewModel;
    private List<Donor> donors = new ArrayList<>();
    private List<String> takenFieldTestDonors = new ArrayList<>();
    private DonorListAdapter adapter;

    @Override
    protected String getToolbarTitle() {
        return FragmentType.DIGITAL;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_donor_list;
    }

    private void setUpTask() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        getViewDataBinding().donorTaskList.setLayoutManager(layoutManager);
        adapter = new DonorListAdapter(getActivity(), donors, takenFieldTestDonors);
        getViewDataBinding().donorTaskList.setAdapter(adapter);

        adapter.setOnClickListener((view, donor) -> {
            if (viewModel.findPendingEqualDonor(donor.id) != null) {
                activity.alertDialog(Constants.ALREADY_USED_DONOR_ALERT, Constants.OKAY);
            } else if (viewModel.haveIncomplete() && viewModel.haveIncompleteTest().getDonorId() == null) {
                activity.alertDialog(Constants.PENDING_ALERT, Constants.OKAY);
            } else if (viewModel.haveIncomplete() && viewModel.haveIncompleteTest().getDonorId() != null && !donor.id.equals(viewModel.haveIncompleteTest().getDonorId())) {
                activity.alertDialog(Constants.PENDING_ALERT, Constants.OKAY);
            } else {
                if (viewModel.haveIncomplete() && viewModel.haveIncompleteTest().getDonorId() != null && donor.id.equals(viewModel.haveIncompleteTest().getDonorId())) {
                    activity.actionDashBoard_to_FieldTestFragment(null, false);
                    return;
                }

                if (getArguments() != null) {
                    donor.task = new DonorTask();
                    donor.task.taskId = getArguments().getString(Constants.TASK);
                    donor.task.taskName = getArguments().getString(Constants.TASK_NAME);
                }
                Bundle b = new Bundle();
                b.putString(Constants.DONOR, new Gson().toJson(donor));
                activity.actionDashBoard_to_FieldTestFragment(b, false);
            }
        });
    }

    public void setVisibilityLayout() {
        if (donors.isEmpty()) {
            getViewDataBinding().noDonor.setVisibility(View.VISIBLE);
            getViewDataBinding().donorLyt.setVisibility(View.GONE);
        } else {
            getViewDataBinding().noDonor.setVisibility(View.GONE);
            getViewDataBinding().donorLyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(FragmentType.TASKS);
        hideToolBarItem();
        showBackIcon();
        setUpTask();
        activity.showProgressBar();
        getViewDataBinding().taskName.setVisibility(getArguments() != null ? View.GONE : View.VISIBLE);
        if (getArguments() != null) {
            Store.isAllDonor = false;
            viewModel.getDonors(getArguments().getString(Constants.TASK), getArguments().getString(Constants.TASK_NAME)).observe(activity, res -> {
                activity.hideProgressBar();
                if (activity.validResponse(res)) {
                    donors.clear();
                    donors.addAll(res.data.donors);
                    List<String> donors = viewModel.getTakenFieldTestDonors();
                    if (donors != null) {
                        takenFieldTestDonors.clear();
                        takenFieldTestDonors.addAll(donors);
                    }
                    adapter.notifyDataSetChanged();
                    setVisibilityLayout();
                }
            });
            activity.setToolBarTitle(getArguments().getString(Constants.TASK_NAME));
        } else {
            Store.isAllDonor = true;
            viewModel.getAllDonors().observe(activity, res -> {
                activity.hideProgressBar();
                if (activity.validResponse(res)) {
                    donors.clear();
                    List<Donor> donorList = new ArrayList<>();
                    for (Donor donors : res.data.donors) {
                        if (donors.task != null) {
                            donorList.add(donors);
                        }
                    }
                    donors.addAll(donorList);
                    List<String> donors = viewModel.getTakenFieldTestDonors();
                    if (donors != null) {
                        takenFieldTestDonors.clear();
                        takenFieldTestDonors.addAll(donors);
                    }
                    adapter.notifyDataSetChanged();
                    setVisibilityLayout();
                }
            });
        }
    }
}
