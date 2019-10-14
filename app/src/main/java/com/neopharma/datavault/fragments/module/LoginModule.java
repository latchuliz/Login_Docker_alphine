package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.LoginViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    LoginViewModel provideLoginViewModel(Repository repository) {
        return new LoginViewModel(repository);
    }
}
