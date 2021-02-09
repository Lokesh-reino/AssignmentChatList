package com.example.assignment.repository;

import android.database.Cursor;
import android.net.Uri;

import com.example.assignment.models.Contact;

import android.database.Cursor;
import android.net.Uri;


/**
 * TODO Write javadoc
 * @author Ulrich Raab
 * @author Mohammad Mirrajabi
 */
public class ColumnMapper {

    // Utility class -> No instances allowed
    public ColumnMapper () {}

     public void mapInVisibleGroup (Cursor cursor, Contact contact, int columnIndex) {
        contact.setInVisibleGroup(cursor.getInt(columnIndex));
    }

     public void mapDisplayName(Cursor cursor, Contact contact, int columnIndex) {
        String displayName = cursor.getString(columnIndex);
        if (displayName != null && !displayName.isEmpty()) {
            contact.setDisplayName(displayName);
        }
    }

     public void mapEmail(Cursor cursor, Contact contact, int columnIndex) {
        String email = cursor.getString(columnIndex);
        if (email != null && !email.isEmpty()) {
            contact.getEmails().add(email);
        }
    }

     public void mapPhoneNumber(Cursor cursor, Contact contact, int columnIndex) {
        String phoneNumber = cursor.getString(columnIndex);
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Remove all whitespaces
            phoneNumber = phoneNumber.replaceAll("\\s+","");
            contact.getPhoneNumbers().add(phoneNumber);
        }
    }

     public void mapPhoto(Cursor cursor, Contact contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty()) {
            contact.setPhoto(Uri.parse(uri));
        }
    }

     public void mapStarred(Cursor cursor, Contact contact, int columnIndex) {
        contact.setStarred(cursor.getInt(columnIndex) != 0);
    }

     public void mapThumbnail(Cursor cursor, Contact contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty()) {
            contact.setThumbnail( Uri.parse(uri));
        }
    }
}