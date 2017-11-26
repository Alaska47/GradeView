package com.akotnana.fcpsstudentvue.utils.cards;

import com.akotnana.fcpsstudentvue.utils.gson.Course;

/**
 * Created by anees on 11/24/2017.
 */

public class ReportCourseCard {

    public String periodNumber;
    public String courseName;
    public String teacherName;
    public String roomNumber;

    public String quarterName;
    public String semesterName;

    public String quarterReportPercentage;
    public String semesterReportPercentage;

    public String quarterReport;
    public String semesterReport;

    public ReportCourseCard(String periodNumber, String courseName, String teacherName, String roomNumber, String quarterName, String semesterName, String quarterReport, String semesterReport, String quarterReportPercentage, String semesterReportPercentage) {
        this.periodNumber = periodNumber;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.roomNumber = roomNumber;
        this.quarterName = quarterName;
        this.semesterName = semesterName;
        this.quarterReport = quarterReport;
        this.semesterReport = semesterReport;
        this.quarterReportPercentage = quarterReportPercentage;
        this.semesterReportPercentage = semesterReportPercentage;
    }
}
