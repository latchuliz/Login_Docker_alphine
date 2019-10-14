package com.neopharma.datavault.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    private String username;
    private String password;
    @SerializedName("force_login")
    private Integer forceLogin;

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest setForceLogin(Integer forceLogin) {
        this.forceLogin = forceLogin;
        return this;
    }
}
