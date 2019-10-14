package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.model.response.ImageUploadResponse;
import com.neopharma.datavault.model.response.Response;
import com.neopharma.datavault.repository.Repository;

import java.util.List;

import okhttp3.MultipartBody;

public class DonorHistoryViewModel extends ViewModel {
    private Repository repo;

    public DonorHistoryViewModel(Repository repo) {
        this.repo = repo;
    }

    public List<FieldTest> getPendingDonors() {
        return repo.getPendingDonor();
    }

    public LiveData<List<FieldTest>> getPendingDonorsLive() {
        return repo.findAllPendingLive();
    }

    public List<Image> getByAllImage(long id, String status) {
        return repo.findByAllImage(id, status);
    }

    public LiveData<ImageUploadResponse> imageUpload(MultipartBody.Part file_to_upload, Image image) {
        return repo.imageUpload(file_to_upload, image);
    }

    public LiveData<Response> syncCloud(FieldTest test) {
        return repo.syncCloud(test);
    }

    public List<Image> findByAllImage(long id, String status) {
        return repo.findByAllImage(id, status);
    }
}
