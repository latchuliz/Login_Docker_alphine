package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.MainViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    MainViewModel provideMainViewModel() {
        return new MainViewModel();
    }
}
