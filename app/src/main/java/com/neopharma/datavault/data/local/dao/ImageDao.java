package com.neopharma.datavault.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neopharma.datavault.data.local.entity.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    long insert(Image image);

    @Delete
    void delete(Image image);

    @Update
    void update(Image image);

    @Query("SELECT * FROM tbl_images where field_id=:id")
    List<Image> findByFieldId(long id);

    @Query("SELECT * FROM tbl_images where field_id=:id and status=:status")
    List<Image> findByAllImage(long id, String status);
}
