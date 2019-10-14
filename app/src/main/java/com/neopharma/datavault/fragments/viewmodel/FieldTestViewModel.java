package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.Bindable;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.data.local.database.AppDatabase;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.kit.Kit;
import com.neopharma.datavault.model.response.AreaResponse;
import com.neopharma.datavault.model.response.KitResponse;
import com.neopharma.datavault.repository.Repository;

import java.util.List;

public class FieldTestViewModel extends BaseViewModel {
    private Repository repo;
    private FieldTest fieldTest;

    private MutableLiveData<List<Image>> donorImageList = new MutableLiveData<>();
    private MutableLiveData<List<Image>> kitImageList = new MutableLiveData<>();
    private MutableLiveData<List<Image>> testResultImageList = new MutableLiveData<>();

    public LiveData<List<Image>> getDonorImages() {
        return donorImageList;
    }

    public void setDonorImages(List<Image> donorImages) {
        donorImageList.postValue(donorImages);
    }

    public LiveData<List<Image>> getKitImages() {
        return kitImageList;
    }

    public void setKitImages(List<Image> kitImages) {
        kitImageList.postValue(kitImages);
    }

    public LiveData<List<Image>> getTestResultImages() {
        return testResultImageList;
    }

    public void setTestResultImages(List<Image> testResultImages) {
        testResultImageList.postValue(testResultImages);
    }

    @Bindable
    public FieldTest getFieldTest() {
        return fieldTest;
    }

    public void setFieldTest(FieldTest fieldTest) {
        this.fieldTest = fieldTest;
        notifyPropertyChanged(BR.fieldTest);
    }

    public void update() {
        repo.update(fieldTest);
    }

    public FieldTestViewModel(Repository repo, AppDatabase db) {
        this.repo = repo;
    }

    public LiveData<KitResponse> getKits() {
        return repo.getKits();
    }

    public FieldTest findOrCreateIncompleteRecord() {
        return repo.findOrCreateIncompleteRecord();
    }

    public Image saveImagePath(FieldTest fieldTest, String imagePath, Constants.ImageType type) {
        return repo.saveImagePath(fieldTest, imagePath, type);
    }

    public Image saveImagePath(String imagePath, Constants.ImageType type) {
        return saveImagePath(findOrCreateIncompleteRecord(), imagePath, type);
    }

    public void removeImage(Image image) {
        repo.removeImage(image);
    }

    public List<Kit> getUnusedKits() {
        return repo.getUnusedKits();
    }

    public void deleteFieldTest() {
        repo.deleteFieldTestById(fieldTest);
    }

    public LiveData<AreaResponse> getAreas() {
        return repo.getAreas();
    }
}
