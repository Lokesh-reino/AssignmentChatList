package com.example.assignment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactsListAdapter;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;
import com.example.assignment.repository.AppDatabase;
import com.example.assignment.viewmodel.ContactsListViewModel;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsListFragment extends Fragment {


    ContactsListViewModel contactsListViewModel;

    ContactsListAdapter adapter;

    @BindView(R.id.contact_recycler_view)
    RecyclerView contactList;
    public ContactsListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ContactsListFragment newInstance(String param1, String param2) {
        ContactsListFragment fragment = new ContactsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ContactsListAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactsListViewModel = ViewModelProviders.of(getActivity()).get(ContactsListViewModel.class);
        contactsListViewModel.fetchContactsFromDatabase();

        contactList.setLayoutManager(new LinearLayoutManager(getContext()));
        contactList.setAdapter(adapter);

        contactsListViewModel = new ViewModelProvider(getActivity()).get(ContactsListViewModel.class);

        observeContactsData();
        observeQueryString();
    }

    private void observeQueryString() {

        contactsListViewModel.getQueryString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                getQueryContactList(s);
            }
        });
    }

    private void getQueryContactList(String s) {
        s = "%"+s +"%";

        contactsListViewModel.initQuery(s);
        contactsListViewModel.queriedContactList.observe(
                this,
                contacts -> adapter.submitList((PagedList<Contact>) contacts));
    }

    private void observeContactsData() {

        contactsListViewModel.contactList.observe(
                this,contacts ->
                        adapter.submitList((PagedList<Contact>) contacts));

    }
}