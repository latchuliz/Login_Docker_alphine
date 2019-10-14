package com.neopharma.datavault.fragments.module;

import com.neopharma.datavault.fragments.viewmodel.SettingViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingModule {
    @Provides
    SettingViewModel provideSettingViewModel(SharedPreferences sharedPreferences) {
        return new SettingViewModel(sharedPreferences);
    }
}
