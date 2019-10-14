package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;

public class SyncViewModel extends BaseViewModel {
    private int pendingCount;
    private int completedCount;
    private MutableLiveData<Integer> completedLiveCount = new MutableLiveData<>();

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public MutableLiveData<Integer> getCompletedLiveCount() {
        return completedLiveCount;
    }

    public void setCompletedLiveCount(int count) {
        completedLiveCount.postValue(count);
    }
}
