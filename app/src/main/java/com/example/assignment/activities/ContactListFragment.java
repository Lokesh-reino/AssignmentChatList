package com.example.assignment.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactAdapter;
import com.example.assignment.models.Contact;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ir.mirrajabi.rxcontacts.RxContacts;

public class ContactListFragment extends Fragment {

    private ContactAdapter contactAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initializeRecyclerView();
        Log.d("hello","in recycler view");
        return inflater.inflate(R.layout.fragment_contact_list, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        requestContacts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void initializeRecyclerView() {
        ContactAdapter contactAdapter = getContactAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView rv = getActivity().findViewById(R.id.contacts_recycler_view);
        if (rv != null) {
            rv.setAdapter(contactAdapter);
            rv.setLayoutManager(linearLayoutManager);
        }
    }

    private ContactAdapter getContactAdapter() {
        if (contactAdapter != null) {
            return contactAdapter;
        }
        contactAdapter = new ContactAdapter();
        return contactAdapter;
    }

    private void requestContacts() {
        compositeDisposable.add(RxContacts
                .fetch(getActivity())
                .filter(m -> m.getInVisibleGroup() == 1)
                .toSortedList(Contact::compareTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    ContactAdapter adapter = getContactAdapter();
                    adapter.setContacts(contacts);
                    adapter.notifyDataSetChanged();
                }, it -> {
                    //Handle exception
                }));
    }
}