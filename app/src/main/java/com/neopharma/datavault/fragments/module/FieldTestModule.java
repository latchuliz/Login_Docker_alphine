package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.data.local.database.AppDatabase;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class FieldTestModule {

    @Provides
    FieldTestViewModel provideFieldTestViewModel(Repository repository, AppDatabase appDatabase) {
        return new FieldTestViewModel(repository, appDatabase);
    }
}
