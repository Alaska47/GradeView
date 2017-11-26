package com.akotnana.fcpsstudentvue.utils.gson;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Primitives;

/**
 * Created by anees on 11/25/2017.
 */

public class Grades {
    @SerializedName("first_quarter")
    Grade firstQuarter;
    @SerializedName("second_quarter")
    Grade secondQuarter;
    @SerializedName("third_quarter")
    Grade thirdQuarter;
    @SerializedName("fourth_quarter")
    Grade fourthQuarter;
    @SerializedName("first_semester")
    Grade semesterOne;
    @SerializedName("second_semester")
    Grade semesterTwo;

    public Grade getFirstQuarter() { return this.firstQuarter; }
    public Grade getSecondQuarter() { return this.secondQuarter; }
    public Grade getThirdQuarter() { return this.thirdQuarter; }
    public Grade getFourthQuarter() { return this.fourthQuarter; }
    public Grade getSemesterOne() { return this.semesterOne; }
    public Grade getSemesterTwo() { return this.semesterTwo; }

    public Grade getCurrentQuarter() {
        if(firstQuarter != null)
            return firstQuarter;
        if(secondQuarter != null)
            return secondQuarter;
        if(thirdQuarter != null)
            return thirdQuarter;
        if(fourthQuarter != null)
            return fourthQuarter;
        return firstQuarter;
    }

    public String getCurrentQuarterString() {
        if(firstQuarter != null)
            return "Q1";
        if(secondQuarter != null)
            return "Q2";
        if(thirdQuarter != null)
            return "Q3";
        if(fourthQuarter != null)
            return "Q4";
        return "Q1";
    }

    public Grade getCurrentSemester() {
        if(semesterOne != null)
            return semesterOne;
        if(semesterTwo != null)
            return semesterTwo;
        return new Grade("N/A", "N/A");
    }
}
