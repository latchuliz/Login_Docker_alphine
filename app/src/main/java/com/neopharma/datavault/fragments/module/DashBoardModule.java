package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.DashBoardViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class DashBoardModule {
    @Provides
    DashBoardViewModel provideDashBoardViewModel(Repository repo) {
        return new DashBoardViewModel(repo);
    }
}
