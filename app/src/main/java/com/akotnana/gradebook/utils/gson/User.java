package com.akotnana.gradeview.utils.gson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by anees on 11/22/2017.
 */

public class User {
    @SerializedName("username")
    String username;
    @SerializedName("full_name")
    String fullName;
    @SerializedName("school_name")
    String schoolName;
    @SerializedName("grade")
    String grade;
    @SerializedName("photo")
    String b64Picture;
    @SerializedName("schedule")
    Period[] schedule;

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getGrade() {
        return grade;
    }

    public Bitmap getPhoto() {
        byte[] decodedString = Base64.decode(b64Picture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public String getB64Picture() {
        return b64Picture;
    }

    public Bitmap reconstructPhoto(String b64) {
        byte[] decodedString = Base64.decode(b64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public Period[] getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return getUsername() + "\n" + getFullName() + "\n" + getGrade() + "\n" + getSchoolName() + "\n" + Arrays.toString(getSchedule());
    }
}
