package com.example.assignment.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.activities.MainActivity;
import com.example.assignment.helpers.UriFromBitmap;
import com.example.assignment.models.User;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

//import android.widget.Toolbar;

public class CreateEntryFragment extends Fragment{

    private CreateEntryViewModel mViewModel;
    @BindView(R.id.image_view_user)
    ImageView userImage;

    @BindView(R.id.editTextDOB)
    EditText userBirthDay;

    @BindView(R.id.editTextPersonName)
    EditText userName;

    @BindView(R.id.editTextPhone1)
    EditText editPhoneNumber1;

    @BindView(R.id.button_save)
    Button button;
    //    @BindView(R.id.date_picker)
//    TextView datePick;
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

    @BindView(R.id.et_cc1)
    EditText et_cc1;

    @BindView(R.id.et_cc2)
    EditText et_cc2;

    @BindView(R.id.et_cc3)
    EditText et_cc3;

    @BindView(R.id.button_edit)
    Button buttonEdit;


    String ProfilePicPath;
    String ProfilePicUri;
    int id = 0;
    boolean flag=false;

    private Long createdDate;
    private Long modifiedDate;
    SimpleDateFormat sdf = new SimpleDateFormat("MM dd,yyyy HH:mm");

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    UriFromBitmap uriFromBitmap;

    public static CreateEntryFragment newInstance() {
        return new CreateEntryFragment();
    }

    public static Fragment newCreateEntryInstance(int id) {
        CreateEntryFragment createEntryFragment = new CreateEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        createEntryFragment.setArguments(bundle);
        return createEntryFragment;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_entry_layout, container, false);
        ButterKnife.bind(this, view);
        uriFromBitmap = new UriFromBitmap();
        mViewModel = new ViewModelProvider(getActivity()).get(CreateEntryViewModel.class);

