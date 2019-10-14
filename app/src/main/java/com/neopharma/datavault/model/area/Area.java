package com.neopharma.datavault.model.area;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_areas")
public class Area {
    @PrimaryKey
    @NonNull
    public String id;
    public String area;
}
