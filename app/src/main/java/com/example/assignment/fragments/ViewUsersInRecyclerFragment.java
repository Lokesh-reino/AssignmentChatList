package com.example.assignment.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.assignment.activities.MainActivity;
import com.example.assignment.interfaces.ItemClickListener;
import com.example.assignment.R;
import com.example.assignment.adapters.UserListAdapter;
import com.example.assignment.models.User;
import com.example.assignment.utils.AlertDialogHelper;
import com.example.assignment.utils.Constants;
import com.example.assignment.utils.RecyclerItemClickListener;
import com.example.assignment.utils.RecyclerSectionItemDecoration;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewUsersInRecyclerFragment extends Fragment implements ItemClickListener, AlertDialogHelper.AlertDialogListener {

    private CreateEntryViewModel createEntryViewModel;
    @BindView(R.id.user_recycler_view)
    RecyclerView UserList;
    List<User> userListItem = new ArrayList<>();
    boolean isMultiSelect = false;
    ActionMode mActionMode;
    ArrayList<User> deleteUserList = new ArrayList<>();
    SwipeRevealLayout swipeRevealLayout;
    ArrayList<User> multiselect_list = new ArrayList<>();
    AlertDialogHelper alertDialogHelper;
    Menu context_menu;
    RecyclerView recyclerView;
    FrameLayout frameLayoutUser;

    SendMessage sendMessageUser;


    private UserListAdapter userListAdapter = new UserListAdapter(this);


    public static ViewUsersInRecyclerFragment newInstance() {
        return new ViewUsersInRecyclerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_users_in_recyclerview, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        swipeRevealLayout = view.findViewById(R.id.swipelayout);

        recyclerView = view.findViewById(R.id.user_recycler_view);
        frameLayoutUser = view.findViewById(R.id.fl_edit_user_fragment);
        UserList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), UserList, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.d("TAG", "Default intent called");
                if (isMultiSelect)
                    multi_select(position, view);
                else {
                    if (!Constants.isSwipeOpen)
                        ViewEditActivityCalling(Constants.viewActionType, position);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<>();
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position, view);
            }
        }));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createEntryViewModel = ViewModelProviders.of(getActivity()).get(CreateEntryViewModel.class);
        createEntryViewModel.fetchDataFromDatabase();

        UserList.setLayoutManager(new LinearLayoutManager(getContext()));
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(UserList);

        UserList.setAdapter(userListAdapter);

        createEntryViewModel = new ViewModelProvider(getActivity()).get(CreateEntryViewModel.class);
        alertDialogHelper = new AlertDialogHelper(getContext(), ViewUsersInRecyclerFragment.this);

        observeQueryString();
        observeUsersDataList();
        observeMultiSelectStatus();
    }

    public void multi_select(int position, View view) {
        if (mActionMode != null) {
            if (multiselect_list.contains(userListItem.get(position))) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
                multiselect_list.remove(userListItem.get(position));
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.purple_200));
                multiselect_list.add(userListItem.get(position));
            }
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else {
//                mActionMode.setTitle("0");
                if (context_menu != null) {
                    mActionMode.finish();
                    context_menu.clear();
                    context_menu.close();
                }
                multiselect_list.clear();
                isMultiSelect = false;
            }
            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        /*userListAdapter.selected_usersList = multiselect_list;
        userListAdapter.notifyDataSetChanged();*/
    }


    private void observeMultiSelectStatus() {
        createEntryViewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
//                multiSelectStatus = aBoolean;
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
//                userListItem =  users.snapshot();
                userListAdapter.submitList(users);


//                storeUser(users);

            }
        });
    }

    @Override
    public void onResume() {
        Log.d("abc","On Resume");
        // observeUsersDataList();
        super.onResume();
        if (context_menu != null) {
            context_menu.clear();
            context_menu.close();
        }
        multiselect_list.clear();
        isMultiSelect = false;
//        multiSelectStatus = false;
    }




    private void observeUsersDataList() {
        createEntryViewModel.userList.observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                userListItem =  users.snapshot();
                userListAdapter.submitList(users);


//                storeUser(users);


                @SuppressLint("ResourceType")
                RecyclerSectionItemDecoration sectionItemDecoration =
                        new RecyclerSectionItemDecoration(10,
                                true,
                                getSectionCallback(users));

                UserList.addItemDecoration(sectionItemDecoration);

            }
        });

    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(PagedList<User> user) {
        return new RecyclerSectionItemDecoration.SectionCallback() {

            @Override
            public boolean isSection(int position) {

                Log.d("position",""+position);

                if (position == userListItem.size())
                    position--;

                return position == 0 ||
                        !Constants.getFormattedDate(getActivity(),userListItem.get(position).getCreatedAt()).
                                equals(Constants.getFormattedDate(getActivity(),userListItem.get(position-1).getCreatedAt()));
            }

            @Override
            public CharSequence getSectionHeader(int position) {

                if (position == userListItem.size())
                    position--;

                return Constants.getFormattedDate(getActivity(),userListItem.get(position).getCreatedAt());

            }
        };
    }

    @Override
    public void onItemClicked(View view,int position) {

        /*if (isMultiSelect)
            multi_select(position, view);
        else {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
            ViewEditActivityCalling(Constants.viewActionType, position);
        }*/
    }

    @Override
    public void onItemLongClicked(View view,  int position) {
        if (!isMultiSelect) {
            multiselect_list = new ArrayList<>();
            isMultiSelect = true;
            if (mActionMode == null) {
                mActionMode = getActivity().startActionMode(mActionModeCallback);
            }
        }
        multi_select(position, view);

    }

    @Override
    public void onEditClick(int clickPosition) {

//        addUserForEditDelete();
        ViewEditActivityCalling(Constants.editActionType, clickPosition);

    }
    private void ViewEditActivityCalling(String edit_view, int clickPosition) {
        addUserForEditDelete();
//        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
//        intent.putExtra("ID", String.valueOf(userListItem.get(clickPosition).getId()));
//        intent.putExtra("User", userListItem.get(clickPosition));
//        intent.putExtra(Constants.actionType, edit_view);
//        getActivity().startActivity(intent);
//        recyclerView.setVisibility(View.GONE);
//        frameLayoutUser.setVisibility(View.VISIBLE);
//        EditUserDetailsFragment fragment = new EditUserDetailsFragment(); //Your Fragment
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("User", (Serializable) userListItem.get(clickPosition));
//        bundle.putString(Constants.actionType,edit_view);
//        fragment.setArguments(bundle);
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fl_edit_user_fragment, fragment)
//                .addToBackStack(null)
//                .commit();

        ((MainActivity) getActivity()).switchToCreateEntryFragment(userListItem.get(clickPosition).getId(),(Serializable) userListItem.get(clickPosition),edit_view);
    }

    interface SendMessage {
        void sendData(User user);
    }


    private void addUserForEditDelete() {
        createEntryViewModel.userList.observe(getActivity(), users -> {
            if (users != null && users.size() > 0) {
//                storeUser(users);
            }
        });
    }

    /*private void storeUser(List<User> users) {
        userListItem.clear();
        userListItem.addAll(users);
    }*/

    @Override
    public void onDeleteClick(int clickPosition) {
        createEntryViewModel.deleteUserFromDatabase(userListItem.get(clickPosition).getId());

        createEntryViewModel.userList.observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
//                storeUser(users);
                userListItem =  users.snapshot();

            }
        });
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
    public void onPositiveClick(int from) {

        if (from == 1) {
            if (multiselect_list.size() > 0) {
                userListItem = multiselect_list;
                for (int i = 0; i < multiselect_list.size(); i++)
                    createEntryViewModel.deleteUserFromDatabase(userListItem.get(i).getId());
                userListAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                    multiselect_list.clear();
                    refreshAdapter();
                }
                Toast.makeText(getActivity(), "Delete Click", Toast.LENGTH_SHORT).show();
            }
        } else if (from == 2) {
            if (mActionMode != null) {
                mActionMode.finish();
                multiselect_list.clear();
                refreshAdapter();
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {
        mActionModeCallback.onDestroyActionMode(mActionMode);
    }

    //Important
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        Menu menu;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            this.menu = menu;
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("", "Are you sure want to delete all the selected contacts(s)?", "DELETE", "CANCEL", 1, false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<>();
            refreshAdapter();
        }
    };


    @Override
    public void onPause() {
        Log.d("abc","On Pause");
        super.onPause();
        frameLayoutUser.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "onUserList", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Log.d("abc","On Destroy");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d("abc","On Stop");
        super.onStop();
    }

    @Override
    public void onAttach(@NonNull @NotNull Activity activity) {
        Log.d("abc","On Attach");
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        Log.d("abc","On Pause");
        super.onStart();
    }
}