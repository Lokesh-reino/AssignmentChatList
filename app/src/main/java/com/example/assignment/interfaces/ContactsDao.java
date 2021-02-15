package com.example.assignment.interfaces;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assignment.models.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface ContactsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Contact contacts);

    @Query("SELECT * FROM contact ORDER BY name")
    DataSource.Factory<Integer, Contact> getAllContacts();


    @Query("SELECT * FROM contact where name like :s or phoneNumber like :s ORDER BY name")
    DataSource.Factory<Integer, Contact> getQueryContacts(String s);
}
