package com.example.assignment.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.helpers.UriFromBitmap;
import com.example.assignment.models.User;
import com.example.assignment.utils.Constants;
import com.example.assignment.viewmodel.CreateEntryViewModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    CreateEntryViewModel viewModel;
    String id;
    CircleImageView userImage;
    CircleImageView addProfilePic;
    Button button;
    EditText editTextName, editTextPhone1, editTextBirthday;
    User user;
    String action;
    String ProfilePicPath;
    String ProfilePicUri = "";
    UriFromBitmap uriFromBitmap;
    ImageView ivAddContacts;

    ImageView ivDeleteContacts3;

    ImageView ivDeleteContacts2;

    LinearLayout llContacts2;

    LinearLayout llContacts3;

    EditText editTextPhone2;

    EditText editTextPhone3;
    EditText et_cc2, et_cc1;

    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    EditText userBirthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_detail);

        init();
        setEditAndViewAction();
        userBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditUserDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(EditUserDetailActivity.this, "Only 3 Contacts are Allowed", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
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
                Toast.makeText(this, "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(this, "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    /*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                binding.ivAvatar.setImageBitmap(photo);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                binding.ivAvatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }
*/
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

                        cameraImageUri = uriFromBitmap.getImageUri(EditUserDetailActivity.this, bitmapCameraImage);
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

    private void selectImage() {
        ProfilePicUri = "";
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserDetailActivity.this);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
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
        editTextName.setEnabled(true);
        editTextPhone1.setEnabled(true);
        editTextBirthday.setEnabled(true);
        button.setVisibility(View.VISIBLE);
      //  userImage.setEnabled(true);
        editTextPhone2.setEnabled(true);
        editTextPhone3.setEnabled(true);
        ivAddContacts.setVisibility(View.VISIBLE);
        ivDeleteContacts2.setVisibility(View.VISIBLE);
        ivDeleteContacts3.setVisibility(View.VISIBLE);

    }

    private void disableEditableView() {

      //  userImage.setEnabled(false);
        addProfilePic.setVisibility(View.GONE);
        editTextName.setEnabled(false);
        editTextPhone1.setEnabled(false);
        editTextBirthday.setEnabled(false);
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

    }

    private void init() {

        ivDeleteContacts3 = findViewById(R.id.ivDeleteContacts3);
        ;


        ivDeleteContacts2 = findViewById(R.id.ivDeleteContacts2);
        ;

        llContacts2 = findViewById(R.id.llContacts2);
        ;


        llContacts3 = findViewById(R.id.llContacts3);
        ;

        editTextPhone2 = findViewById(R.id.editTextPhone2);
        ;

        editTextPhone3 = findViewById(R.id.editTextPhone3);
        ;
        addProfilePic = findViewById(R.id.ivAddProfilePic);
        ivAddContacts = findViewById(R.id.ivAddContacts2);
        userBirthDay = findViewById(R.id.editTextDOB);
        et_cc2 = findViewById(R.id.et_cc2);
        et_cc1 = findViewById(R.id.et_cc1);
        userImage = findViewById(R.id.image_view_user);
        editTextName = findViewById(R.id.editTextPersonName);
        editTextBirthday = findViewById(R.id.editTextDOB);
        editTextPhone1 = findViewById(R.id.editTextPhone1);
        button = findViewById(R.id.button_save);
        uriFromBitmap = new UriFromBitmap();
        user = (User) getIntent().getSerializableExtra("User");
        action = getIntent().getStringExtra(Constants.actionType);

        viewModel = ViewModelProviders.of(this).get(CreateEntryViewModel.class);

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("" + user.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id = getIntent().getStringExtra("ID");
        viewModel.fetchDetailsFromDatabase(Integer.parseInt(id));
        viewModel.getUser().observe(this, user -> {
            editTextName.setText(user.getName());
            editTextPhone1.setText(user.getPhoneNumber1());
            editTextBirthday.setText(user.getBirthday());
            editTextPhone1.setText(user.getPhoneNumber1());
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
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.getImage() == null || user.getImage().equals("")) {
                        Toast.makeText(EditUserDetailActivity.this, "Profile Image not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(EditUserDetailActivity.this, DetailOfUserDisplay.class);
                        intent.putExtra("User", user.getImage());
                        startActivity(intent);
                    }
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editTextName.getText().toString();
                    String phoneNumber = editTextPhone1.getText().toString();
                    String phoneNumber2 = editTextPhone2.getText().toString();
                    String phoneNumber3 = editTextPhone3.getText().toString();
                    String birthday = editTextBirthday.getText().toString();

                    Long createdDate = user.getCreatedAt();
                    Long modifiedDate = System.currentTimeMillis();
                    User user = new User(name, phoneNumber, birthday, ProfilePicUri, createdDate, modifiedDate, phoneNumber2, phoneNumber3);
//                    viewModel.updateUseruser(user);
                    viewModel.updateUser(name, birthday, phoneNumber, ProfilePicPath, createdDate, modifiedDate, phoneNumber2, phoneNumber3, Integer.parseInt(id));
                    Toast.makeText(EditUserDetailActivity.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(EditUserDetailActivity.this, MainActivity.class);
                    startActivity(intent);*/

                    finish();
                }
            });
        });
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