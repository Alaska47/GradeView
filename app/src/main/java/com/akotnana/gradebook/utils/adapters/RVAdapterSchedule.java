package com.akotnana.gradeview.utils.adapters;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.utils.ColorManager;
import com.akotnana.gradeview.utils.cards.ScheduleCard;

import java.util.List;

/**
 * Created by anees on 11/24/2017.
 */

public class RVAdapterSchedule extends RecyclerView.Adapter<RVAdapterSchedule.ScheduleViewHolder>{
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView periodNumber;
        TextView courseName;
        TextView teacherName;
        TextView roomNumber;

        ScheduleViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            periodNumber = (TextView)itemView.findViewById(R.id.period_number);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            teacherName = (TextView)itemView.findViewById(R.id.teacher_name);
            roomNumber = (TextView)itemView.findViewById(R.id.room_number);
        }
    }

    List<ScheduleCard> scheduleCards;

    public RVAdapterSchedule(List<ScheduleCard> grades){
        this.scheduleCards = grades;
    }

    @Override
    public int getItemCount() {
        if(scheduleCards == null)
            return 0;
        return scheduleCards.size();
    }

    public void clear() {
        scheduleCards.clear();
        notifyDataSetChanged();
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_card, viewGroup, false);
        ScheduleViewHolder pvh = new ScheduleViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder gradeViewHolder, int i) {
        gradeViewHolder.periodNumber.setText(scheduleCards.get(i).periodNumber);
        gradeViewHolder.courseName.setText(scheduleCards.get(i).courseName);
        gradeViewHolder.teacherName.setText("Teacher: " + scheduleCards.get(i).teacherName);
        gradeViewHolder.roomNumber.setText("Room: " + scheduleCards.get(i).roomNumber);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
