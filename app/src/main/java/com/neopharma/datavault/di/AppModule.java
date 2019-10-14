package com.neopharma.datavault.di;

import android.app.Application;
import androidx.room.Room;
import android.content.Context;

import com.neopharma.datavault.data.local.database.AppDatabase;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.utility.Connection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(Application application) {
        return new SharedPreferences(application);
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application) {
        AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "datavault.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return db;
    }

    @Provides
    @Singleton
    Connection provideConnection(Application application) {
        return new Connection(application);
    }
}
