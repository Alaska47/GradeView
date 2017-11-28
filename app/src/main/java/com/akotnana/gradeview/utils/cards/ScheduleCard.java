package com.akotnana.gradeview.utils.cards;

import com.akotnana.gradeview.utils.gson.Course;

/**
 * Created by anees on 11/24/2017.
 */

public class ScheduleCard {
    public String periodNumber;
    public String courseName;
    public String teacherName;
    public String roomNumber;

    public ScheduleCard(String periodNumber, String courseName, String teacherName, String roomNumber) {
        this.periodNumber = periodNumber;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.roomNumber = roomNumber;
    }
}
