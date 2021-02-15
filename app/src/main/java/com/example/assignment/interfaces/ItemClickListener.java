package com.example.assignment.interfaces;

import android.view.View;

import com.example.assignment.models.User;

public interface ItemClickListener {
    void onItemClicked(View view, int position);

    void onItemLongClicked(View view, int index);

    void onEditClick(int clickPosition);

    void onDeleteClick(int clickPosition);
}
