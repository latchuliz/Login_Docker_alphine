package com.neopharma.datavault.model.donor;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_donor")
public class Donor {
    @PrimaryKey
    @NonNull
    public String id;
    public String status;
    public String address;
    public String email;
    public String ssn;
    public String name;
    public String gender;
    @Embedded
    public DonorTask task;
    public String dob;
    @SerializedName("mobile_number")
    public String mobileNumber;
    public String donorType;

    @SerializedName("id_type")
    public String idType;

    @SerializedName("id_no")
    public String idReference;
}
