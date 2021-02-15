package com.example.assignment.interfaces;

import androidx.paging.DataSource;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);


    @Query("SELECT * FROM User ORDER BY id DESC")
    DataSource.Factory<Integer,User> getAllUser();

    @Query("Delete From User")
    Completable deleteAllUsers();

    @Query("DELETE  FROM User  WHERE id = :Id")
    Completable deleteUser(int Id);


    @Query("UPDATE User SET name=:name,birthday=:birthday,phoneNumber1 =:phoneNumber,image=:profilePicPath,phoneNumber2=:phoneNumber2,phoneNumber3=:phoneNumber3,modifiedAt=:modifiedDate,createdAt=:createdDate WHERE id = :id ")
    Completable updateUserById(String name, String birthday, String phoneNumber, String profilePicPath, Long createdDate, Long modifiedDate, String phoneNumber2, String phoneNumber3, int id);


    @Query("SELECT * FROM User WHERE id = :Id")
    Single<User> getUserById(int Id);

    @Query("select * from User where name like :query or phoneNumber1 like :query ORDER BY id DESC")
    DataSource.Factory<Integer, User> queryAllUser(String query);


}
