package com.neopharma.datavault.fragments.initial;

import android.app.Dialog;

import com.auth0.android.jwt.JWT;
import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentSigninBinding;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.LoginViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.utility.CustomAlertDialog;
import com.neopharma.datavault.utility.PasswordValidator;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;

public class SignInFragment extends InitialFragment<FragmentSigninBinding> {

    @Inject
    public LoginViewModel viewModel;

    @Inject
    public SharedPreferences preferences;
    private CustomAlertDialog alertDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signin;
    }

    @Override
    public void setBRVariables() {
        getViewDataBinding().setVariable(BR.login, viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Validator validator = new Validator(getViewDataBinding());
        final PasswordValidator passwordValidator = new PasswordValidator(getViewDataBinding().inputPassword, getViewDataBinding().passTextInputLayout, R.string.empty_password);
        passwordValidator.setEmptyMessageResouce(R.string.empty_password);
        getViewDataBinding().loginBtn.setOnClickListener(view -> {
            if (validator.validate() && passwordValidator.validate()) {
                doLogin(false);
            } else {
                //TODO move to better place
                if (getViewDataBinding().inputUsername.getError() != null)
                    getViewDataBinding().inputUsername.requestFocus();
                if (getViewDataBinding().inputPassword.getError() != null)
                    getViewDataBinding().inputPassword.requestFocus();
            }
        });
    }

    private void doLogin(boolean b) {
        activity.showProgressBar();
        viewModel.doLogin(b)
                .observe(this, res -> {
                    activity.hideProgressBar();
                    if (activity.validResponseCallback(res)) {
                        String role = new JWT(res.data.token).getClaim("role").asString();
                        if (Constants.SUPERVISOR.equals(role) || Constants.ENFORCEMENT_OFFICER.equals(role))
                            preferences.setToken(res.data.token).observe(this, isSaved -> {
                                if (isSaved) {
                                    activity.actionToDataFetchFragment();
                                }
                            });
                        else
                            activity.alertDialog(Constants.INVALID_LOGIN, Constants.OKAY);
                    } else {
                        if (res != null) {
                            if (res.errorCode != null && res.errorCode.equals("309")) {
                                forceLogin(res.message);
                            } else {
                                activity.alertDialog(res.message, Constants.OKAY);
                            }
                        } else {
                            activity.alertDialog(Constants.ERROR, Constants.OKAY);
                        }
                    }
                });
    }

    public void forceLogin(String message) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        alertDialog = new CustomAlertDialog(activity);
        alertDialog.setMessage(message);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, Constants.FORCE_LOGIN, (dialog, which) -> {
            doLogin(true);
            dialog.dismiss();
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, Constants.CANCEL, (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
