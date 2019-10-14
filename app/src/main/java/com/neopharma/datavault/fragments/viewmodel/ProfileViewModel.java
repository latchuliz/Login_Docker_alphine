package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.model.response.UserResponse;
import com.neopharma.datavault.repository.Repository;

public class ProfileViewModel extends ViewModel {
    private Repository repo;

    public ProfileViewModel(Repository repo) {
        this.repo = repo;
    }

    public LiveData<UserResponse> getProfile() {
        return repo.getProfile();
    }
}
