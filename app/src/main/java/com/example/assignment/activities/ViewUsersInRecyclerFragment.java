package com.example.assignment.activities;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.assignment.EditAndDeleteInterface;
import com.example.assignment.ItemClickListener;
import com.example.assignment.R;
import com.example.assignment.adapters.UserListAdapter;
import com.example.assignment.models.User;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewUsersInRecyclerFragment extends Fragment implements  EditAndDeleteInterface {

    private CreateEntryViewModel createEntryViewModel;
    @BindView(R.id.user_recycler_view)
    RecyclerView UserList;
    boolean multiSelectStatus = false;
    List<User> currentUserList;
    ArrayList<User> deleteUserList = new ArrayList<>();
    Button txtEdit, txtDelete;
    SwipeRevealLayout swipeRevealLayout;
    ArrayList<User> userList;
    private UserListAdapter userListAdapter = new UserListAdapter((EditAndDeleteInterface) this, getActivity());


    public static ViewUsersInRecyclerFragment newInstance() {
        return new ViewUsersInRecyclerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_users_in_recyclerview, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        currentUserList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createEntryViewModel = ViewModelProviders.of(getActivity()).get(CreateEntryViewModel.class);
        createEntryViewModel.fetchDataFromDatabase();

        UserList.setLayoutManager(new LinearLayoutManager(getContext()));

       //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(UserList);

        txtDelete = view.findViewById(R.id.txtDelete);
        txtEdit = view.findViewById(R.id.txtEdit);
        swipeRevealLayout = view.findViewById(R.id.swipelayout);

        UserList.setAdapter(userListAdapter);

        createEntryViewModel = new ViewModelProvider(getActivity()).get(CreateEntryViewModel.class);

        observeForDbChanges();
        observeQueryString();
        observeUsersDataList();
        observeMultiSelectStatus();
    }

    private void observeMultiSelectStatus() {
        createEntryViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                multiSelectStatus = aBoolean;
            }
        });
    }

    private void observeQueryString() {
        createEntryViewModel.getQueryString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String query) {
                Log.d("TAG", "Inside Tab1Fragment: " + query);
                queryChatList(query);
            }
        });
    }

    private void queryChatList(String query) {
        query = "%" + query + "%";
        createEntryViewModel.queryInit(query);
        createEntryViewModel.queriedUserList.observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                userListAdapter.submitList(users);
            }
        });
    }

    @Override
    public void onResume() {
        // observeUsersDataList();
        super.onResume();
        Log.e("TAG", "onResume: ");
    }

//    ItemTouchHelper.SimpleCallback

   /* ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        ArrayList<User> userList;

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            userListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            createEntryViewModel.userList.observe(getActivity(), users -> {
                if (users != null && users.size() > 0) {
                    storeUser(users);
                }
            });


            final CharSequence[] options = {"View Details", "Edit", "Delete", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Options");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Toast.makeText(getActivity(), "On Click", Toast.LENGTH_SHORT).show();
                    if (options[item].equals("View Details")) {
                        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
                        intent.putExtra("ID", String.valueOf(userList.get(viewHolder.getAdapterPosition()).getId()));
                        getActivity().startActivity(intent);


                    } else if (options[item].equals("Edit")) {
                        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
                        intent.putExtra("ID", String.valueOf(userList.get(viewHolder.getAdapterPosition()).getId()));
                        getActivity().startActivity(intent);

                    } else if (options[item].equals("Delete")) {
                        Log.d("abc", "deletion");
                        createEntryViewModel.deleteUserFromDatabase(userList.get(viewHolder.getAdapterPosition()).getId());

                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

        private void storeUser(List<User> users) {
            userList = new ArrayList<>();
            userList.addAll(users);
        }
    };
*/
    private void observeUsersDataList() {
        createEntryViewModel.userList.observe(this, users -> userListAdapter.submitList(users));

    }

    private void observeForDbChanges() {

        createEntryViewModel.userList.observe(getViewLifecycleOwner(), new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
//                currentUserList.clear();
                currentUserList = users.snapshot();
                userListAdapter.submitList(users);
            }
        });

    }

    @Override
    public void onItemClicked(View view, User user) {
        Log.d("TAG", String.valueOf(multiSelectStatus));
        if (multiSelectStatus) {
            if (!deleteUserList.contains(user)) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
                deleteUserList.add(user);
            } else {
                deleteUserList.remove(user);
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_500));
            }

        } else {
            Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
            intent.putExtra("ID", String.valueOf(user.getId()));
            getActivity().startActivity(intent);

        }
        Log.d("TAG", "Default intent called");
    }

    @Override
    public void onItemLongClicked(View view, User user, int index) {
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
        deleteUserList.add(user);
        Log.d("TAG", "LongItemClick: " + index);
        createEntryViewModel.setIsMultiSelect(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.multi_select_delete_menu) {

            for (User user : deleteUserList) {
                createEntryViewModel.deleteUserFromDatabase(user.getId());
            }

            deleteUserList.clear();
            createEntryViewModel.setIsMultiSelect(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void edit(int clickPosition) {

       addUserForEditDelete();

        Toast.makeText(getActivity(), "onedit", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
        intent.putExtra("ID", String.valueOf(userList.get(clickPosition).getId()));
        getActivity().startActivity(intent);
    }

    private void addUserForEditDelete() {
        createEntryViewModel.userList.observe(getActivity(), users -> {
            if (users != null && users.size() > 0) {
                storeUser(users);
            }
        });
    }

    private void storeUser(List<User> users) {
        userList = new ArrayList<>();
        userList.addAll(users);
    }

    @Override
    public void delete(int clickPosition) {
        addUserForEditDelete();
        createEntryViewModel.deleteUserFromDatabase(userList.get(clickPosition).getId());
    }
}