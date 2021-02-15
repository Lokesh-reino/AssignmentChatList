package com.example.assignment.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class User implements Serializable {
@PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="phoneNumber1")
    private String  phoneNumber1;
    @ColumnInfo(name="phoneNumber2")
    private String  phoneNumber2;
    @ColumnInfo(name="phoneNumber3")
    private String  phoneNumber3;
    @ColumnInfo
    private String birthday;
    @ColumnInfo(name = "createdAt")
    private long createdAt;
    @ColumnInfo(name = "modifiedAt")
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

    public User(String name, String phoneNumber1, String birthday, String image, long createdAt, long modifiedAt, String phoneNumber2, String phoneNumber3) {
        this.name = name;
        this.phoneNumber1 = phoneNumber1;
        this.birthday = birthday;
        this.image=image;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumber3 = phoneNumber3;

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

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(image, user.image) &&
                Objects.equals(phoneNumber1, user.phoneNumber1) &&
                Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, phoneNumber1, birthday);
    }
}
