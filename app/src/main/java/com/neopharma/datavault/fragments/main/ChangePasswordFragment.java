package com.neopharma.datavault.fragments.main;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentChangePasswordBinding;
import com.neopharma.datavault.fragments.viewmodel.ChangePasswordViewModel;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.Password;
import com.neopharma.datavault.utility.PasswordValidator;
import com.neopharma.datavault.utility.Utility;

import javax.inject.Inject;

public class ChangePasswordFragment extends MainFragment<FragmentChangePasswordBinding> implements OnBackPressedListener {

    @Inject
    public ChangePasswordViewModel viewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.CHANGE_PASSWORD;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideToolBarItem();
        getViewDataBinding().setVariable(BR.data, viewModel);
        viewModel.setPassword(new Password());
        PasswordValidator oldPasswordValidator = new PasswordValidator(getViewDataBinding().oldPassword, getViewDataBinding().oldPasswordTi, R.string.empty_password);
        oldPasswordValidator.setEmptyMessageResouce(R.string.old_password_error);
        PasswordValidator newPasswordValidator = new PasswordValidator(getViewDataBinding().password, getViewDataBinding().passwordTi, R.string.empty_password);
        newPasswordValidator.setEmptyMessageResouce(R.string.password_error);
        newPasswordValidator.setMinLength(6);
        newPasswordValidator.setMinLengthMessage(R.string.min_password_limit);
        PasswordValidator confirmPasswordValidator = new PasswordValidator(getViewDataBinding().confirmPassword, getViewDataBinding().confirmPasswordTi, R.string.empty_password);
        confirmPasswordValidator.setNotEqualResource(R.string.custom_error_password_not_equal);
        getViewDataBinding().passwordSubmit.setOnClickListener(view -> {
            if (validator.validate() &&
                    oldPasswordValidator.validate() &&
                    newPasswordValidator.validate() &&
                    confirmPasswordValidator.equals(newPasswordValidator)) {
                viewModel.update().observe(this, res -> {
                    if (activity.validResponse(res)) {
                        activity.showLogoutDialog(Constants.PASSWORD_CHANGED_SUCCESSFULLY);
                    }
                    if (!Constants.OKAY.equalsIgnoreCase(res.status)) {
                        viewModel.setPassword(new Password());
                    }
                });
            } else {
                //TODO move to better place
                if (getViewDataBinding().password.getError() != null)
                    getViewDataBinding().password.requestFocus();
                if (getViewDataBinding().oldPassword.getError() != null)
                    getViewDataBinding().oldPassword.requestFocus();
                if (getViewDataBinding().confirmPassword.getError() != null)
                    getViewDataBinding().confirmPassword.requestFocus();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        Utility.hideKeyboard(activity);
        return false;
    }
}