        if (getArguments() != null) {
            boolean flag = false;
            buttonEdit.setVisibility(View.VISIBLE);
            id = getArguments().getInt("ID");
            mViewModel.fetchDetailsFromDatabase(id);
            mViewModel.getUser().observe(this, user -> {
                Log.d("image", "image is" + user.getImage());
                if (user.getImage() != null) {
                    Glide.with(this).load(Uri.parse(user.getImage()))
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(userImage);
                } else {
                    userImage.setImageResource(R.drawable.ic_baseline_person_24);
                }
                userName.setText(user.getName());
                editPhoneNumber1.setText(user.getPhoneNumber1());
                et_cc1.setText(user.getPhoneNumber1());

                Log.d("TAG", user.getPhoneNumber1());
                if (user.getPhoneNumber2() != null || !user.getPhoneNumber2().equalsIgnoreCase("")) {

                    llContacts2.setVisibility(View.VISIBLE);
                    editTextPhone2.setText(user.getPhoneNumber2());
                    et_cc2.setText(user.getPhoneNumber2());
                }
                if (user.getPhoneNumber3() != null || !user.getPhoneNumber3().equalsIgnoreCase("")) {

                    llContacts3.setVisibility(View.VISIBLE);
                    editTextPhone3.setText(user.getPhoneNumber3());
                    et_cc3.setText(user.getPhoneNumber3());
                }
                userBirthDay.setText(user.getBirthday());
                userName.setEnabled(false);
                editPhoneNumber1.setEnabled(false);
                editTextPhone2.setEnabled(false);
                editTextPhone3.setEnabled(false);
                userBirthDay.setEnabled(false);
                et_cc1.setEnabled(false);
                et_cc2.setEnabled(false);
                et_cc3.setEnabled(false);
                addProfilePic.setVisibility(View.GONE);
                createdDate = user.getCreatedAt();
            });
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName.setEnabled(true);
                    editPhoneNumber1.setEnabled(true);
                    editTextPhone2.setEnabled(true);
                    editTextPhone3.setEnabled(true);
                    userBirthDay.setEnabled(true);
                    et_cc1.setEnabled(true);
                    et_cc2.setEnabled(true);
                    et_cc3.setEnabled(true);
                    addProfilePic.setVisibility(View.VISIBLE);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = userName.getText().toString();
                    String phoneNumber1;
                    String phoneNumber2;
                    String phoneNumber3;
                    if (et_cc1.getText().toString().equals("")) {
                        phoneNumber1 = "+91" + editPhoneNumber1.getText().toString();
                    } else {
                        phoneNumber1 = et_cc2.getText().toString() + editPhoneNumber1.getText().toString();
                    }
                    if (et_cc2.getText().toString().equals("")) {
                        phoneNumber2 = "+91" + editTextPhone2.getText().toString();
                    } else {
                        phoneNumber2 = et_cc2.getText().toString() + editTextPhone2.getText().toString();
                    }
                    if (et_cc3.getText().toString().equals("")) {
                        phoneNumber3 = "+91" + editTextPhone3.getText().toString();
                    } else {
                        phoneNumber3 = et_cc3.getText().toString() + editTextPhone3.getText().toString();
                    }
                    String birthday = userBirthDay.getText().toString();
                    if (ProfilePicUri == null) {
                        mViewModel.getUser().observe(getActivity(), user -> {
                            ProfilePicUri = user.getImage();
                        });
                    }

                    modifiedDate = System.currentTimeMillis();
                    Date date = new Date(createdDate);
                    mViewModel.updateUser(name, phoneNumber1, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3, id);
                    ((MainActivity) getActivity()).switchToViewUserFragment();
                    userName.setEnabled(false);
                    editPhoneNumber1.setEnabled(false);
                    editTextPhone2.setEnabled(false);
                    editTextPhone3.setEnabled(false);
                    userBirthDay.setEnabled(false);
                    et_cc1.setEnabled(false);
                    et_cc2.setEnabled(false);
                    et_cc3.setEnabled(false);
                    addProfilePic.setVisibility(View.VISIBLE);

                    Log.d("Here","here");
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
                    et_cc2.setText("");
                }
            });
            ivDeleteContacts3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llContacts3.setVisibility(View.GONE);
                    ivDeleteContacts2.setVisibility(View.VISIBLE);
                    ivAddContacts.setVisibility(View.VISIBLE);
                    editTextPhone3.setText("");
                    et_cc3.setText("");
                }
            });

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
                    selectImage(getContext());
                }
            });
        }

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
                et_cc2.setText("");
            }
        });
        ivDeleteContacts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContacts3.setVisibility(View.GONE);
                ivDeleteContacts2.setVisibility(View.VISIBLE);
                ivAddContacts.setVisibility(View.VISIBLE);
                editTextPhone3.setText("");
                et_cc3.setText("");
            }
        });

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String phoneNumber1 = et_cc1.getText().toString() + "" + editPhoneNumber1.getText().toString();
                String phoneNumber2 = et_cc2.getText().toString() + "" + editTextPhone2.getText().toString();
                String phoneNumber3 = et_cc3.getText().toString() + "" + editTextPhone3.getText().toString();
                String birthday = userBirthDay.getText().toString();
                createdDate = System.currentTimeMillis();
                modifiedDate = System.currentTimeMillis();
                Date date = new Date(createdDate);


                User user = new User(name, phoneNumber1, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3);
                mViewModel.saveToDatabase(user);
                Log.d("ABC", String.valueOf(date));
                Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                clearFields();
                changeTab();
                Log.d("simongoback","go back simorn");
            }
        });

        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });
        return view;
    }


    private void selectImage(Context context) {
        ProfilePicUri = "";
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
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

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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


    private void changeTab() {
        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        viewPager.setCurrentItem(0, true);
    }

    private void clearFields() {
        userImage.setImageResource(R.drawable.ic_baseline_person_24);
        userName.setText("");
        editPhoneNumber1.setText("");
        userBirthDay.setText("");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(CreateEntryViewModel.class);
    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.button_save:
//                if(flag == true && getArguments()!=null) {
//                    String name = userName.getText().toString();
//                    String phoneNumber1 = et_cc1.getText().toString() + "" + editPhoneNumber1.getText().toString();
//                    String phoneNumber2 = et_cc2.getText().toString() + "" + editTextPhone2.getText().toString();
//                    String phoneNumber3 = et_cc3.getText().toString() + "" + editTextPhone3.getText().toString();
//                    String birthday = userBirthDay.getText().toString();
//                    createdDate = System.currentTimeMillis();
//                    modifiedDate = System.currentTimeMillis();
//                    Date date = new Date(createdDate);
//
//
//                    User user = new User(name, phoneNumber1, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3);
//                    mViewModel.saveToDatabase(user);
//                    Log.d("ABC", String.valueOf(date));
//                    Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
//                    clearFields();
//                    changeTab();
//                    flag = false;
//                }
//                else
//                {
//                    String name = userName.getText().toString();
//                    String phoneNumber1;
//                    String phoneNumber2;
//                    String phoneNumber3;
//                    if (et_cc1.getText().toString().equals("")) {
//                        phoneNumber1 = "+91" + editPhoneNumber1.getText().toString();
//                    } else {
//                        phoneNumber1 = et_cc2.getText().toString() + editPhoneNumber1.getText().toString();
//                    }
//                    if (et_cc2.getText().toString().equals("")) {
//                        phoneNumber2 = "+91" + editTextPhone2.getText().toString();
//                    } else {
//                        phoneNumber2 = et_cc2.getText().toString() + editTextPhone2.getText().toString();
//                    }
//                    if (et_cc3.getText().toString().equals("")) {
//                        phoneNumber3 = "+91" + editTextPhone3.getText().toString();
//                    } else {
//                        phoneNumber3 = et_cc3.getText().toString() + editTextPhone3.getText().toString();
//                    }
//                    String birthday = userBirthDay.getText().toString();
//                    if (ProfilePicUri == null) {
//                        mViewModel.getUser().observe(getActivity(), user -> {
//                            ProfilePicUri = user.getImage();
//                        });
//                    }
//
//                    modifiedDate = System.currentTimeMillis();
//                    Date date = new Date(createdDate);
//
//
//                    mViewModel.updateUser(name, phoneNumber1, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3, id);
//                    ((MainActivity) getActivity()).switchToViewUserFragment();
//                    userName.setEnabled(false);
//                    editPhoneNumber1.setEnabled(false);
//                    editTextPhone2.setEnabled(false);
//                    editTextPhone3.setEnabled(false);
//                    userBirthDay.setEnabled(false);
//                    et_cc1.setEnabled(false);
//                    et_cc2.setEnabled(false);
//                    et_cc3.setEnabled(false);
//                    addProfilePic.setVisibility(View.VISIBLE);
//                    flag = false;
//                }
//                break;
//            case R.id.button_edit:
//                userName.setEnabled(true);
//                editPhoneNumber1.setEnabled(true);
//                editTextPhone2.setEnabled(true);
//                editTextPhone3.setEnabled(true);
//                userBirthDay.setEnabled(true);
//                et_cc1.setEnabled(true);
//                et_cc2.setEnabled(true);
//                et_cc3.setEnabled(true);
//                addProfilePic.setVisibility(View.VISIBLE);
//                flag = true;
//                break;
//            case R.id.ivAddContacts2:
//                if (llContacts2.getVisibility() == View.VISIBLE && llContacts3.getVisibility() == View.VISIBLE) {
//                    Toast.makeText(getActivity(), "Only 3 Contacts are Allowed", Toast.LENGTH_SHORT).show();
//                    // ivAddContacts.setVisibility(View.GONE);
//                } else if (llContacts2.getVisibility() == View.GONE) {
//                    llContacts2.setVisibility(View.VISIBLE);
//                } else if (llContacts3.getVisibility() == View.GONE) {
//                    llContacts3.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.ivAddProfilePic:
//                selectImage(getContext());
//                break;
//            case R.id.ivDeleteContacts2:
//                llContacts2.setVisibility(View.GONE);
//                ivAddContacts.setVisibility(View.VISIBLE);
//                editTextPhone2.setText("");
//                et_cc2.setText("");
//                break;
//            case R.id.ivDeleteContacts3:
//                llContacts3.setVisibility(View.GONE);
//                ivDeleteContacts2.setVisibility(View.VISIBLE);
//                ivAddContacts.setVisibility(View.VISIBLE);
//                editTextPhone3.setText("");
//                et_cc3.setText("");
//                break;
//            case R.id.editTextDOB:
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                        month = month + 1;
//                        String date = dayOfMonth + "/" + month + "/" + year;
//                        userBirthDay.setText(date);
//                    }
//                },
//                        Calendar.getInstance().get(Calendar.YEAR),
//                        Calendar.getInstance().get(Calendar.MONTH),
//                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.show();
//                break;
//        }
//
//    }
}