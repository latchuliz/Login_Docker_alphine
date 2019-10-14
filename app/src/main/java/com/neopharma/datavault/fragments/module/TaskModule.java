package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.TaskViewModel;
import com.neopharma.datavault.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskModule {

    @Provides
    TaskViewModel provideTaskViewModel(Repository repository) {
        return new TaskViewModel(repository);
    }
}
