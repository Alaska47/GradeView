package com.akotnana.fcpsstudentvue.utils.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.cards.GradeCourseCard;

import java.util.List;

/**
 * Created by anees on 11/24/2017.
 */

public class RVAdapterGrade extends RecyclerView.Adapter<RVAdapterGrade.GradeViewHolder>{
    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView periodName;
        TextView courseName;
        TextView teacherName;
        TextView roomName;
        TextView quarterName;
        TextView semesterName;
        TextView quarterGrade;
        TextView semesterGrade;
        TextView finalExamGrade;

        GradeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            periodName = (TextView)itemView.findViewById(R.id.period_number);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            teacherName = (TextView)itemView.findViewById(R.id.teacher_name);
            roomName = (TextView)itemView.findViewById(R.id.room_number);
            quarterName = (TextView)itemView.findViewById(R.id.quarter_name);
            semesterName = (TextView)itemView.findViewById(R.id.semester_name);
            quarterGrade = (TextView)itemView.findViewById(R.id.quarter_grade);
            semesterGrade = (TextView)itemView.findViewById(R.id.semester_grade);
            finalExamGrade = (TextView)itemView.findViewById(R.id.final_exam_grade);
        }
    }

    List<GradeCourseCard> gradeCourseCards;

    public RVAdapterGrade(List<GradeCourseCard> grades){
        this.gradeCourseCards = grades;
    }

    @Override
    public int getItemCount() {
        if(gradeCourseCards == null)
            return 0;
        return gradeCourseCards.size();
    }

    public void clear() {
        gradeCourseCards.clear();
        notifyDataSetChanged();
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_card, viewGroup, false);
        GradeViewHolder pvh = new GradeViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(GradeViewHolder gradeViewHolder, int i) {
        gradeViewHolder.periodName.setText(gradeCourseCards.get(i).periodNumber);
        gradeViewHolder.courseName.setText(gradeCourseCards.get(i).courseName);
        gradeViewHolder.teacherName.setText("Teacher: " + gradeCourseCards.get(i).teacherName);
        gradeViewHolder.roomName.setText("Room: " + gradeCourseCards.get(i).roomNumber);
        gradeViewHolder.quarterName.setText(gradeCourseCards.get(i).quarterName);
        gradeViewHolder.semesterName.setText(gradeCourseCards.get(i).semesterName);
        gradeViewHolder.quarterGrade.setText(gradeCourseCards.get(i).quarterGrade);
        gradeViewHolder.semesterGrade.setText(gradeCourseCards.get(i).semesterGrade);
        gradeViewHolder.finalExamGrade.setText(gradeCourseCards.get(i).finalExamGrade);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
