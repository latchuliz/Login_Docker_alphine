package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.ProfileViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {
    @Provides
    ProfileViewModel provideProfileViewModel(Repository repository) {
        return new ProfileViewModel(repository);
    }
}
