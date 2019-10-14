package com.neopharma.datavault.data.local.database;

import androidx.room.TypeConverter;

import com.neopharma.datavault.data.local.entity.Image;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<Image> image(List<Image> images) {
        return images == null ? null : new ArrayList<>();
    }
}
