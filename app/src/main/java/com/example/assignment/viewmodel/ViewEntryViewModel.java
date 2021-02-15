package com.example.assignment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.assignment.models.User;
import com.example.assignment.interfaces.UserDao;
import com.example.assignment.repository.AppDatabase;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ViewEntryViewModel extends AndroidViewModel {
    public MutableLiveData<List<User>> users = new MutableLiveData<>();
    UserDao dao = AppDatabase.getInstance(getApplication()).userDao();

    private CompositeDisposable disposable = new CompositeDisposable();

    public ViewEntryViewModel(@NonNull Application application) {
        super(application);
    }

    public void saveToDatabase(User user){
        dao.insert(user).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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


}