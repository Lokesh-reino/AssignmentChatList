package com.example.assignment;

import android.view.View;

import com.example.assignment.models.User;

public interface EditAndDeleteInterface {
    public void edit(int clickPosition);
    public void delete(int clickPosition);
    void onItemClicked(View view, User user);

    void onItemLongClicked(View view, User user, int index);
}
