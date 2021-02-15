package com.example.assignment.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.activities.DetailOfUserDisplay;
import com.example.assignment.activities.EditUserDetailActivity;
import com.example.assignment.helpers.UriFromBitmap;
import com.example.assignment.models.User;
import com.example.assignment.utils.AlertDialogHelper;
import com.example.assignment.utils.Constants;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class EditUserDetailsFragment extends Fragment {


    private CreateEntryViewModel mViewModel;
    @BindView(R.id.image_view_user)
    ImageView userImage;

    @BindView(R.id.editTextDOB)
    EditText userBirthDay;

    @BindView(R.id.editTextPersonName)
    EditText userName;

    @BindView(R.id.editTextPhone1)
    EditText editTextPhone1;

    @BindView(R.id.button_save)
    Button button;

    @BindView(R.id.ivAddProfilePic)
    CircleImageView addProfilePic;

    @BindView(R.id.ivAddContacts2)
    ImageView ivAddContacts;

    @BindView(R.id.ivDeleteContacts3)
    ImageView ivDeleteContacts3;

    @BindView(R.id.ivDeleteContacts2)
    ImageView ivDeleteContacts2;

    @BindView(R.id.llContacts2)
    LinearLayout llContacts2;

    @BindView(R.id.llContacts3)
    LinearLayout llContacts3;

    @BindView(R.id.editTextPhone2)
    EditText editTextPhone2;

    @BindView(R.id.editTextPhone3)
    EditText editTextPhone3;

    Integer id;
    String action;
    FrameLayout frameLayoutUser;

    Menu context_menu;

    User user;
    CreateEntryViewModel viewModel;


    String ProfilePicPath;
    String ProfilePicUri;

    Dialog myDialog;

    private Long createdDate;
    private Long modifiedDate;
    SimpleDateFormat sdf = new SimpleDateFormat("MM dd,yyyy HH:mm");

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    UriFromBitmap uriFromBitmap;


    public static EditUserDetailsFragment newInstance(String param1) {
        EditUserDetailsFragment fragment = new EditUserDetailsFragment();
        return fragment;
    }

    public static Fragment newCreateEntryInstance(int id, Serializable serializable, String s) {
        EditUserDetailsFragment createEntryFragment = new EditUserDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        bundle.putSerializable("User", serializable);
        bundle.putString("Action", s);
        createEntryFragment.setArguments(bundle);
//        this.user = (User)serializable;
        return createEntryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        boolean flag = false;
        id = getArguments().getInt("ID");
        user = (User) bundle.getSerializable("User"); // Key, default value
        action = bundle.getString("Action");
//        Toast.makeText(getContext(), "EditUserDetail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user_details, container, false);
        ButterKnife.bind(this, view);
        // recyclerView = view.findViewById(R.id.user_recycler_view);
        frameLayoutUser = view.findViewById(R.id.fl_edit_user_fragment);
        uriFromBitmap = new UriFromBitmap();
        setHasOptionsMenu(true);
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialog_product);

        init();
        setEditAndViewAction();
        userBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        userBirthDay.setText(date);
                    }
                },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });
        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ivAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llContacts2.getVisibility() == View.VISIBLE && llContacts3.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getActivity(), "Only 3 Contacts are Allowed", Toast.LENGTH_SHORT).show();
                    // ivAddContacts.setVisibility(View.GONE);
                } else if (llContacts2.getVisibility() == View.GONE) {
                    llContacts2.setVisibility(View.VISIBLE);
                } else if (llContacts3.getVisibility() == View.GONE) {
                    llContacts3.setVisibility(View.VISIBLE);
                }

            }
        });


        ivDeleteContacts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContacts2.setVisibility(View.GONE);
                ivAddContacts.setVisibility(View.VISIBLE);
                editTextPhone2.setText("");
//                et_cc2.setText("");
            }
        });
        ivDeleteContacts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContacts3.setVisibility(View.GONE);
                ivDeleteContacts2.setVisibility(View.VISIBLE);
                ivAddContacts.setVisibility(View.VISIBLE);
                editTextPhone3.setText("");
            }
        });

