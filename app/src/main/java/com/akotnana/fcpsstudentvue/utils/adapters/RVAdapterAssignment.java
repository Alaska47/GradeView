package com.akotnana.fcpsstudentvue.utils.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.R;
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

    public RVAdapterAssignment(List<AssignmentCard> grades){
        this.assignmentCards = grades;
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
        gradeViewHolder.gradeLetter.setText((assignmentCards.get(i).grade.length() < 2) ? " " + assignmentCards.get(i).grade + " " : assignmentCards.get(i).grade);
        gradeViewHolder.score.setText("Score: " + assignmentCards.get(i).score);
        gradeViewHolder.points.setText("Points: " + assignmentCards.get(i).points);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
