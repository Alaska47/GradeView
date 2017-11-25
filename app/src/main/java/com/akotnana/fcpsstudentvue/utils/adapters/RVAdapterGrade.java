package com.akotnana.fcpsstudentvue.utils.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.AssignmentViewActivity;
import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.ColorManager;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.cards.GradeCourseCard;
import com.google.gson.Gson;

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
    Context context;

    public RVAdapterGrade(List<GradeCourseCard> grades, Context con){
        this.gradeCourseCards = grades;
        this.context = con;
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
    public void onBindViewHolder(GradeViewHolder gradeViewHolder, final int i) {
        gradeViewHolder.periodName.setText(gradeCourseCards.get(i).periodNumber);
        gradeViewHolder.courseName.setText(gradeCourseCards.get(i).courseName);
        gradeViewHolder.teacherName.setText("Teacher: " + gradeCourseCards.get(i).teacherName);
        gradeViewHolder.roomName.setText("Room: " + gradeCourseCards.get(i).roomNumber);
        gradeViewHolder.quarterName.setText(gradeCourseCards.get(i).quarterName);
        gradeViewHolder.semesterName.setText(gradeCourseCards.get(i).semesterName);
        gradeViewHolder.quarterGrade.setText(gradeCourseCards.get(i).quarterGrade);

        gradeViewHolder.quarterGrade.setTextColor(ColorManager.getColor(scoreToLetterGrade(Double.parseDouble(gradeCourseCards.get(i).quarterGrade))));

        gradeViewHolder.semesterGrade.setText(gradeCourseCards.get(i).semesterGrade);
        gradeViewHolder.finalExamGrade.setText(gradeCourseCards.get(i).finalExamGrade);
        if(new DataStorage(context).getData("currentQuarter").equals(gradeCourseCards.get(i).quarterName)) {
            gradeViewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, AssignmentViewActivity.class);
                    intent.putExtra("currentQuarter", gradeCourseCards.get(i).quarterName);
                    Gson gson = new Gson();
                    intent.putExtra("assignments", gson.toJson(gradeCourseCards.get(i).course));
                    intent.putExtra("period", gradeCourseCards.get(i).course.getPeriodNumber());
                    context.startActivity(intent);
                }
            });
        }
    }

    public String scoreToLetterGrade(double score) {
        String result;
        if (score >= 92.5d) { result = "A"; }
        else if (score >= 89.5d) { result = "A-"; }
        else if (score >= 86.5d) { result = "B+"; }
        else if (score >= 83.5d) { result = "B"; }
        else if (score >= 79.5d) { result = "B-"; }
        else if (score >= 76.5d) { result = "C+"; }
        else if (score >= 73.5d) { result = "C"; }
        else if (score >= 69.5d) { result = "C-"; }
        else if (score >= 66.5d) { result = "D+"; }
        else if (score >= 63.5d) { result = "D+"; }
        else { result = "F"; }
        if(score == 0.0d)
            result = "N/A";
        return result;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
