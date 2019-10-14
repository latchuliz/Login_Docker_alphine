package com.neopharma.datavault.fragments.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentSettingBinding;
import com.neopharma.datavault.fragments.viewmodel.SettingViewModel;
import com.neopharma.datavault.model.Constants.FragmentType;

import javax.inject.Inject;

public class SettingFragment extends MainFragment<FragmentSettingBinding> {

    @Inject
    public SettingViewModel viewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.SETTINGS;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.menuSelected.post(FragmentType.SETTINGS);
        showBackIcon();
        hideToolBarItem();
        getViewDataBinding().syncSwitchButton.setOnCheckedChangeListener((compoundButton, b) -> {
            getViewDataBinding().manual.setTextColor(getResources().getColor(b ? R.color.colorDashText : R.color.colorTestKit));
            getViewDataBinding().auto.setTextColor(getResources().getColor(b ? R.color.colorTestKit : R.color.colorDashText));
            viewModel.saveSyncState(b);
            activity.mService.restartSyncData();
        });
        if (viewModel.getSyncState())
            getViewDataBinding().syncSwitchButton.toggle();

        getViewDataBinding().changePassword.setOnClickListener(v -> activity.actionSettingFragment_to_ChangePasswordFragment(false));
    }
}
