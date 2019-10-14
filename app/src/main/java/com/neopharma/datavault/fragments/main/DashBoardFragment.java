package com.neopharma.datavault.fragments.main;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentDashBoardBinding;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.DashBoardViewModel;
import com.neopharma.datavault.fragments.viewmodel.DataFetchViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.utility.Store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class DashBoardFragment extends MainFragment<FragmentDashBoardBinding> {

    @Inject
    public DashBoardViewModel viewModel;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public DataFetchViewModel dataFetchViewModel;
    private boolean profileDone;
    private boolean kitDone;
    private boolean taskDone;
    private boolean donorDone;

    @Override
    public int layoutId() {
        return R.layout.fragment_dash_board;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.DASHBOARD;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(FragmentType.DASHBOARD);
        hideToolBarItem();
        hideBackIcon();
        getViewDataBinding().setVariable(BR.data, viewModel);
        getAllDonorCount();
        viewModel.findAllPendingLive().observe(this, pendingTests -> {
            viewModel.setpCount(String.valueOf(pendingTests.size()));
            viewModel.setkCount(String.valueOf(viewModel.getKitCount()));
        });
        viewModel.getTodayTask().observe(this, tasks -> {
            viewModel.settCount(String.valueOf(tasks));
            getDonorCount();
        });
        getViewDataBinding().startTest.setOnClickListener(view -> activity.actionDashBoard_to_FieldTestFragment(null, true));
        getViewDataBinding().swipeRefresh.setOnRefreshListener(this::fetchFromServer);
        getViewDataBinding().lastSync.setText(getSyncTime());
    }

    public void getDonorCount() {
        if (Constants.ENFORCEMENT_OFFICER.equals(Store.userRole)) {
            viewModel.setdCount(viewModel.getDonorCount());
        } else {
            viewModel.getDonorLive().observe(this, donors -> {
                List<Donor> donorList = new ArrayList<>();
                for (Donor d : donors) {
                    if (d.status.equals(Constants.DonorStatus.NEW)) {
                        donorList.add(d);
                    }
                }
                viewModel.setdCount(String.valueOf(donorList.size()));
            });
        }
    }

    private String getSyncTime() {
        return "Last updated  - " + sharedPreferences.getString(Constants.Preference.LAST_SYNC_TIME);
    }

    public void fetchFromServer() {
        profileDone = false;
        kitDone = false;
        taskDone = false;
        donorDone = false;
        dataFetchViewModel.fetchProfile().observe(this, profileRes -> {
            profileDone = true;
            stopRefresh();
            if (activity.validResponse(profileRes)) {
                activity.setProfile(profileRes);
            }
        });

        dataFetchViewModel.fetchKit().observe(this, kitRes -> {
            kitDone = true;
            stopRefresh();
            if (activity.validResponse(kitRes)) {
                viewModel.setkCount(String.valueOf(viewModel.getKitCount()));
            }
        });

        dataFetchViewModel.fetchAdminTasks().observe(this, taskRes -> {
            taskDone = true;
            stopRefresh();
            if (activity.validResponse(taskRes)) {
                //Ignored
            }
        });

        if (Constants.SUPERVISOR.equals(Store.userRole))
            dataFetchViewModel.fetchSupervisorTasks().observe(this, taskRes -> {
                taskDone = true;
                stopRefresh();
                if (activity.validResponse(taskRes)) {
                    //Ignored
                }
            });

        getAllDonorCount();
    }

    private void getAllDonorCount() {
        if (Constants.ENFORCEMENT_OFFICER.equals(Store.userRole)) {
            dataFetchViewModel.fetchAllDonors().observe(this, donorResponse -> {
                donorDone = true;
                stopRefresh();
                if (activity.validResponse(donorResponse)) {
                    getDonorCount();
                }
            });
        } else {
            getDonorCount();
        }
    }

    private void stopRefresh() {
        if (profileDone && kitDone && taskDone && Store.isNetworkAvailable) {
            getViewDataBinding().swipeRefresh.setRefreshing(false);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy | HH:mm", Locale.ENGLISH);
            sharedPreferences.putString(Constants.Preference.LAST_SYNC_TIME, sdf.format(new Date()));
            getViewDataBinding().lastSync.setText(getSyncTime());
        } else if (!Store.isNetworkAvailable) {
            getViewDataBinding().swipeRefresh.setRefreshing(false);
        }
    }
}
