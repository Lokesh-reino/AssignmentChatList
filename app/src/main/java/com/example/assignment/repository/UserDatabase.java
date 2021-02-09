package com.example.assignment.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.assignment.models.User;

@Database(entities = {User.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase instance;
    static Migration migration = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD datecreated DATETIME DEFAULT CURRENT_TIMESTAMP ");

        }
    };

    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDatabase.class,
                    "user")
                    .addMigrations(migration)
                    .build();
        }
        return instance;
    }
    public abstract UserDao userDao();
}