//        Toast.makeText(getContext(), "EditUserDetail in on create view", Toast.LENGTH_SHORT).show();
        return view;
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(CreateEntryViewModel.class);
        viewModel.fetchDetailsFromDatabase(id);
        viewModel.getUser().observe(this, user -> {
            userName.setText(user.getName());
            editTextPhone1.setText(user.getPhoneNumber1());
            userBirthDay.setText(user.getBirthday());
            editTextPhone2.setText(user.getPhoneNumber2());
            editTextPhone3.setText(user.getPhoneNumber3());
            if (user.getImage() != null) {
                userImage.setImageURI(Uri.parse(user.getImage()));
//                userImage.setImageBitmap(loadBitmap(user.getImage()));
            } else {
                Uri imgUri = Uri.parse("android.resource://com.example.assignment/" + R.drawable.ic_baseline_person_24);
                userImage.setImageURI(null);
                userImage.setImageURI(imgUri);
            }
//            userImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (user.getImage() == null || user.getImage().equals("")) {
//                        Toast.makeText(getActivity(), "Profile Image not found", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Intent intent = new Intent(getActivity(), DetailOfUserDisplay.class);
//                        intent.putExtra("User", user.getImage());
//                        startActivity(intent);
//                    }
//                }
//            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = userName.getText().toString();
                    String phoneNumber = editTextPhone1.getText().toString();
                    String phoneNumber2 = editTextPhone2.getText().toString();
                    String phoneNumber3 = editTextPhone3.getText().toString();
                    String birthday = userBirthDay.getText().toString();

                    Long createdDate = user.getCreatedAt();
                    Long modifiedDate = System.currentTimeMillis();
                    User user = new User(name, phoneNumber, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3);
                    viewModel.updateUser(name, birthday, phoneNumber, ProfilePicPath, createdDate, modifiedDate, phoneNumber2, phoneNumber3, id);
                    Toast.makeText(getActivity(), "Successfully Edited", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setEditAndViewAction() {
        if (action.equalsIgnoreCase(Constants.viewActionType)) {
            disableEditableView();
        } else if (action.equalsIgnoreCase(Constants.editActionType)) {
            enableEditableView();
        }
    }

    private void enableEditableView() {
        addProfilePic.setVisibility(View.VISIBLE);
        userName.setEnabled(true);
        editTextPhone1.setEnabled(true);
        userBirthDay.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        //  userImage.setEnabled(true);
        editTextPhone2.setEnabled(true);
        editTextPhone3.setEnabled(true);
        ivAddContacts.setVisibility(View.VISIBLE);
        ivDeleteContacts2.setVisibility(View.VISIBLE);
        ivDeleteContacts3.setVisibility(View.VISIBLE);
    }

    private void disableEditableView() {
        addProfilePic.setVisibility(View.GONE);
        userName.setEnabled(false);
        editTextPhone1.setEnabled(false);
        userBirthDay.setEnabled(false);
        editTextPhone2.setEnabled(false);
        editTextPhone3.setEnabled(false);
        ivAddContacts.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        ivDeleteContacts2.setVisibility(View.GONE);
        ivDeleteContacts3.setVisibility(View.GONE);

        if (user.getPhoneNumber2() != null && !user.getPhoneNumber2().equalsIgnoreCase("")) {
            llContacts2.setVisibility(View.VISIBLE);
        }
        if (user.getPhoneNumber3() != null && !user.getPhoneNumber3().equalsIgnoreCase("")) {
            llContacts3.setVisibility(View.VISIBLE);
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView dialog_name_tv = (TextView) myDialog.findViewById(R.id.dialog_name);
                TextView dialog_phone= (TextView) myDialog.findViewById(R.id.dialog_phone);
                ImageView dialog_image = (ImageView) myDialog.findViewById(R.id.dialog_image);
                dialog_name_tv.setText(user.getName());
                dialog_phone.setText(user.getPhoneNumber1());
                if (user.getImage() != null) {
                    dialog_image.setImageURI(Uri.parse(user.getImage()));
//                userImage.setImageBitmap(loadBitmap(user.getImage()));
                } else {
                    Uri imgUri = Uri.parse("android.resource://com.example.assignment/" + R.drawable.ic_baseline_person_24);
                    dialog_image.setImageURI(null);
                    dialog_image.setImageURI(imgUri);
                }
                myDialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        this.context_menu = menu;

        context_menu.findItem(R.id.edit_User).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getActivity(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getActivity(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        ProfilePicUri = "";
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {

                    checkPermissionAndStartCamera();

                } else if (options[item].equals("Choose from Gallery")) {

                    checkPermissionAndOpenGallery();

                } else
                    dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri = null;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of Tab2Fragment");

                        cameraImageUri = uriFromBitmap.getImageUri(getContext(), bitmapCameraImage);
                        Log.d("TAG", "cameraUri: " + cameraImageUri.toString());
                        Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());

                    } catch (Exception e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }
                    userImage.setImageURI(cameraImageUri);
                    ProfilePicPath = cameraImageUri.getPath();
                    ProfilePicUri = cameraImageUri.toString();

                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    ProfilePicPath = selectedImageUri.getPath();
                    ProfilePicUri = selectedImageUri.toString();
                    userImage.setImageURI(selectedImageUri);
                    break;
            }
        }
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
    }

    public Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

}