package com.example.assignment.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>  {
    private ArrayList<User> userList;
    static String id;


    public UserListAdapter() {


    }

    public void updateUserList(List<User> newUserList) {
        userList = new ArrayList<>();
        userList.addAll(newUserList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        if(user != null){
            holder.userName.setText(user.getName());
        }
        Log.d("image r",String.valueOf(user.getImage()));
        Log.d("image o",String.valueOf(R.drawable.ic_baseline_person_24));
        holder.userImage.setImageURI(Uri.parse(user.getImage()));

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0: userList.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_user)
        TextView userName;
        @BindView(R.id.image_user)
        ImageView userImage;
        @BindView(R.id.checkbox)
        ImageView checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
           Context item_view_context=itemView.getContext();

           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   checkbox.setVisibility(View.VISIBLE);


                   return true;
               }
           });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = getAdapterPosition();
                    User user = userList.get(clickPosition);
                    Intent intent =new Intent(item_view_context,DetailActivity.class);

                     id =String.valueOf(user.getId());

                    intent.putExtra("ID",id);
                    // Log.d("abc",id);
                    item_view_context.startActivity(intent);

                }
            });


        }
    }

}

