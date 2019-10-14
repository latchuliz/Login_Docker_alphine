package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.DataFetchViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class DataFetchModule {
    @Provides
    DataFetchViewModel provideDataFetchViewModel(Repository repo) {
        return new DataFetchViewModel(repo);
    }
}
