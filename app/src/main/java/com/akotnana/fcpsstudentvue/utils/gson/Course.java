package com.akotnana.fcpsstudentvue.utils.gson;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Primitives;

import java.util.Arrays;

/**
 * Created by anees on 11/21/2017.
 */

public class Course {
    @SerializedName("period")
    String period;
    @SerializedName("name")
    String name;
    @SerializedName("location")
    String roomNumber;
    @SerializedName("assignments")
    Assignment[] assignments;
    @SerializedName("grades")
    Grades grades;
    @SerializedName("teacher")
    String teacher;

    public String getPeriodNumber() {
        return period;
    }

    public String getCourseName() {
        return name.split(" \\(")[0];
    }

    public String getCourseID() {
        String f = name.split(" \\(")[1];
        return f.substring(0, f.length()-1);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Assignment[] getAssignments() {
        return assignments;
    }

    public Grades getGrades() { return this.grades; }

    public String getTeacher() {
        return teacher;
    }

    @Override
    public String toString() {
        return getCourseName() + " | " + getTeacher() + " | " + getRoomNumber() + " | " + Arrays.toString(assignments);
    }
}
