package com.neopharma.datavault.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.model.Constants;

import java.util.List;


@Dao
public interface FieldTestDao {

    @Insert
    long insert(FieldTest fieldTest);

    @Delete
    void delete(FieldTest fieldTest);

    @Update
    void update(FieldTest fieldTest);

    @Query("SELECT * FROM tbl_field_test where user_id=:userId and status='" + Constants.FieldTest.INCOMPLETE + "'")
    FieldTest findIncompleteRecord(String userId);

    @Query("SELECT * FROM tbl_field_test where user_id=:userId and status='" + Constants.FieldTest.PENDING + "'")
    List<FieldTest> findAllPending(String userId);

    @Query("SELECT * FROM tbl_field_test where user_id=:userId and id=:donorId and status='" + Constants.FieldTest.PENDING + "'")
    FieldTest findPendingEqualDonor(String userId, String donorId);

    @Query("SELECT * FROM tbl_field_test where user_id=:userId and status='" + Constants.FieldTest.PENDING + "'")
    LiveData<List<FieldTest>> findAllPendingLive(String userId);

    @Query("SELECT kit_id FROM tbl_field_test where user_id=:userId and status='" + Constants.FieldTest.PENDING + "'")
    List<String> findAllFieldTestUserId(String userId);

    @Query("SELECT id FROM tbl_field_test where user_id=:userId and status='" + Constants.FieldTest.PENDING + "'")
    List<String> findTakenFieldTestDonorsByUserId(String userId);
}
