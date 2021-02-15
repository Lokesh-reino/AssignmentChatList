package com.example.assignment.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListAdapter extends PagedListAdapter<Contact,ContactsListAdapter.ViewHolder> {


    public static DiffUtil.ItemCallback<Contact> DIFF__CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ContactsListAdapter() {
        super(DIFF__CALLBACK);
    }

    @NonNull
    @Override
    public ContactsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        return new ContactsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsListAdapter.ViewHolder holder, int position) {

        Contact contact = getItem(position);
        if(contact != null){
            holder.userName.setText(contact.getName());
            holder.userNumber.setText(contact.getPhoneNumbers());
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_contact_name)
        TextView userName;
        @BindView(R.id.tv_contact_number)
        TextView userNumber;
        @BindView(R.id.image_user)
        ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
