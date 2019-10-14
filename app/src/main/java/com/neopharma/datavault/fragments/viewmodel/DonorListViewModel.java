package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.model.response.DonorResponse;
import com.neopharma.datavault.repository.Repository;

import java.util.List;

public class DonorListViewModel extends ViewModel {
    private Repository repo;

    public DonorListViewModel(Repository repo) {
        this.repo = repo;
    }

    public LiveData<DonorResponse> getDonors(String taskId, String taskName) {
        return repo.getDonors(taskId, taskName);
    }

    public LiveData<DonorResponse> getAllDonors() {
        return repo.getAllDonors();
    }

    public boolean haveIncomplete() {
        return repo.haveIncomplete();
    }

    public FieldTest haveIncompleteTest() {
        return repo.haveIncompleteTest();
    }

    public FieldTest findPendingEqualDonor(String donorId) {
        return repo.findPendingEqualDonor(donorId);
    }

    public List<String> getTakenFieldTestDonors() {
        return repo.getTakenFieldTestDonors();
    }
}
