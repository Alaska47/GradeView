package com.akotnana.fcpsstudentvue.utils.cards;

/**
 * Created by anees on 11/24/2017.
 */

public class GradeCourseCard {

    public String periodNumber;
    public String courseName;
    public String teacherName;
    public String roomNumber;

    public String quarterName;
    public String semesterName;

    public String quarterGrade;
    public String semesterGrade;
    public String finalExamGrade;

    public GradeCourseCard(String periodNumber, String courseName, String teacherName, String roomNumber, String quarterName, String semesterName, String quarterGrade, String semesterGrade, String finalExamGrade) {
        this.periodNumber = periodNumber;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.roomNumber = roomNumber;
        this.quarterName = quarterName;
        this.semesterName = semesterName;
        this.quarterGrade = quarterGrade;
        this.semesterGrade = semesterGrade;
        this.finalExamGrade = finalExamGrade;
    }
}
