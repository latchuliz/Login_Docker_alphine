package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.SyncViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class SyncModule {

    @Provides
    SyncViewModel provideSyncViewModel() {
        return new SyncViewModel();
    }
}
