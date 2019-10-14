package com.neopharma.datavault.model.kit;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_kit")
public class Kit {
    @PrimaryKey
    @NonNull
    public String id;
    @SerializedName("serial_no")
    public String serialNo;
    @SerializedName("test_panel")
    public String testPanel;
    @SerializedName("lot_no")
    public String lotNo;
    @SerializedName("expiry_date")
    public String expiryDate;
    @SerializedName("item_code")
    public String itemCode;
    @SerializedName("received_date")
    public String receivedDate;
    @SerializedName("manufacture_date")
    public String manufactureDate;
    public String status;
}
