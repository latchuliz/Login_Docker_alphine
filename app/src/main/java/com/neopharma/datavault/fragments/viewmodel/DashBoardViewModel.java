package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.databinding.Bindable;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.repository.Repository;

import java.util.List;

public class DashBoardViewModel extends BaseViewModel {

    private Repository repo;
    private String tCount, pCount, kCount, dCount;

    public DashBoardViewModel(Repository repo) {
        this.repo = repo;
    }

    @Bindable
    public String getdCount() {
        return dCount;
    }

    public void setdCount(String dCount) {
        this.dCount = dCount;
        notifyPropertyChanged(BR.dCount);
    }

    @Bindable
    public String getpCount() {
        return pCount;
    }

    public void setpCount(String pCount) {
        this.pCount = pCount;
        notifyPropertyChanged(BR.pCount);
    }

    @Bindable
    public String gettCount() {
        return tCount;
    }

    public void settCount(String tCount) {
        this.tCount = tCount;
        notifyPropertyChanged(BR.tCount);
    }

    @Bindable
    public String getkCount() {
        return kCount;
    }

    public void setkCount(String kCount) {
        this.kCount = kCount;
        notifyPropertyChanged(BR.kCount);
    }

    public LiveData<List<Donor>> getDonorLive() {
        return repo.getDonorLive();
    }

    public String getDonorCount() {
        return String.valueOf(repo.getDonorCount());
    }

    public LiveData<List<FieldTest>> findAllPendingLive() {
        return repo.findAllPendingLive();
    }

    public LiveData<Integer> getTodayTask() {
        return repo.getTodayTask();
    }

    public Integer getKitCount() {
        return repo.getUnusedKits().size();
    }

}
