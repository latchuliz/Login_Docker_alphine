package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.databinding.Bindable;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.model.Password;
import com.neopharma.datavault.model.response.Response;
import com.neopharma.datavault.repository.Repository;
import com.neopharma.datavault.utility.Utility;

public class ChangePasswordViewModel extends BaseViewModel {
    private Repository repo;
    private Password password;

    public ChangePasswordViewModel(Repository repo) {
        this.repo = repo;
    }

    @Bindable
    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public LiveData<Response> update() {
        password.oldPassword = Utility.sha256HashString(password.oldPassword);
        password.confirmPassword = Utility.sha256HashString(password.confirmPassword);
        return repo.updatePassword(password);
    }
}
