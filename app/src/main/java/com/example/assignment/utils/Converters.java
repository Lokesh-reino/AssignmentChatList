package com.example.assignment.utils;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Converters {


/*    @TypeConverter
    public static List<String> stringToList(String stringList) {
        return Arrays.asList(stringList.split(","));
    }

    @TypeConverter
    public static String listToString(List<String> listString) {
        return TextUtils.join(",", listString);
    }*/

    @TypeConverter
    public static Set<String> StringtoSet(String value) {
        Type listType = new TypeToken<HashSet<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(Set<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
