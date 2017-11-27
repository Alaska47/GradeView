package com.akotnana.gradeview.utils.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anees on 11/21/2017.
 */

public class Assignment {
    @SerializedName("name")
    String name;
    @SerializedName("assignment_type")
    String assignmentType;
    @SerializedName("date")
    String dateAssigned;
    @SerializedName("due_date")
    String dateDue;
    @SerializedName("score")
    String score;
    @SerializedName("points")
    String points;
    @SerializedName("notes")
    String notes;

    public String getAssignmentName() {
        return name;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public String getDateAssigned() {
        return dateAssigned;
    }

    public String getDateDue() {
        return dateDue;
    }

    public String getScore() {
        return score;
    }

    public String getPoints() {
        return points;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return getAssignmentName() + " | " + getPoints();
    }
}
