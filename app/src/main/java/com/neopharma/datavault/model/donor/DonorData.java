package com.neopharma.datavault.model.donor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonorData {
    public Object lastKey;
    public Integer count;
    @SerializedName("results")
    public List<Donor> donors;
}
