package com.example.assignment.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.assignment.models.Contact;

import java.util.ArrayList;
import java.util.Calendar;

public class Constants {


    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_READ_CONTACTS = 2;

    public static String  actionType = "action_type";
    public static String  viewActionType = "view";
    public static String  editActionType = "edit";
    public static boolean isSwipeOpen = false;

    public static void mapDisplayName(Cursor cursor, Contact contact, int columnIndex) {
        String displayName = cursor.getString(columnIndex);
        if (displayName != null && !displayName.isEmpty()) {
            contact.setName(displayName);
        }
    }

    public static void mapPhoneNumber (Cursor cursor, Contact contact, int columnIndex) {
        String phoneNumber = cursor.getString(columnIndex);
        ArrayList<String> phoneList = new ArrayList<>();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Remove all whitespaces
            phoneNumber = phoneNumber.replaceAll("\\s+", "");

            if (phoneNumber.startsWith("0"))
                phoneNumber = "+91" + phoneNumber.substring(1);
            else if (!phoneNumber.startsWith("+"))
                phoneNumber = "+91" + phoneNumber;

            contact.setPhoneNumbers(phoneNumber);

            contact.getPhone_numbers().add(phoneNumber);

        }
    }

    public static void mapEmail (Cursor cursor, Contact contact, int columnIndex) {
        String email = cursor.getString(columnIndex);

        Log.e("email",email);

        if (email != null && !email.isEmpty()) {
            contact.getPhone_numbers_type().add(email);
        }
    }


    public static void mapThumbnail (Cursor cursor, Contact contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty()) {
            contact.setImage(uri);
        }
    }

    public static String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final String dateFormatString = "EEEE, MMMM d";

        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today"/* + DateFormat.format(timeFormatString, smsTime)*/;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday"/* + DateFormat.format(timeFormatString, smsTime)*/;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy", smsTime).toString();
        }
    }




}
