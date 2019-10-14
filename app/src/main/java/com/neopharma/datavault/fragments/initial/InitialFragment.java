package com.neopharma.datavault.fragments.initial;

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

import com.neopharma.datavault.activity.InitialActivity;

import dagger.android.support.AndroidSupportInjection;

public abstract class InitialFragment<T extends ViewDataBinding> extends Fragment {

    protected InitialActivity activity;
    private T mViewDataBinding;

    @LayoutRes
    public abstract int getLayoutId();

    // Method used to set Binding variables by override
    public void setBRVariables() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        activity = (InitialActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        setBRVariables();
        mViewDataBinding.executePendingBindings();
        return mViewDataBinding.getRoot();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

}
