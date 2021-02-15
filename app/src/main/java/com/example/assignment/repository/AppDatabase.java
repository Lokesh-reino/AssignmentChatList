package com.example.assignment.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.example.assignment.R;
import com.example.assignment.interfaces.ContactsDao;
import com.example.assignment.interfaces.UserDao;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;

@Database(entities = {User.class, Contact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
//    static Migration migration = new Migration(1,2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE 'user table' ADD COLUMN 'column' TEXT NOT NULL DEFAULT''");
//
//        }
//    };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    context.getResources().getString(R.string.mydatabase_name))
                  //  .addMigrations(migration)
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract ContactsDao contactsDao();
}