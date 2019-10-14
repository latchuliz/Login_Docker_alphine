package com.neopharma.datavault.model;

import com.google.gson.annotations.SerializedName;

public class Password {
    @SerializedName("old_password")
    public String oldPassword;
    @SerializedName("old_password")
    public transient String newPassword;
    @SerializedName("password")
    public String confirmPassword;
}
