package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.model.Constants.Settings;

import javax.inject.Inject;

public class SettingViewModel extends ViewModel {
    private SharedPreferences preferences;

    @Inject
    public SettingViewModel(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveSyncState(boolean b) {
        preferences.putBoolean(Settings.AUTO_SYNC, b);
    }

    public boolean getSyncState() {
        return preferences.getBoolean(Settings.AUTO_SYNC);
    }
}
