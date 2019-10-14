package com.neopharma.datavault.model.user;

import androidx.room.ColumnInfo;

public class Tenant {
    @ColumnInfo(name = "tenant_id")
    public String id;
    public String name;
    public String hostName;
    public String country;
}