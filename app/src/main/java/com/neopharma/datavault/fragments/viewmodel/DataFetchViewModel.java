package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.databinding.Bindable;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.model.response.AreaResponse;
import com.neopharma.datavault.model.response.DonorResponse;
import com.neopharma.datavault.model.response.KitResponse;
import com.neopharma.datavault.model.response.TaskResponse;
import com.neopharma.datavault.model.response.UserResponse;
import com.neopharma.datavault.repository.Repository;

public class DataFetchViewModel extends BaseViewModel {
    private Repository repo;
    private boolean profileFetchComplete;
    private boolean kitFetchComplete;
    private boolean adminTaskFetchComplete;
    private boolean supervisorTaskFetchComplete;
    private boolean adminDonorFetchComplete;
    private boolean supervisorDonorFetchComplete;
    private boolean enforcementDonorFetchComplete;
    private boolean areaFetchComplete;

    public DataFetchViewModel(Repository repo) {
        this.repo = repo;
    }

    public LiveData<UserResponse> fetchProfile() {
        return repo.getProfile();
    }

    public void deleteAllTaskData() {
        repo.deleteAllTaskData();
    }

    public LiveData<TaskResponse> fetchAdminTasks() {
        return repo.getAdminTasks();
    }

    public LiveData<TaskResponse> fetchSupervisorTasks() {
        return repo.getSupervisorTasks();
    }

    public LiveData<DonorResponse> fetchAllDonors() {
        return repo.getAllDonors();
    }
    public LiveData<AreaResponse> getAreas() {
        return repo.getAreas();
    }

    public LiveData<KitResponse> fetchKit() {
        return repo.getKits();
    }

    @Bindable
    public boolean isEnforcementDonorFetchComplete() {
        return enforcementDonorFetchComplete;
    }

    public void setEnforcementDonorFetchComplete(boolean enforcementDonorFetchComplete) {
        this.enforcementDonorFetchComplete = enforcementDonorFetchComplete;
        notifyPropertyChanged(BR.enforcementDonorFetchComplete);
    }

    @Bindable
    public boolean isProfileFetchComplete() {
        return profileFetchComplete;
    }

    public void setProfileFetchComplete(boolean profileFetchComplete) {
        this.profileFetchComplete = profileFetchComplete;
        notifyPropertyChanged(BR.profileFetchComplete);
    }

    @Bindable
    public boolean isKitFetchComplete() {
        return kitFetchComplete;
    }

    public void setKitFetchComplete(boolean kitFetchComplete) {
        this.kitFetchComplete = kitFetchComplete;
        notifyPropertyChanged(BR.kitFetchComplete);
    }

    @Bindable
    public boolean isAdminTaskFetchComplete() {
        return adminTaskFetchComplete;
    }

    public void setAdminTaskFetchComplete(boolean adminTaskFetchComplete) {
        this.adminTaskFetchComplete = adminTaskFetchComplete;
        notifyPropertyChanged(BR.adminTaskFetchComplete);
    }

    @Bindable
    public boolean isSupervisorTaskFetchComplete() {
        return supervisorTaskFetchComplete;
    }

    public void setSupervisorTaskFetchComplete(boolean supervisorTaskFetchComplete) {
        this.supervisorTaskFetchComplete = supervisorTaskFetchComplete;
        notifyPropertyChanged(BR.supervisorTaskFetchComplete);
    }

    @Bindable
    public boolean isAreaFetchComplete() {
        return areaFetchComplete;
    }

    public void setAreaFetchComplete(boolean areaFetchComplete) {
        this.areaFetchComplete = areaFetchComplete;
        notifyPropertyChanged(BR.areaFetchComplete);
    }

    @Bindable
    public boolean isAdminDonorFetchComplete() {
        return adminDonorFetchComplete;
    }

    public void setAdminDonorFetchComplete(boolean adminDonorFetchComplete) {
        this.adminDonorFetchComplete = adminDonorFetchComplete;
        notifyPropertyChanged(BR.adminDonorFetchComplete);
    }

    @Bindable
    public boolean isSupervisorDonorFetchComplete() {
        return supervisorDonorFetchComplete;
    }

    public void setSupervisorDonorFetchComplete(boolean supervisorDonorFetchComplete) {
        this.supervisorDonorFetchComplete = supervisorDonorFetchComplete;
        notifyPropertyChanged(BR.supervisorDonorFetchComplete);
    }

}
