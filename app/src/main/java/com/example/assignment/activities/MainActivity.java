package com.example.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.adapters.MyFragmentAdapter;
import com.example.assignment.fragments.TestFragment;
import com.example.assignment.repository.ContactsRipo;
import com.example.assignment.viewmodel.ContactsListViewModel;
import com.example.assignment.viewmodel.CreateEntryViewModel;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.assignment.utils.Constants.REQUEST_READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    MyFragmentAdapter adapter;
    MenuItem searchViewItem;
    int pagePosition = -1;
    private CompositeDisposable compositeDisposable =
            new CompositeDisposable();

    public static ContactsListViewModel contactsListViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getContacts();
    }

    private void getContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ) {

                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_READ_CONTACTS);

            } else {
                requestContacts();
            }
        }else{
            requestContacts();
        }
    }

    private void init() {


        toolbar = findViewById(R.id.tool_bar);
//        toolbar.setTitle("User Info");


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle(adapter.getPageTitle(viewPager.getVerticalScrollbarPosition()));
        pagePosition = viewPager.getVerticalScrollbarPosition();
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contactsListViewModel = new ViewModelProviders().of(this).get(ContactsListViewModel.class);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(adapter.getPageTitle(position));
                pagePosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        searchViewItem = menu.findItem(R.id.app_bar_search);
        final android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                if (pagePosition == 0)
                    CreateEntryViewModel.setQueryString(query);

                else if (pagePosition == 2)
                    ContactsListViewModel.setQueryString(query);

                Log.d("TAG", "Inside onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (pagePosition == 0)
                    CreateEntryViewModel.setQueryString(newText);

                else if (pagePosition == 2)
                    ContactsListViewModel.setQueryString(newText);


                Log.d("TAG", "Inside onQueryTextChange: " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestContacts();
                } else {

                    Toast.makeText(this, "Failed. Please grant contacts permission", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private void requestContacts() {

        compositeDisposable.add(ContactsRipo
                .fetch(this)
//                .filter(m -> m.getInVisibleGroup() == 1)
//                .toSortedList(Contact::compareTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {


                   /* for (Contact contact: (List<Contact>)contacts)
                    {
                        contactsListViewModel.saveAllContacts(contact);

                    }*/
//                    contactsListViewModel.saveAllContacts((List<Contact>) contacts);

                   /* ContactAdapter adapter = getContactAdapter();
                    adapter.setContacts(contacts);
                    adapter.notifyDataSetChanged();*/
                }, it -> {
                    //Handle exception
                }));
    }

    @SuppressLint("ResourceType")
    public void loadFragment()
    {

        // create a frame layout
        FrameLayout fragmentLayout = new FrameLayout(this);
        // set the layout params to fill the activity
        fragmentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // set an id to the layout
        fragmentLayout.setId(1000); // some positive integer
        // set the layout as Activity content
        setContentView(fragmentLayout);
        // Finally , add the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("test")
                .replace(1000,new TestFragment()).commit();  // 1000 - is the id set for the container layout

    }


}