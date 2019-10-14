package com.neopharma.datavault.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.databinding.Bindable;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.request.LoginRequest;
import com.neopharma.datavault.model.response.LoginResponse;
import com.neopharma.datavault.repository.Repository;

import static com.neopharma.datavault.utility.Utility.sha256HashString;

public class LoginViewModel extends BaseViewModel {
    private Repository repo;
    private String username;
    private String password;

    public LoginViewModel(Repository repository) {
        repo = repository;
    }

    public LiveData<LoginResponse> doLogin(boolean b) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(sha256HashString(password));
        if (b) request.setForceLogin(Constants.Numbers.ONE);
        return repo.doLogin(request);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
