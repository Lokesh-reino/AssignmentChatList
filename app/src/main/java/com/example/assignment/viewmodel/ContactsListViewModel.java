package com.example.assignment.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.assignment.interfaces.ContactsDao;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;
import com.example.assignment.repository.AppDatabase;
import com.example.assignment.repository.LocalRepository;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsListViewModel extends AndroidViewModel {

    private LocalRepository repository = new LocalRepository(getApplication());

    public LiveData contactList;
    public LiveData queriedContactList;

    public static MutableLiveData<String> queryString = new MutableLiveData<>();


    public ContactsListViewModel(@NonNull Application application) {
        super(application);
    }

    public static void setQueryString(String query) {

        queryString.setValue(query);
    }

    public static LiveData<String> getQueryString() {
        return queryString;
    }

    public void saveAllContacts(Contact contacts) {
        repository.insertAllContacts(contacts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("Contact Insert","Completed");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                        e.printStackTrace();

                    }
                });
    }

    public void fetchContactsFromDatabase() {


        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setPageSize(20).build();

        contactList = (new LivePagedListBuilder(repository.getAllContacts(),
                pagedListConfig))
                .build();
    }

    public void initQuery(String s) {
        repository = new LocalRepository(getApplication());
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setPageSize(20).build();

        queriedContactList = (new LivePagedListBuilder(repository.getQueryAllContacts(s),
                pagedListConfig))
                .build();

    }
}
