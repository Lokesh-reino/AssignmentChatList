package com.example.assignment.utils;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyTypeConverter {


    @TypeConverter
    public static List<String> stringToList(String stringList) {
        return Arrays.asList(stringList.split(","));
    }

    @TypeConverter
    public static String listToString(List<String> listString) {
        return TextUtils.join(",", listString);
    }

}
