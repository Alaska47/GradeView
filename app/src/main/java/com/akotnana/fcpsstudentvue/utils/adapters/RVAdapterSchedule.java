package com.akotnana.fcpsstudentvue.utils.adapters;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.ColorManager;
import com.akotnana.fcpsstudentvue.utils.cards.ScheduleCard;

import java.util.List;

/**
 * Created by anees on 11/24/2017.
 */

public class RVAdapterSchedule extends RecyclerView.Adapter<RVAdapterSchedule.ScheduleViewHolder>{
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView scheduleName;
        TextView points;
        TextView score;
        TextView gradeLetter;

        ScheduleViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            scheduleName = (TextView)itemView.findViewById(R.id.schedule_name);
            points = (TextView)itemView.findViewById(R.id.points);
            score = (TextView)itemView.findViewById(R.id.score);
            gradeLetter = (TextView)itemView.findViewById(R.id.schedule_grade);
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
        gradeViewHolder.scheduleName.setText(scheduleCards.get(i).scheduleName);
        if(scheduleCards.get(i).grade.length() < 3) {
            gradeViewHolder.gradeLetter.setText(scheduleCards.get(i).grade);
            gradeViewHolder.gradeLetter.setTextSize(32f);
        } else {
            gradeViewHolder.gradeLetter.setText(scheduleCards.get(i).grade);
            gradeViewHolder.gradeLetter.setTextSize(26f);
        }
        //gradeViewHolder.gradeLetter.setBackgroundColor(ColorManager.getColor(scheduleCards.get(i).grade));
        GradientDrawable sd = (GradientDrawable) gradeViewHolder.gradeLetter.getBackground().mutate();
        sd.setColor(ColorManager.getColor(scheduleCards.get(i).grade));
        sd.invalidateSelf();
        gradeViewHolder.score.setText("Score: " + scheduleCards.get(i).score);
        gradeViewHolder.points.setText("Points: " + scheduleCards.get(i).points);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
