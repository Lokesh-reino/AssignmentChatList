package com.example.assignment.repository;

import android.content.Context;

import androidx.paging.DataSource;

import com.example.assignment.interfaces.ContactsDao;
import com.example.assignment.interfaces.UserDao;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LocalRepository {
    private Context ctx;
    private UserDao userDao;
    private ContactsDao contactsDao;

    public LocalRepository(Context ctx){
        this.ctx = ctx;
        userDao = AppDatabase.getInstance(ctx).userDao();
        contactsDao = AppDatabase.getInstance(ctx).contactsDao();
    }
    public Completable insert(User user){
        return userDao.insert(user);
    }

    public Completable deleteUser(int id){
        return userDao.deleteUser(id);
    }

    public Completable updateUserById(String name, String birthday, String phoneNumber, String profilePicPath, Long createdDate, Long modifiedDate, String phoneNumber2, String phoneNumber3, int id){
        return userDao.updateUserById( name, birthday, phoneNumber,profilePicPath,createdDate,modifiedDate,phoneNumber2,phoneNumber3, id);
    }

    /*public Completable updateUseruser(User user){
        return userDao.updateUser(user);
    }*/
    public Single<User> getUserById(int id) {
        return userDao.getUserById(id);

    }

    public DataSource.Factory<Integer, User> queryAllUser(String query) {
        return userDao.queryAllUser(query);
    }

    public DataSource.Factory<Integer, User> getAllUsers() {
        return userDao.getAllUser();
    }

    public Completable insertAllContacts(Contact contacts)
    {

        return contactsDao.insertAll(contacts);
    }

    public DataSource.Factory<Integer, Contact> getAllContacts() {
        return contactsDao.getAllContacts();
    }


    public DataSource.Factory<Integer, Contact> getQueryAllContacts(String s) {
        return contactsDao.getQueryContacts(s);
    }
}
