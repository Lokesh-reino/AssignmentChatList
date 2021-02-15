package com.example.assignment.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.activities.MainActivity;
import com.example.assignment.adapters.ContactsListAdapter;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;
import com.example.assignment.repository.AppDatabase;
import com.example.assignment.utils.RecyclerSectionItemDecoration;
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
    ContactsListAdapter adapter = new ContactsListAdapter();
    List<Contact> listOfContacts = new ArrayList<>();
    Integer size = 0;
    PagedList<Contact> contacts;

    @BindView(R.id.contact_recycler_view)
    RecyclerView contactList;

    @BindView(R.id.tv_number_of_contacts)
    TextView textViewContactCount;


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
        Log.d("abc","OnCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        ButterKnife.bind(this, view);

//        int contactCount = contactsListViewModel.getCountOfContacts();
//        textViewContactCount.setText("Contacts("+contactCount+")");
//        setSupportActionBar(toolbar);
        textViewContactCount.setText("Contacts("+ MainActivity.countContacts +")");
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
//        listOfContacts =

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
                contacts -> {
                    adapter.submitList((PagedList<Contact>) contacts);
                size = ((PagedList<Contact>) contacts).size();});
        Toast.makeText(getActivity(), size.toString(), Toast.LENGTH_SHORT).show();
    }

    private void observeContactsData() {

        contactsListViewModel.contactList.observe(
                this,contacts ->{
                        adapter.submitList((PagedList<Contact>) contacts);
//                size = ((PagedList<Contact>) contacts).size();
                });
    }
}