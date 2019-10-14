package com.neopharma.datavault.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    public String token;
    @SerializedName("expires_in")
    public Integer expiresIn;
}