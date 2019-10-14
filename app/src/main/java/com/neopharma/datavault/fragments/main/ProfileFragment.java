package com.neopharma.datavault.fragments.main;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentProfileBinding;
import com.neopharma.datavault.fragments.viewmodel.ProfileViewModel;
import com.neopharma.datavault.model.Constants;

import javax.inject.Inject;

public class ProfileFragment extends MainFragment<FragmentProfileBinding> {

    @Inject
    public ProfileViewModel viewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected String getToolbarTitle() {
        return Constants.FragmentType.PROFILE;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(Constants.FragmentType.PROFILE);
        activity.showBackIcon();
        showToolbarLogout();
        activity.showProgressBar();
        viewModel.getProfile().observe(this, res -> {
            activity.hideProgressBar();
            if (activity.validResponse(res)) {
                activity.setProfile(res);
                getViewDataBinding().setVariable(BR.data, res.data);
                getViewDataBinding().executePendingBindings();
            }
        });
    }
}
