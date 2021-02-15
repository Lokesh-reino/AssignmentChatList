package com.example.assignment.models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.assignment.utils.Constants;
import com.example.assignment.utils.Converters;
import com.example.assignment.utils.MyTypeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(tableName = "contact", indices = @Index(value = {"name"}, unique = true))
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name ="m_Id")
    private int m_Id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="image")
    private String image;

    @ColumnInfo(name="phoneNumber")
    private String PhoneNumbers;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "phone_numbers")
    private Set<String> phone_numbers = new HashSet<>();

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "phone_numbers_type")
    private Set<String> phone_numbers_type = new HashSet<>();

    public Contact(int id) {
        Log.e("Mid", "ID: "+id);
        this.m_Id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumbers() {
        return PhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        PhoneNumbers = phoneNumbers;
    }

    public int getM_Id() {
        return m_Id;
    }

    public void setM_Id(int m_Id) {
        this.m_Id = m_Id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*public List<String> getPhone_numbers() {
        return phone_numbers;
    }

    public void setPhone_numbers(List<String> phone_numbers) {
        this.phone_numbers = phone_numbers;
    }*/

    public Set<String> getPhone_numbers() {
        return phone_numbers;
    }

    public void setPhone_numbers(Set<String> phone_numbers) {
        this.phone_numbers = phone_numbers;
    }

    public Set<String> getPhone_numbers_type() {
        return phone_numbers_type;
    }

    public void setPhone_numbers_type(Set<String> phone_numbers_type) {
        this.phone_numbers_type = phone_numbers_type;
    }
}
