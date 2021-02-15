package com.example.assignment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactPhoneListAdapter extends RecyclerView.Adapter<ContactPhoneListAdapter.ViewHolderView> {

   Context ctx;
    List<String> phone_numbers;

    public ContactPhoneListAdapter(Context ctx, Set<String> phone_numbers)
    {
        this.ctx = ctx;
        this.phone_numbers = new ArrayList<>(phone_numbers);

    }
    @NonNull
    @Override
    public ViewHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_phone, parent, false);
        return new ContactPhoneListAdapter.ViewHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderView holder, int position) {

        holder.tv_contact_phone_num.setText(phone_numbers.get(position));
    }

    @Override
    public int getItemCount() {
        return phone_numbers.size();
    }

    public class ViewHolderView extends RecyclerView.ViewHolder {

        TextView tv_contact_phone_num;
        public ViewHolderView(@NonNull View itemView) {
            super(itemView);

            tv_contact_phone_num = itemView.findViewById(R.id.tv_contact_phone_num);
        }
    }
}
