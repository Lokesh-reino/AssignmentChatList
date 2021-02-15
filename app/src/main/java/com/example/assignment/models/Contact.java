package com.example.assignment.models;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.assignment.utils.MyTypeConverter;

import java.util.HashSet;
import java.util.Set;

@Entity(tableName = "contact", indices = @Index(value = {"name"}, unique = true))
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name ="mId")
    private int mId;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="image")
    private String image;

    @ColumnInfo(name="phoneNumber")
    private String PhoneNumbers;

    public Contact(int id) {
        this.mId = id;
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
