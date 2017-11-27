package com.akotnana.gradeview.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.utils.ColorManager;
import com.akotnana.gradeview.utils.PreferenceManager;
import com.akotnana.gradeview.utils.cards.ReportCourseCard;

import java.util.List;

/**
 * Created by anees on 11/24/2017.
 */

public class RVAdapterReport extends RecyclerView.Adapter<RVAdapterReport.ReportViewHolder>{
    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView periodName;
        TextView courseName;
        TextView teacherName;
        TextView roomName;
        TextView quarterName;
        TextView semesterName;
        TextView quarterReport;
        TextView semesterReport;

        ReportViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            periodName = (TextView)itemView.findViewById(R.id.period_number);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            teacherName = (TextView)itemView.findViewById(R.id.teacher_name);
            roomName = (TextView)itemView.findViewById(R.id.room_number);
            quarterName = (TextView)itemView.findViewById(R.id.quarter_name);
            semesterName = (TextView)itemView.findViewById(R.id.semester_name);
            quarterReport = (TextView)itemView.findViewById(R.id.quarter_report);
            semesterReport = (TextView)itemView.findViewById(R.id.semester_report);
        }
    }

    List<ReportCourseCard> reportCourseCards;
    Context context;
    Activity a;

    public RVAdapterReport(List<ReportCourseCard> reports, Context con, Activity a){
        this.reportCourseCards = reports;
        this.context = con;
        this.a = a;
    }

    @Override
    public int getItemCount() {
        if(reportCourseCards == null)
            return 0;
        return reportCourseCards.size();
    }

    public void clear() {
        reportCourseCards.clear();
        notifyDataSetChanged();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_card, viewGroup, false);
        ReportViewHolder pvh = new ReportViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ReportViewHolder reportViewHolder, final int i) {
        reportViewHolder.periodName.setText(reportCourseCards.get(i).periodNumber);
        reportViewHolder.courseName.setText(reportCourseCards.get(i).courseName);
        reportViewHolder.teacherName.setText("Teacher: " + reportCourseCards.get(i).teacherName);
        reportViewHolder.roomName.setText("Room: " + reportCourseCards.get(i).roomNumber);
        reportViewHolder.quarterName.setText(reportCourseCards.get(i).quarterName);
        reportViewHolder.semesterName.setText(reportCourseCards.get(i).semesterName);
        reportViewHolder.quarterReport.setText(reportCourseCards.get(i).quarterReport);
        if(!reportCourseCards.get(i).quarterReport.equals("N/A")) {
            reportViewHolder.quarterReport.setTextSize(22f);
        } else {
            reportViewHolder.quarterReport.setTextSize(16f);
        }
        //reportViewHolder.quarterReport.setTextColor(ColorManager.getColor(scoreToLetterGrade(Double.parseDouble(reportCourseCards.get(i).quarterReportPercentage))));

        if(new PreferenceManager(a).getMyPreference("color")){
            GradientDrawable sd = (GradientDrawable) reportViewHolder.quarterReport.getBackground().mutate();
            sd.setColor(ColorManager.getColor(reportCourseCards.get(i).quarterReport));
            sd.invalidateSelf();
        } else {
            GradientDrawable sd = (GradientDrawable) reportViewHolder.quarterReport.getBackground().mutate();
            sd.setColor(ColorManager.getColor("N/A"));
            sd.invalidateSelf();
        }

        reportViewHolder.semesterReport.setText(reportCourseCards.get(i).semesterReport);

        if(!reportCourseCards.get(i).semesterReport.equals("N/A")) {
            reportViewHolder.semesterReport.setTextSize(22f);
        } else {
            reportViewHolder.semesterReport.setTextSize(16f);
        }

        if(new PreferenceManager(a).getMyPreference("color")){
            GradientDrawable sd = (GradientDrawable) reportViewHolder.semesterReport.getBackground().mutate();
            sd.setColor(ColorManager.getColor(reportCourseCards.get(i).semesterReport));
            sd.invalidateSelf();
        } else {
            GradientDrawable sd = (GradientDrawable) reportViewHolder.semesterReport.getBackground().mutate();
            sd.setColor(ColorManager.getColor("N/A"));
            sd.invalidateSelf();
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
