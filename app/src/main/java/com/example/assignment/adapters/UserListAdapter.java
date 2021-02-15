package com.example.assignment.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.assignment.R;
import com.example.assignment.fragments.ViewUsersInRecyclerFragment;
import com.example.assignment.interfaces.ItemClickListener;
import com.example.assignment.models.User;
import com.example.assignment.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends PagedListAdapter<User,UserListAdapter.MyViewHolder>  {
    private ArrayList<User> userList;
    ItemClickListener itemClickListener;
    static String id;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public ArrayList<User> selected_usersList = new ArrayList<>();
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

    public UserListAdapter(ItemClickListener itemClickListener){
        super(DIFF__CALLBACK);
        this.itemClickListener = itemClickListener;
        viewBinderHelper.setOpenOnlyOne(true);

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
        if(user != null){
            holder.userName.setText(user.getName());

        }

        if (user.getImage() == null || user.getImage().equalsIgnoreCase(""))
            holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);
        else
            holder.userImage.setImageURI(Uri.parse(user.getImage()));


        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.closeLayout(user.getName());
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(user.getId()));

        holder.swipeRevealLayout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {
                Constants.isSwipeOpen = false;
            }

            @Override
            public void onOpened(SwipeRevealLayout view) {
                // Toast.makeText(activity, "open", Toast.LENGTH_SHORT).show();
                Constants.isSwipeOpen = true;
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {
                // Toast.makeText(activity, "slide", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name_user)
        TextView userName;
        @BindView(R.id.image_user)
        ImageView userImage;
        @BindView(R.id.checkbox)
        ImageView checkbox;
        @BindView(R.id.btn_edit)
        Button btn_edit;
        @BindView(R.id.btn_delete)
        Button btn_delete;

        @BindView(R.id.rl_item_view)
        RelativeLayout rl_item_view;

        SwipeRevealLayout swipeRevealLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            btn_edit.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            rl_item_view.setOnClickListener(this);
//            rl_item_view.setOnLongClickListener(this);
            Context item_view_context=itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick in adapter called: " + v.getId());
            if (itemClickListener != null)
            {
                if (v.getId() == R.id.btn_delete)
                {
                    itemClickListener.onDeleteClick(getAdapterPosition());
                }else if (v.getId() == R.id.btn_edit)
                {
                    itemClickListener.onEditClick(getAdapterPosition());
                }else if (v.getId() == R.id.rl_item_view)
                {
                    itemClickListener.onItemClicked(v,getAdapterPosition());
                }

            }

        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("TAG", "onLongClick boolean called: " + v.getId());
            if (itemClickListener != null)

                if (v.getId() == R.id.rl_item_view) {
                    itemClickListener.onItemLongClicked(v , getAdapterPosition());
                }
            return true;
        }
    }

}

