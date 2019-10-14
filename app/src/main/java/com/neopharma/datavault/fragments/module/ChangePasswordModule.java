package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.ChangePasswordViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePasswordModule {

    @Provides
    ChangePasswordViewModel provideChangePasswordViewModel(Repository repo) {
        return new ChangePasswordViewModel(repo);
    }
}
