package com.neopharma.datavault.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neopharma.datavault.model.donor.Donor;

import java.util.List;

@Dao
public interface DonorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Donor donor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Donor> donors);

    @Delete
    void delete(Donor donors);

    @Query("SELECT * FROM tbl_donor")
    List<Donor> getAll();

    @Query("SELECT * FROM tbl_donor where taskId=:taskId")
    List<Donor> findDonorByTaskId(String taskId);

    @Query("SELECT Count(*) FROM tbl_donor where donorType=:donorType")
    Integer findAllDonorCount(String donorType);

    @Query("SELECT * FROM tbl_donor where taskId IN (:tasks)")
    LiveData<List<Donor>> findAllDonorLive(List<String> tasks);

    @Query("DELETE FROM tbl_donor")
    void deleteAll();

    @Query("DELETE FROM tbl_donor where taskId=:taskId")
    void deleteTaskDonors(String taskId);

    @Query("DELETE FROM tbl_donor where donorType=:donorType")
    void deleteAllDonors(String donorType);

    @Query("SELECT * FROM tbl_donor where donorType=:donorType")
    List<Donor> getAllDonors(String donorType);
}
