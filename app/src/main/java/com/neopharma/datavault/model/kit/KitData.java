package com.neopharma.datavault.model.kit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class KitData {
    public Object lastKey;
    public Integer count;
    @SerializedName("results")
    public List<Kit> kits = new ArrayList<>();
}
