package com.neopharma.datavault.fragments.initial;

import android.app.Dialog;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentDataFetchBinding;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.DataFetchViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.utility.CustomAlertDialog;
import com.neopharma.datavault.utility.Store;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DataFetchFragment extends InitialFragment<FragmentDataFetchBinding> {

    @Inject
    public DataFetchViewModel viewModel;

    @Inject
    SharedPreferences sharedPreferences;
    private CustomAlertDialog dialog;
    private int retryCount;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data_fetch;
    }

    @Override
    public void setBRVariables() {
        getViewDataBinding().setVariable(BR.datafetch, viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = new CustomAlertDialog(activity);
        fetchFromServer();
    }

    public void fetchFromServer() {
        if (!viewModel.isProfileFetchComplete())
            viewModel.fetchProfile().observe(this, profileRes -> {
                if (activity.validResponseCallback(profileRes)) {
                    Store.userId = profileRes.data.id;
                    Store.userRole = profileRes.data.role.code;
                    if (Store.isNetworkAvailable)
                        viewModel.deleteAllTaskData();
                    viewModel.setProfileFetchComplete(true);

                    if (!viewModel.isAdminTaskFetchComplete())
                        viewModel.fetchAdminTasks().observe(this, taskRes -> {
                            if (activity.validResponseCallback(taskRes)) {
                                viewModel.setAdminTaskFetchComplete(true);
                                viewModel.setAdminDonorFetchComplete(true);
                                goToMainActivity();
                            } else {
                                if (taskRes != null && taskRes.errorCode != null && Constants.SESSION_EXPIRED.equals(taskRes.errorCode)) {
                                    activity.showLogoutDialog(taskRes.message);
                                } else {
                                    showRetry();
                                }
                            }
                        });

                    if (Constants.SUPERVISOR.equals(Store.userRole) && !viewModel.isSupervisorTaskFetchComplete())
                        viewModel.fetchSupervisorTasks().observe(this, taskRes -> {
                            if (activity.validResponseCallback(taskRes)) {
                                viewModel.setSupervisorTaskFetchComplete(true);
                                viewModel.setSupervisorDonorFetchComplete(true);
                                goToMainActivity();
                            } else {
                                if (taskRes != null && taskRes.errorCode != null && Constants.SESSION_EXPIRED.equals(taskRes.errorCode)) {
                                    activity.showLogoutDialog(taskRes.message);
                                } else {
                                    showRetry();
                                }
                            }
                        });

                    if (Constants.ENFORCEMENT_OFFICER.equals(Store.userRole) && !viewModel.isEnforcementDonorFetchComplete())
                        viewModel.fetchAllDonors().observe(this, taskRes -> {
                            if (activity.validResponseCallback(taskRes)) {
                                viewModel.setEnforcementDonorFetchComplete(true);
                                goToMainActivity();
                            } else {
                                if (taskRes != null && taskRes.errorCode != null && Constants.SESSION_EXPIRED.equals(taskRes.errorCode)) {
                                    activity.showLogoutDialog(taskRes.message);
                                } else {
                                    showRetry();
                                }
                            }
                        });


                } else {
                    if (profileRes != null && profileRes.errorCode != null && Constants.SESSION_EXPIRED.equals(profileRes.errorCode)) {
                        activity.showLogoutDialog(profileRes.message);
                    } else {
                        showRetry();
                    }
                }
            });

        if (!viewModel.isKitFetchComplete())
            viewModel.fetchKit().observe(this, kitRes -> {
                if (activity.validResponseCallback(kitRes)) {
                    viewModel.setKitFetchComplete(true);
                    goToMainActivity();
                } else {
                    if (kitRes != null && kitRes.errorCode != null && Constants.SESSION_EXPIRED.equals(kitRes.errorCode)) {
                        activity.showLogoutDialog(kitRes.message);
                    } else {
                        showRetry();
                    }
                }
            });

        if (!viewModel.isAreaFetchComplete())
            viewModel.getAreas().observe(this, areaRes -> {
                if (activity.validResponseCallback(areaRes)) {
                    viewModel.setAreaFetchComplete(true);
                    goToMainActivity();
                } else {
                    if (areaRes != null && areaRes.errorCode != null && Constants.SESSION_EXPIRED.equals(areaRes.errorCode)) {
                        activity.showLogoutDialog(areaRes.message);
                    } else {
                        showRetry();
                    }
                }
            });
    }

    private void showRetry() {
        if (retryCount < 3) {
            retryCount++;
            if (dialog.isShowing()) {
                dialog.hide();
            }
            dialog.setMessage("Incomplete, try again.");
            dialog.setImage(true);
            dialog.setCancelable(false);
            dialog.setButton(Dialog.BUTTON_POSITIVE, Constants.OKAY, (dialog, which) -> {
                dialog.dismiss();
                fetchFromServer();
            });
            dialog.setButton(Dialog.BUTTON_NEGATIVE, "go back", (dialog, which) -> {
                dialog.dismiss();
                activity.logout();
            });
            dialog.show();
        } else {
            activity.logout();
        }
    }

    private void goToMainActivity() {
        if (viewModel.isProfileFetchComplete()
                && viewModel.isAdminTaskFetchComplete()
                && viewModel.isKitFetchComplete()
                && viewModel.isAreaFetchComplete()) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd,yyyy | HH:mm", Locale.ENGLISH);
            sharedPreferences.putString(Constants.Preference.LAST_SYNC_TIME, sdf.format(new Date()));
            if (Constants.SUPERVISOR.equals(Store.userRole)) {
                if (viewModel.isSupervisorTaskFetchComplete())
                    activity.actionGoToMainActivity();
            } else {
                if (viewModel.isEnforcementDonorFetchComplete())
                    activity.actionGoToMainActivity();
            }
        }
    }
}
