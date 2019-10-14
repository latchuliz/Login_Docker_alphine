package com.neopharma.datavault.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neopharma.datavault.model.area.Area;

import java.util.List;

@Dao
public interface AreaDao {

    @Insert
    void insert(Area area);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Area> areas);

    @Delete
    void delete(Area area);

    @Query("SELECT * FROM tbl_areas")
    List<Area> getAreas();

    @Query("DELETE FROM tbl_areas")
    void deleteAll();
}
