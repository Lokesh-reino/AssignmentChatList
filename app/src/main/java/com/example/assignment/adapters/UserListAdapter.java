package com.example.assignment.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.assignment.EditAndDeleteInterface;
import com.example.assignment.R;
import com.example.assignment.ItemClickListener;
import com.example.assignment.activities.EditUserDetailActivity;
import com.example.assignment.activities.MainActivity;
import com.example.assignment.models.User;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends PagedListAdapter<User, UserListAdapter.MyViewHolder> {
    //  ItemClickListener itemClickListener;
    EditAndDeleteInterface editAndDeleteInterface;
    static String id;

    Context ctx;
    public ArrayList<User> userArrayList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public static ItemCallback<User> DIFF__CALLBACK = new ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    public UserListAdapter(EditAndDeleteInterface itemClickListener, Activity activity) {
        super(DIFF__CALLBACK);
        //  this.itemClickListener = itemClickListener;
        this.editAndDeleteInterface = itemClickListener;
        viewBinderHelper.setOpenOnlyOne(true);
        ctx = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.userName.setText(user.getName());
        }
        Log.d("image r", String.valueOf(user.getImage()));
        Log.d("image o", String.valueOf(R.drawable.ic_baseline_person_24));

        if (user.getImage() == null || ("").equalsIgnoreCase(user.getImage()))
            holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);
        else
            holder.userImage.setImageURI(Uri.parse(user.getImage()));

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.closeLayout(user.getName());
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(user.getId()));
        holder.txtEdit.setTag(position);
        holder.txtDelete.setTag(position);
        holder.txtEdit.setOnClickListener(v -> {
            int clickPosition = (int) holder.txtEdit.getTag();
            editAndDeleteInterface.edit(clickPosition);
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickPosition = (int) holder.txtDelete.getTag();
                editAndDeleteInterface.delete(clickPosition);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ctx, "inside long", Toast.LENGTH_SHORT).show();
               /* selected_usersList.clear();
                if (long_press_enabled) {
                    long_press_enabled = false;
                }else{
                    long_press_enabled = true;
                    selected_usersList.add(user.getUid());
                    Log.d("abc", "in adapter onLOngClick:" + user.getName());
                }*/
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "inside click", Toast.LENGTH_SHORT).show();
                Log.d("abc", "in adapter onClick:" + user.getName());
                /*if (long_press_enabled) {
                    if (selected_usersList.contains(user.getUid())) {
                        selected_usersList.remove(user.getUid());
                        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    } else {
                        selected_usersList.add(user.getUid());
                        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color._light_green));
                    }
                } else {
                    Intent intentEditUserInfoActivity = new Intent(context, EditUserInfoActivity.class);
                    intentEditUserInfoActivity.putExtra("User", userArrayList.get(position));
                    context.startActivity(intentEditUserInfoActivity);
                }*/
            }
        });
      /*  holder.llUserRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "on Click linear",Toast.LENGTH_LONG).show();
            }
        });

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "on Click image",Toast.LENGTH_LONG).show();
            }
        });*/
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name_user)
        TextView userName;
        @BindView(R.id.image_user)
        ImageView userImage;
        @BindView(R.id.checkbox)
        ImageView checkbox;
        //        @BindView(R.id.txtEdit)
        Button txtEdit;
        //        @BindView(R.id.txtDelete)
        Button txtDelete;
        //        @BindView(R.id.swipelayout)
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout llUserRecycler;


        CreateEntryViewModel createEntryViewModel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);
            llUserRecycler = itemView.findViewById(R.id.llUserRecycler);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


       /* @Override
        public void edit(int clickPosition) {

        }

        @Override
        public void delete(int clickPosition) {

        }*/

       /* @Override
        public void onItemClicked(View view, User user) {
            Log.d("TAG", "onClick in adapter called: " + view.getId());
            Toast.makeText(ctx,"on CLickk Adapter", Toast.LENGTH_SHORT).show();
            if (editAndDeleteInterface != null)
                editAndDeleteInterface.onItemClicked(view, getItem(getAdapterPosition()));
        }

        @Override
        public void onItemLongClicked(View view, User user, int index) {
            Log.d("TAG", "onLongClick boolean called: " + view.getId());
            if (editAndDeleteInterface != null)
                editAndDeleteInterface.onItemLongClicked(view, getItem(getAdapterPosition()), getAdapterPosition());

        }*/

        @Override
        public void onClick(View view) {
           /* if (editAndDeleteInterface != null)
                editAndDeleteInterface.onItemClicked(view, getAdapterPosition());*/
            Toast.makeText(ctx, " click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("TAG", "onLongClick boolean called: " + v.getId());
            Toast.makeText(ctx, "long click", Toast.LENGTH_SHORT).show();
            /*if (editAndDeleteInterface != null)
                editAndDeleteInterface.onItemLongClicked(v, (getAdapterPosition()), getAdapterPosition());*/
            return true;
        }
    }
}