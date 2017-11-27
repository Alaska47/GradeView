package com.akotnana.gradeview.utils.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anees on 11/25/2017.
 */

public class Grade {
    @SerializedName("letter")
    String letter;
    @SerializedName("percentage")
    String percentage;

    public Grade(String s, String s1) {
        letter = s;
        percentage = s1;
    }

    public String getLetter() {
        return this.letter;
    }

    public String getPercentage() {
        return this.percentage;
    }
}
