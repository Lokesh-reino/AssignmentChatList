package com.example.assignment.repository;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.assignment.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);


    @Query("SELECT * FROM User")
    DataSource.Factory<Integer,User> getAllUser();

    @Query("Delete From User")
    Completable deleteAllUsers();

    @Query("DELETE  FROM User  WHERE id = :Id")
    Completable deleteUser(int Id);

    @Delete
    Completable deleteUserfromDb(User user);

    @Query("UPDATE User SET name=:u_name,birthday=:u_bday,phoneNumber =:u_phonenumber, modified_at =:time_now WHERE id = :Id ")
    Completable  updateUserById(String u_name,String u_bday,String u_phonenumber,int Id,long time_now);

    @Query("SELECT * FROM User WHERE id = :Id")

    Single<User> getUserById(int Id);

    @Query("SELECT * FROM User Order By datecreated DESC")
    DataSource.Factory<Integer,User> getAllUserbyDateCreated();


    @Query("select * from User where name like :query or phoneNumber like :query")
    DataSource.Factory<Integer, User> queryAllUser(String query);

    @Query("UPDATE User SET name=:u_name,birthday=:u_bday,phoneNumber =:u_phonenumber,image=:u_image WHERE id = :Id ")
    Completable  undoDeleteInDatabase(String u_name,String u_bday,String u_phonenumber,String u_image,int Id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWithTime(User user);

    @Update
    void updateWithTime(User user);

//    @Dao
//    abstract class TimestampDataDao {
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        abstract void insertwithTime(User user);
//
//        private void insertWithTimestamp(User user) {
//            insert(user.apply{
//                createdAt = System.currentTimeMillis();
//                modifiedAt = System.currentTimeMillis();
//            }
//        }
//
//        @Update
//        abstract fun update(data: Data)
//
//        fun updateWithTimestamp(data: Data) {
//            insert(data.apply{
//                modifiedAt = System.currentTimeMillis()
//            }
//        }
//    }


}
