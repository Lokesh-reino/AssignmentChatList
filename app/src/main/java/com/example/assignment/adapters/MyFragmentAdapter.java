package com.example.assignment.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assignment.fragments.ContactsListFragment;
import com.example.assignment.fragments.CreateEntryFragment;
import com.example.assignment.fragments.ViewUsersInRecyclerFragment;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    public MyFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new CreateEntryFragment();
            case 2: return new ContactsListFragment();
            default: return new ViewUsersInRecyclerFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "USER LIST";
            case 1: return "CREATE USER ENTRY";
            case 2: return "CONTACT LIST";
        }
        return null;

    }
}
