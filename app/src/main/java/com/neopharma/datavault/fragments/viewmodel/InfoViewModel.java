package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.model.response.ConfigResponse;
import com.neopharma.datavault.repository.Repository;

public class InfoViewModel extends ViewModel {
    private Repository repo;

    public InfoViewModel(Repository repository) {
        repo = repository;
    }

    public LiveData<ConfigResponse> getConfig() {
        return repo.getConfig();
    }
}
