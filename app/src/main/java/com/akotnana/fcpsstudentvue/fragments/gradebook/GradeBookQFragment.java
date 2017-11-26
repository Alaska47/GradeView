package com.akotnana.fcpsstudentvue.fragments.gradebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.activities.SignInActivity;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.akotnana.fcpsstudentvue.utils.adapters.RVAdapterGrade;
import com.akotnana.fcpsstudentvue.utils.cards.GradeCourseCard;
import com.akotnana.fcpsstudentvue.utils.gson.Course;
import com.akotnana.fcpsstudentvue.utils.gson.Quarter;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradeBookQFragment extends Fragment {

    private static final String TAG = "GradeBookQFragment";
    int quarterIndex;
    public String quarterName;
    public String semesterName;
    public static String grades;
    private List<GradeCourseCard> gradesCards;
    private RecyclerView rv;
    private RVAdapterGrade adapter;

    public boolean hasAlreadyLoaded = false;

    public int visibleFragment = -1;

    public Snackbar errorSnack;

    public GradeBookQFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //Log.d(TAG, "bundle not NULL");
            this.quarterIndex = bundle.getInt("index", 0);
            if (quarterIndex < 2) {
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

        //Log.d(TAG, "OnCreate called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grade_book_q, container, false);

        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        initializeAdapter();
        initializeData();

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeAdapter() {
        adapter = new RVAdapterGrade(gradesCards, getContext(), getActivity());
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        Log.d(TAG, "SelectedQuarter from " + quarterName + " : " + new DataStorage(getContext()).getData("selectedQuarter"));
        if (Integer.parseInt(new DataStorage(getContext()).getData("selectedQuarter")) == quarterIndex) {
            new DataStorage(getContext()).storeData("selectedQuarter", "-1", false);
            Log.d(TAG, quarterName + " is loading what is already loaded now!");
            gradesCards = new ArrayList<>();
            Gson gson = null;
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Quarter quarter = gson.fromJson(new DataStorage(getContext()).getData("GradeBook"), Quarter.class);
            Course[] courses = quarter.getCourses();
            if(courses.length < 1) {
                errorSnack = Snackbar.make(((Activity) getContext()).findViewById(android.R.id.content), "Grades for this quarter are currently unavailable.", Snackbar.LENGTH_LONG);
                errorSnack.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorSnack.dismiss();
                    }
                });
                errorSnack.show();
            }
            for (Course course : courses) {
                gradesCards.add(new GradeCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getCurrentQuarter().getPercentage(), course.getGrades().getCurrentSemester().getPercentage(), "N/A", course));
            }
            hasAlreadyLoaded = true;
            initializeAdapter();
        } else {
            Log.d(TAG, quarterName + " has nothing to load!");
            gradesCards = new ArrayList<>();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(quarterName != null) {
                this.visibleFragment = Integer.parseInt(String.valueOf(quarterName.charAt(1))) - 1;
            }
            Log.d(TAG, "The index of the visible fragment is: " + this.visibleFragment + " called from fragment " + quarterIndex);
            if(getContext() != null) {
                //Log.d(TAG, Integer.parseInt(new DataStorage(getContext()).getData("selectedQuarter")) + "");
            }
            if(this.visibleFragment == quarterIndex && Integer.parseInt(new DataStorage(getContext()).getData("selectedQuarter")) == -1 && !hasAlreadyLoaded) {
                Log.d(TAG, quarterName + " is loading new now!");
                gradesCards = new ArrayList<>();
                Gson gson = null;
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                final Gson finalGson = gson;
                BackendUtils.doPostRequest("/grades/quarter/" + quarterIndex, new HashMap<String, String>() {{
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        Quarter quarter = finalGson.fromJson(result, Quarter.class);
                        Course[] courses = quarter.getCourses();
                        for (Course course : courses) {
                            gradesCards.add(new GradeCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getCurrentQuarter().getPercentage(), course.getGrades().getCurrentSemester().getPercentage(), "N/A", course));
                        }
                        if(courses.length < 1) {
                            Log.i(TAG, "Snackbar called!");
                            errorSnack = Snackbar.make(((Activity) getContext()).findViewById(android.R.id.content), "Grades for this quarter are currently unavailable.", Snackbar.LENGTH_LONG);
                            errorSnack.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    errorSnack.dismiss();
                                    errorSnack = null;
                                }
                            });
                            errorSnack.addCallback(new Snackbar.Callback() {

                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                        errorSnack = null;
                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                }
                            });
                            errorSnack.show();
                        }
                        progressDialog.dismiss();
                        initializeAdapter();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse.statusCode == 401) {
                            Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                        }
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    }
                }, getContext(), getActivity());

            }

        } else {
            gradesCards = new ArrayList<>();
            if(adapter != null)
                initializeAdapter();
            hasAlreadyLoaded = false;
            Log.d(TAG, "Can no longer see fragment " + quarterIndex);
            if (errorSnack != null) {
                Log.i(TAG, "Snackbar dismissed!");
                errorSnack.dismiss();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (errorSnack != null) {
            Log.i(TAG, "Snackbar dismissed!");
            errorSnack.dismiss();
        }
    }
}