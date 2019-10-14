package com.neopharma.datavault.model.donor;

import com.google.gson.annotations.SerializedName;

public class DonorTask {
    @SerializedName("id")
    public String taskId;
    @SerializedName("name")
    public String taskName;
    public String description;
}