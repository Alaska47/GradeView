package com.akotnana.fcpsstudentvue.utils.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by anees on 11/21/2017.
 */

public class Quarter {
    @SerializedName("name")
    String name;
    @SerializedName("start_date")
    String startDate;
    @SerializedName("end_date")
    String endDate;
    @SerializedName("courses")
    Course[] courses;

    public String getQuarterName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Course[] getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return name + "\n" + Arrays.toString(courses);
    }
}
