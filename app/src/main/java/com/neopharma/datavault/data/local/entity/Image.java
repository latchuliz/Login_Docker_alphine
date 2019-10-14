package com.neopharma.datavault.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_images", indices = {@Index("field_id")}, foreignKeys = @ForeignKey(entity = FieldTest.class, parentColumns = "field_id", childColumns = "field_id", onDelete = ForeignKey.CASCADE))
public class Image {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "method_name")
    public String methodName;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "field_id")
    public long fieldId;
}
