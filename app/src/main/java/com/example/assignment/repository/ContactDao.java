package com.example.assignment.repository;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.assignment.models.Contact;
import com.example.assignment.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ContactDao {

    @Insert
    Completable insert(Contact contact);


    @Query("SELECT * FROM Contact")
    DataSource.Factory<Integer,Contact> getAllContact();

    @Query("Delete From Contact")
    Completable deleteAllUsers();

    @Query("DELETE  FROM Contact  WHERE id = :Id")
    Completable deleteContact(int Id);

    @Delete
    Completable deleteContactfromDb(Contact contact);

    @Query("UPDATE Contact SET name=:u_name,birthday=:u_bday,phoneNumber =:u_phonenumber WHERE id = :Id ")
    Completable  updateContactById(String u_name,String u_bday,String u_phonenumber,int Id);


    @Query("SELECT * FROM Contact WHERE id = :Id")
    Single<User> getContactById(int Id);

    @Query("select * from Contact where name like :query or phoneNumber like :query")
    DataSource.Factory<Integer, Contact> queryAllContact(String query);
}
