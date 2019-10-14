package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.services.SyncLocalDataService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    public abstract SyncLocalDataService contributeSyncLocalDataService();
}