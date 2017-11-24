package com.akotnana.fcpsstudentvue.fragments.gradebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.adapters.RVAdapterGrade;
import com.akotnana.fcpsstudentvue.utils.cards.GradeCourseCard;
import com.akotnana.fcpsstudentvue.utils.gson.Course;
import com.akotnana.fcpsstudentvue.utils.gson.Quarter;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GradeBookQFragment extends Fragment {

    private static final String TAG = "GradeBookQFragment";
    int quarterIndex;
    public String quarterName;
    public String semesterName;
    public static String grades;
    private List<GradeCourseCard> gradesCards;
    private RecyclerView rv;

    public boolean isVisible = false;

    public GradeBookQFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not NULL");
            this.quarterIndex = bundle.getInt("index", 0);
            Log.d(TAG, "" + this.quarterIndex);
            this.grades = bundle.getString("grades", "N/A");
            if(quarterIndex < 2) {
                this.semesterName = "S1";
            } else {
                this.semesterName = "S2";
            }
            switch (quarterIndex) {
                case 0:
                    quarterName = "Q1";
                    break;
                case 1:
                    quarterName = "Q2";
                    break;
                case 2:
                    quarterName = "Q3";
                    break;
                case 3:
                    quarterName = "Q4";
                    break;
                default:
                    break;
            }

            /*
            initializeData();
            initializeAdapter();
            */
        }

        Log.d(TAG, "OnCreate called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh : {
                Log.i("GradeBookQFragment", "Save from fragment");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grade_book_q, container, false);

        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        Log.d(TAG, "OnCreateView called");
        initializeData();
        initializeAdapter();
        return v;

    }

    private void initializeAdapter(){
        RVAdapterGrade adapter = new RVAdapterGrade(gradesCards);
        rv.setAdapter(adapter);
    }

    private void initializeData(){
        gradesCards = new ArrayList<>();
        Gson gson = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "" + this.grades);
        Quarter quarter = gson.fromJson(this.grades, Quarter.class);
        Course[] courses = quarter.getCourses();
        for(Course course : courses) {
            gradesCards.add(new GradeCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGradePercentage(), "N/A", "N/A"));
        }
    }
}