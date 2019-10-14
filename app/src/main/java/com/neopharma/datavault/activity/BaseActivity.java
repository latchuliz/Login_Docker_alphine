package com.neopharma.datavault.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.neopharma.datavault.R;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.response.Response;
import com.neopharma.datavault.navigation.Navigator;
import com.neopharma.datavault.repository.Repository;
import com.neopharma.datavault.utility.CustomAlertDialog;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.neopharma.datavault.utility.Store.isInfoSkipped;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {
    protected Navigator navigator;
    protected Dialog dialog;
    private CustomAlertDialog alertDialog;

    @Inject
    SharedPreferences preferences;

    @Inject
    Repository repo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        navigator = new Navigator();
        navigator.init(this);
        progressInit();
    }

    private void progressInit() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_progress_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigator = null;
        dialog.dismiss();
        dismissDialog();
    }

    public void showProgressBar() {
        dialog.show();
    }

    public void hideProgressBar() {
        dialog.dismiss();
    }

    public boolean validResponse(Response res) {
        if (res != null) {
            if (Constants.OKAY.equalsIgnoreCase(res.status)) {
                return true;
            } else {
                if (Constants.SESSION_EXPIRED.equals(res.errorCode)) {
                    showLogoutDialog(res.message);
                } else {
                    alertDialog(res.message, Constants.OKAY);
                }
            }
        } else {
            alertDialog(Constants.ERROR, Constants.OKAY);
        }
        return false;
    }

    public boolean validResponseCallback(Response res) {
        if (res != null) {
            return Constants.OKAY.equalsIgnoreCase(res.status);
        }
        return false;
    }

    public void logout() {
        //reset token
        showProgressBar();
        repo.logout();
        isInfoSkipped = true;
        preferences.setToken("").observe(this, isSaved -> {
            hideProgressBar();
            if (isSaved) {
                actionToInitialActivity();
            }
        });
    }

    public void actionToInitialActivity() {
        navigator
                .finish(true)
                .navigate(InitialActivity.class);
    }

    public void showLogoutDialog(String message) {
        showLogoutDialog(message, false);
    }

    public void showLogoutDialog(String message, boolean showNegative) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        alertDialog = new CustomAlertDialog(this);
        alertDialog.setMessage(message);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, Constants.OKAY, (dialog, which) -> {
            if (showNegative)
                preferences.putBoolean(Constants.Settings.AUTO_SYNC, false);
            logout();
            dialog.dismiss();
        });
        if (showNegative)
            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, Constants.CANCEL, (dialog, which) -> {
                dialog.dismiss();
            });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void alertDialog(String message, String okay) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        alertDialog = new CustomAlertDialog(this);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, okay, (dialog, which) -> alertDialog.dismiss());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void discardFieldTestDialog(FieldTestViewModel viewModel) {
        alertDialog = new CustomAlertDialog(this);
        alertDialog.setMessage(Constants.FIELD_TEST_ALERT);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, Constants.DISCARD, (dialog, which) -> {
            viewModel.deleteFieldTest();
            super.onBackPressed();
            alertDialog.dismiss();
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, Constants.KEEP, (dialog, which) -> {
            viewModel.update();
            super.onBackPressed();
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}