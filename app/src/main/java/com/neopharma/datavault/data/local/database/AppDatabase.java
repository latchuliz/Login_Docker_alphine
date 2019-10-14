package com.neopharma.datavault.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.neopharma.datavault.data.local.dao.AreaDao;
import com.neopharma.datavault.data.local.dao.DonorDao;
import com.neopharma.datavault.data.local.dao.FieldTestDao;
import com.neopharma.datavault.data.local.dao.ImageDao;
import com.neopharma.datavault.data.local.dao.KitDao;
import com.neopharma.datavault.data.local.dao.TaskDao;
import com.neopharma.datavault.data.local.dao.UserDao;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.model.area.Area;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.model.kit.Kit;
import com.neopharma.datavault.model.task.Task;
import com.neopharma.datavault.model.user.UserData;

@Database(entities = {FieldTest.class, Image.class, Task.class, Kit.class, UserData.class, Donor.class, Area.class},
        version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FieldTestDao fieldTestDao();

    public abstract ImageDao imageDao();

    public abstract TaskDao taskDao();

    public abstract KitDao kitDao();

    public abstract UserDao userDao();

    public abstract DonorDao donorDao();

    public abstract AreaDao areaDao();
}