package com.akotnana.fcpsstudentvue.utils.cards;

/**
 * Created by anees on 11/24/2017.
 */

public class AssignmentCard {
    public String assignmentName;
    public String score;
    public String points;
    public String grade;

    public AssignmentCard(String assignmentName, String score, String points, String grade) {
        this.assignmentName = assignmentName;
        this.score = score;
        this.points = points;
        this.grade = grade;
    }
}
