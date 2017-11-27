package com.akotnana.gradeview.utils.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anees on 11/22/2017.
 */

public class Period {
    @SerializedName("period")
    String period;
    @SerializedName("name")
    String periodName;
    @SerializedName("location")
    String periodLocation;
    @SerializedName("grade_letter")
    String gradeLetter;
    @SerializedName("grade_percentage")
    String gradePercentage;
    @SerializedName("teacher")
    String teacher;

    public String getPeriod() {
        return period;
    }

    public String getPeriodName() {
        return periodName;
    }

    public String getPeriodLocation() {
        return periodLocation;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public String getGradePercentage() {
        return gradePercentage;
    }

    public String getTeacher() {
        return teacher;
    }

    @Override
    public String toString() {
        return period + " | " + periodName + " | " + teacher + " | " + periodLocation;
    }
}
