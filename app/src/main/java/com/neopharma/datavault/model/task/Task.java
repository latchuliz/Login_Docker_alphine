package com.neopharma.datavault.model.task;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_tasks")
public class Task {

    @PrimaryKey
    @NonNull
    public String id;

    @SerializedName("schedule_date")
    public String scheduleDate;
    public String status;
    public String description;
    public String name;
    @Embedded
    @SerializedName("created_by")
    public CreatedBy createdBy;

    @SerializedName("donorCount")
    public Integer donorCount;

    @Ignore
    public boolean hasDonor() {
        return donorCount != null && donorCount > 0;
    }
}
