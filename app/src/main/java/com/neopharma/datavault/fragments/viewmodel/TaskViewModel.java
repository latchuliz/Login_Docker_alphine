package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.neopharma.datavault.model.response.TaskResponse;
import com.neopharma.datavault.repository.Repository;

public class TaskViewModel extends ViewModel {
    private Repository repo;

    public TaskViewModel(Repository repo) {
        this.repo = repo;
    }

    public LiveData<TaskResponse> getAdminTasks() {
        return repo.getAdminTasks();
    }

    public LiveData<TaskResponse> getSupervisorTasks() {
        return repo.getSupervisorTasks();
    }
}
