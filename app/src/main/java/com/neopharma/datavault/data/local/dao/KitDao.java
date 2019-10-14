package com.neopharma.datavault.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neopharma.datavault.model.kit.Kit;

import java.util.List;

@Dao
public interface KitDao {
    @Insert
    void insert(Kit kit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Kit> kits);

    @Delete
    void delete(Kit kit);

    @Query("SELECT * FROM tbl_kit")
    List<Kit> getAll();

    @Query("DELETE FROM tbl_kit")
    void deleteAll();

    @Query("DELETE FROM tbl_kit where serialNo = :serialNo")
    void deleteSyncKit(String serialNo);
}
