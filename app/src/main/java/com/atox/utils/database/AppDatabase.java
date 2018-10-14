package com.atox.utils.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.atox.utils.database.UserDao;
import com.atox.utils.database.UserModel;

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "atox-database";

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            Context appContext = context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract UserDao userModel();

}