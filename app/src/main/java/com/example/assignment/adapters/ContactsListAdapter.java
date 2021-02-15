package com.example.assignment.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.activities.ContactsViewActivity;
import com.example.assignment.activities.EditUserDetailActivity;
import com.example.assignment.activities.MainActivity;
import com.example.assignment.models.Contact;
import com.example.assignment.models.User;
import com.example.assignment.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListAdapter extends PagedListAdapter<Contact,ContactsListAdapter.ViewHolder> {

    Context ctx;

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

    public ContactsListAdapter(FragmentActivity activity) {
        super(DIFF__CALLBACK);
        ctx = activity;
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

        holder.itemView.setTag(position);
        if(contact != null){
            holder.userName.setText(contact.getName());
            holder.userNumber.setText(contact.getPhoneNumbers());
            if (contact.getImage() != null)
                holder.userImage.setImageURI(Uri.parse(contact.getImage()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ((MainActivity)ctx).loadFragment();

                Intent intent = new Intent(ctx, ContactsViewActivity.class);
                intent.putExtra("Contact", getItem(position));
                ctx.startActivity(intent);
            }
        });

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
