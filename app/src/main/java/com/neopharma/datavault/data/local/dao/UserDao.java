package com.neopharma.datavault.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neopharma.datavault.model.user.UserData;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserData userData);

    @Delete
    void delete(UserData userData);

    @Query("SELECT * FROM tbl_user")
    UserData getUser();

    @Query("DELETE FROM tbl_user")
    void deleteAll();
}
