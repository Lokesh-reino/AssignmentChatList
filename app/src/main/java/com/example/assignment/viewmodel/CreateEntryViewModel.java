package com.example.assignment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.assignment.models.User;
import com.example.assignment.repository.LocalRepository;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateEntryViewModel extends AndroidViewModel {


    public LiveData<PagedList<User>> userList ;
    public MutableLiveData<List<User>> users = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();
    private LocalRepository repository = new LocalRepository(getApplication());
    public LiveData<PagedList<User>> queriedUserList;

    public static MutableLiveData<String> queryString = new MutableLiveData<>();
    public static void setQueryString(String query) {
        queryString.setValue(query);
    }
    public LiveData<String> getQueryString() {
        return queryString;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public CreateEntryViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        fetchDataFromDatabase();
    }

    public void fetchDataFromDatabase() {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        userList = new LivePagedListBuilder<>(repository.getAllUsers(),config).build();

    }


    public void saveToDatabase(User user){
        repository.insert(user).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            }
            @Override
            public void onComplete() {
                fetchDataFromDatabase();
                //  Toast.makeText(getApplication(),"User Saved",Toast.LENGTH_SHORT).show();
                Log.e("Tab1Viewmodel", "<---------- onComplete: User Saved in DB ---------->" );
            }
            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                e.printStackTrace();
                Log.e("Tab1Viewmodel", "onError: ---->"+e.getMessage() );

            }
        });

    }
    public void fetchDetailsFromDatabase(int id){
        repository.getUserById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull User user1) {
                        user.setValue(user1);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }
                });

    }

    public void deleteUserFromDatabase(int id){
        repository.deleteUser(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {


                    }
                    @Override
                    public void onComplete() {
                        fetchDataFromDatabase();

                    }
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    public void updateUser(String name, String birthday, String phoneNumber, String ProfilePicPath, Long createdDate, Long modifiedDate, String phoneNumber2, String phoneNumber3, int Id){
        repository.updateUserById(name, birthday, phoneNumber,ProfilePicPath,createdDate,modifiedDate,phoneNumber2,phoneNumber3, Id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }
                });
    }
    private static MutableLiveData<Boolean> isMultiSelectOn = new MutableLiveData<>();

    public void setIsMultiSelect(boolean isMultiSelect) {
        isMultiSelectOn.setValue(isMultiSelect);
    }

    public static LiveData<Boolean> getIsMultiSelectOn() {
        return isMultiSelectOn;
    }



    public LiveData<List<User>> getUsers() {
        return users;
    }
    public LiveData<User> getUser() {
        return user;
    }

    public void queryInit(String query) {

        repository = new LocalRepository(getApplication());

        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();
        queriedUserList = new LivePagedListBuilder<>(repository.queryAllUser(query), config).build();
    }
}