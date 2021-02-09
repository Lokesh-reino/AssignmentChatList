package com.example.assignment.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.assignment.converters.DateTypeConverters;

import java.util.Date;
import java.util.Objects;

@Entity
public class User {
@PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="phoneNumber")
    private String  phoneNumber;
    @ColumnInfo
    private String birthday;
    @TypeConverters({DateTypeConverters.class})
    private Long datecreated;

    @ColumnInfo(name = "created_at")
    private long createdAt;
    @ColumnInfo(name = "modified_at")
    private long modifiedAt;

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Long getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Long datecreated) {
        this.datecreated = datecreated;
    }

    public User(String name, String image, String phoneNumber, String birthday, long createdAt, long modifiedAt) {
        this.name = name;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(image, user.image) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, phoneNumber, birthday);
    }
}
