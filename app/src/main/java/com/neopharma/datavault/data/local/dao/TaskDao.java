package com.neopharma.datavault.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neopharma.datavault.model.task.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Task> tasks);

    @Delete
    void delete(Task tasks);

    @Query("SELECT * FROM tbl_tasks where taskOwnerId = :taskOwnerId")
    List<Task> getSupervisorTasks(String taskOwnerId);

    @Query("SELECT * FROM tbl_tasks where taskOwnerId != :taskOwnerId")
    List<Task> getAdminTasks(String taskOwnerId);

    @Query("SELECT count(*) FROM tbl_tasks where taskOwnerId != :taskOwnerId")
    LiveData<Integer> getTaskCount(String taskOwnerId);

    @Query("DELETE FROM tbl_tasks")
    void deleteAll();

    @Query("DELETE FROM tbl_tasks where taskOwnerId != :taskOwnerId")
    void deleteAllAdmin(String taskOwnerId);

    @Query("DELETE FROM tbl_tasks where taskOwnerId = :taskOwnerId")
    void deleteAllSupervisor(String taskOwnerId);
}
