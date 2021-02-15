package com.example.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.adapters.ContactPhoneListAdapter;
import com.example.assignment.models.Contact;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsViewActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView tv_contact_name;
    RecyclerView rv_contact_numbers;
    Contact contact;
    ContactPhoneListAdapter contactPhoneListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);
        contact = (Contact) getIntent().getSerializableExtra("Contact");


        initView();
        tv_contact_name.setText(contact.getName());

        if (contact.getImage()!= null) {
            userImage.setImageURI(Uri.parse(contact.getImage()));
        }

        rv_contact_numbers.setLayoutManager(new LinearLayoutManager(this));
        contactPhoneListAdapter = new ContactPhoneListAdapter(this,contact.getPhone_numbers());

        rv_contact_numbers.setAdapter(contactPhoneListAdapter);

    }

    private void initView() {

        userImage = findViewById(R.id.image_view_user);
        tv_contact_name = findViewById(R.id.tv_contact_name);
        rv_contact_numbers = findViewById(R.id.rv_contact_number);

    }
}