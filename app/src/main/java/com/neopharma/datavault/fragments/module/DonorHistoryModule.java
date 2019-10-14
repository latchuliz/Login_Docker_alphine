package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.DonorHistoryViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class DonorHistoryModule {

    @Provides
    DonorHistoryViewModel provideDonorHistoryViewModel(Repository repo) {
        return new DonorHistoryViewModel(repo);
    }
}
