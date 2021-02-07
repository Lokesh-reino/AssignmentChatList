package com.example.assignment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.viewmodel.CreateEntryViewModel;

public class DetailsOfUserActivity extends AppCompatActivity {
    CreateEntryViewModel viewModel;
    Toolbar toolbar;
    String id;
    ImageView imageView;
    TextView textViewName, textViewPhone, textViewBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_user);
        init();
    }

    private void init() {
        imageView = findViewById(R.id.image_view_user);
        textViewName = findViewById(R.id.textPersonName);
        textViewBirthday = findViewById(R.id.textBirthDate);
        textViewPhone = findViewById(R.id.textPhone);


        viewModel = ViewModelProviders.of(this).get(CreateEntryViewModel.class);

        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("User Details");
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
            imageView.setImageURI(Uri.parse(user.getImage()));
            textViewName.setText("Name: " + user.getName());
            textViewPhone.setText("Mobile: " + user.getPhoneNumber());
            textViewBirthday.setText("BirthDate: " + user.getBirthday());
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.delete:

                viewModel.deleteUserFromDatabase(Integer.parseInt(id));
                finish();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit:
                intent = new Intent(this, EditUserDetailActivity.class);

                id = getIntent().getStringExtra("ID");

                intent.putExtra("ID", id);
                // Log.d("abc",id);
                startActivity(intent);
                Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}