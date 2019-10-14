package com.neopharma.datavault.model.area;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AreaData {
    public String lastKey;
    public String count;
    @SerializedName("results")
    public List<Area> areas = null;

    public List<String> getAreaNames() {
        List<String> areaNames = new ArrayList<>();
        if (areas != null)
            for (Area area : areas) {
                areaNames.add(area.area);
            }
        return areaNames;
    }
}
