package com.example.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.R;
import com.example.assignment.models.User;
import com.example.assignment.viewmodel.CreateEntryViewModel;

public class EditUserDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    CreateEntryViewModel viewModel;
    String id;
    ImageView imageViewUserProfilePic;
    Button button;
    EditText editTextName, editTextPhone, editTextBirthday;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_detail);
        init();
    }

    private void init() {
        imageViewUserProfilePic = findViewById(R.id.image_view_user);
        editTextName = findViewById(R.id.editTextPersonName);
        editTextBirthday = findViewById(R.id.editTextDOB);
        editTextPhone = findViewById(R.id.editTextPhone);
        button = findViewById(R.id.button_save);

        user = (User) getIntent().getSerializableExtra("User");

        viewModel = ViewModelProviders.of(this).get(CreateEntryViewModel.class);

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Edit User Details");
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
            editTextPhone.setText(user.getPhoneNumber());
            editTextBirthday.setText(user.getBirthday());
            if (user.getImage() != null) {
                imageViewUserProfilePic.setImageURI(Uri.parse(user.getImage()));
            }
            else
            {
            Uri imgUri=Uri.parse("android.resource://com.example.assignment/"+R.drawable.ic_baseline_person_24);
            imageViewUserProfilePic.setImageURI(null);
            imageViewUserProfilePic.setImageURI(imgUri);}

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editTextName.getText().toString();
                    String phoneNumber = editTextPhone.getText().toString();
                    String birthday = editTextBirthday.getText().toString();
                    User user = new User(name, phoneNumber, birthday, "uri",System.currentTimeMillis(),System.currentTimeMillis());
                    viewModel.updateUser(name, birthday, phoneNumber, Integer.parseInt(id));
                    Toast.makeText(EditUserDetailActivity.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditUserDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        });
    }
}