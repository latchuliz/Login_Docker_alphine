package com.neopharma.datavault.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ConfigData {
    @SerializedName("IDTYPE")
    public HashMap<String, String> idTypes;

    @SerializedName("TAMPERING")
    public HashMap<String, String> tamperingTypes;
}
