package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.DonorListViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class DonorListModule {

    @Provides
    DonorListViewModel provideDonorListViewModel(Repository repo) {
        return new DonorListViewModel(repo);
    }
}
