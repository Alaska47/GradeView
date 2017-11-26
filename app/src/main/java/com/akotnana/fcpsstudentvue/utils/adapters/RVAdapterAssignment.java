package com.akotnana.fcpsstudentvue.utils.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.ColorManager;
import com.akotnana.fcpsstudentvue.utils.PreferenceManager;
import com.akotnana.fcpsstudentvue.utils.cards.AssignmentCard;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by anees on 11/24/2017.
 */

public class RVAdapterAssignment extends RecyclerView.Adapter<RVAdapterAssignment.AssignmentViewHolder>{
    public static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView assignmentName;
        TextView points;
        TextView score;
        TextView gradeLetter;

        AssignmentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            assignmentName = (TextView)itemView.findViewById(R.id.assignment_name);
            points = (TextView)itemView.findViewById(R.id.points);
            score = (TextView)itemView.findViewById(R.id.score);
            gradeLetter = (TextView)itemView.findViewById(R.id.assignment_grade);
        }
    }

    List<AssignmentCard> assignmentCards;
    Activity a;

    public RVAdapterAssignment(List<AssignmentCard> grades, Activity a){
        this.assignmentCards = grades;
        this.a = a;
    }

    @Override
    public int getItemCount() {
        if(assignmentCards == null)
            return 0;
        return assignmentCards.size();
    }

    public void clear() {
        assignmentCards.clear();
        notifyDataSetChanged();
    }

    @Override
    public AssignmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assignment_card, viewGroup, false);
        AssignmentViewHolder pvh = new AssignmentViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AssignmentViewHolder gradeViewHolder, int i) {
        gradeViewHolder.assignmentName.setText(assignmentCards.get(i).assignmentName);
        if(assignmentCards.get(i).grade.length() < 3) {
            gradeViewHolder.gradeLetter.setText(assignmentCards.get(i).grade);
            gradeViewHolder.gradeLetter.setTextSize(32f);
        } else {
            gradeViewHolder.gradeLetter.setText(assignmentCards.get(i).grade);
            gradeViewHolder.gradeLetter.setTextSize(26f);
        }

        if(new PreferenceManager(a).getMyPreference("color")){
            GradientDrawable sd = (GradientDrawable) gradeViewHolder.gradeLetter.getBackground().mutate();
            sd.setColor(ColorManager.getColor(assignmentCards.get(i).grade));
            sd.invalidateSelf();
        } else {
            GradientDrawable sd = (GradientDrawable) gradeViewHolder.gradeLetter.getBackground().mutate();
            sd.setColor(ColorManager.getColor("N/A"));
            sd.invalidateSelf();
        }

        gradeViewHolder.score.setText("Score: " + assignmentCards.get(i).score);
        gradeViewHolder.points.setText("Points: " + assignmentCards.get(i).points);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
