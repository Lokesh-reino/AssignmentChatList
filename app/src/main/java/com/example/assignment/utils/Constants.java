package com.example.assignment.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;

import com.example.assignment.models.Contact;

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
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Remove all whitespaces
            phoneNumber = phoneNumber.replaceAll("\\s+","");
            contact.setPhoneNumbers(phoneNumber);
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
