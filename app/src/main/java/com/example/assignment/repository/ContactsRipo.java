package com.example.assignment.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


import com.example.assignment.activities.MainActivity;
import com.example.assignment.models.Contact;
import com.example.assignment.viewmodel.ContactsListViewModel;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.example.assignment.utils.Constants.mapDisplayName;
import static com.example.assignment.utils.Constants.mapEmail;
import static com.example.assignment.utils.Constants.mapPhoneNumber;
import static com.example.assignment.utils.Constants.mapThumbnail;


public class ContactsRipo {

    private static final String[] PROJECTION = {
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME_PRIMARY,
            ContactsContract.Data.STARRED,
            ContactsContract.Data.PHOTO_URI,
            ContactsContract.Data.PHOTO_THUMBNAIL_URI,
            ContactsContract.Data.DATA1,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.IN_VISIBLE_GROUP
    };

    ContactsListViewModel contactsListViewModel;

    private ContentResolver mResolver;

    public ContactsRipo(Context context) {
        mResolver = context.getContentResolver();
        contactsListViewModel = MainActivity.contactsListViewModel;
    }

    public static Observable<Contact> fetch(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Contact>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull
                                          ObservableEmitter<Contact> e) throws Exception {
                new ContactsRipo(context).fetch(e);
            }
        });
    }

    private void fetch(ObservableEmitter<Contact> emitter) {
        HashMap<Long, Contact> contacts = new HashMap<>();
        Cursor cursor = createCursor();
        cursor.moveToFirst();
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        int inVisibleGroupColumnIndex = cursor.getColumnIndex(ContactsContract.Data.IN_VISIBLE_GROUP);
        int displayNamePrimaryColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY);
        int starredColumnIndex = cursor.getColumnIndex(ContactsContract.Data.STARRED);
        int photoColumnIndex = cursor.getColumnIndex(ContactsContract.Data.PHOTO_URI);
        int thumbnailColumnIndex = cursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI);
        int mimetypeColumnIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        int dataColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DATA1);
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(idColumnIndex);
            Contact contact = contacts.get(id);
            if (contact == null) {
                contact = new Contact((int)id);
                Log.e("ID",""+id);
//                mapInVisibleGroup(cursor, contact, inVisibleGroupColumnIndex);
                mapDisplayName(cursor, contact, displayNamePrimaryColumnIndex);
//                mapStarred(cursor, contact, starredColumnIndex);
//                mapPhoto(cursor, contact, photoColumnIndex);
                mapThumbnail(cursor, contact, thumbnailColumnIndex);
                contacts.put(id, contact);
            }
            String mimetype = cursor.getString(mimetypeColumnIndex);
            switch (mimetype) {
                case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE: {
                    mapEmail(cursor, contact, dataColumnIndex);
                    break;
                }
                case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE: {
                    mapPhoneNumber(cursor, contact, dataColumnIndex);
                    break;
                }
            }

            contactsListViewModel.saveAllContacts(contact);
            cursor.moveToNext();
        }
        cursor.close();
        for (Long key : contacts.keySet()) {
            emitter.onNext(contacts.get(key));
        }
        emitter.onComplete();
    }

    private Cursor createCursor() {
        return mResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Data.CONTACT_ID
        );
    }
}
