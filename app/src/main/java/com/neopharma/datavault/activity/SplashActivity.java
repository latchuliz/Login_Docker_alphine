package com.neopharma.datavault.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.InfoViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.response.ConfigResponse;
import com.neopharma.datavault.utility.Store;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    SharedPreferences pref;

    @Inject
    InfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getConfig().observe(this, res -> {
            if (res != null) {
                pref.putString(Constants.Preference.CONFIG, new Gson().toJson(res));
                Store.Config = res.data;
                actionGoToInitialActivity();
            } else {
                String oldConfig = pref.getString(Constants.Preference.CONFIG);
                try {
                    Store.Config = new Gson().fromJson(oldConfig, ConfigResponse.class).data;
                    actionGoToInitialActivity();
                } catch (Exception ex) {
                    //Handle negative;
                    finish();
                }
            }
        });
        //Try to load load secure token and go to initial fragment
        pref.fillToken().observe(this, res -> {

        });
    }

    public void actionGoToInitialActivity() {
        navigator
                .flag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .finish(true)
                .navigate(InitialActivity.class);
    }
}
