package com.neopharma.datavault.fragments.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.activity.MainActivity;

import br.com.ilhasoft.support.validation.Validator;
import dagger.android.support.AndroidSupportInjection;

public abstract class MainFragment<T extends ViewDataBinding> extends Fragment {

    protected MainActivity activity;
    private T binding;
    protected Validator validator = null;

    @LayoutRes
    public abstract int layoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        validator = new Validator(binding);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setToolBarTitle(getToolbarTitle());
    }

    protected String getToolbarTitle() {
        return "";
    }

    protected void hideToolBarItem() {
        activity.hideToolBarItems();
    }

    protected void showBackIcon() {
        activity.showBackIcon();
    }

    protected void hideBackIcon() {
        activity.hideBackIcon();
    }

    protected void showToolbarSync() {
        activity.showSync();
    }

    protected void showToolbarLogout() {
        activity.showLogout();
    }

    public T getViewDataBinding() {
        return binding;
    }

}
