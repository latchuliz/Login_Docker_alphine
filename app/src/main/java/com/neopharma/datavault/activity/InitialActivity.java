package com.neopharma.datavault.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.neopharma.datavault.R;
import com.neopharma.datavault.fragments.initial.DataFetchFragment;
import com.neopharma.datavault.fragments.initial.InfoFragment;
import com.neopharma.datavault.fragments.initial.SignInFragment;
import com.neopharma.datavault.utility.Connection;
import com.neopharma.datavault.utility.Store;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.neopharma.datavault.utility.Store.isInfoSkipped;

public class InitialActivity extends BaseActivity implements HasSupportFragmentInjector {

    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    public Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        connection.observe(this, res -> {
            Store.isNetworkAvailable = res;
            if (!preferences.getToken().isEmpty()) {
                actionToDataFetchFragment();
                return;
            }
            //move forward once connection state is known
            navigator.navigate(isInfoSkipped ? new SignInFragment() : new InfoFragment());
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public void actionGoToMainActivity() {
        navigator
                .flag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .finish(true)
                .navigate(MainActivity.class);
    }

    public void actionToSignInFragment() {
        navigator.navigate(new SignInFragment());
    }

    public void actionToDataFetchFragment() {
        navigator.navigate(new DataFetchFragment());
    }
}
