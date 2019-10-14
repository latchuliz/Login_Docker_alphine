package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.InfoViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class InfoPresenterModule {

    @Provides
    InfoViewModel provideUserViewModel(Repository repository) {
        return new InfoViewModel(repository);
    }
}
