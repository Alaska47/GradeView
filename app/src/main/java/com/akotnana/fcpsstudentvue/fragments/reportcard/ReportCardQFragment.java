package com.akotnana.fcpsstudentvue.fragments.reportcard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.akotnana.fcpsstudentvue.utils.adapters.RVAdapterReport;
import com.akotnana.fcpsstudentvue.utils.cards.ReportCourseCard;
import com.akotnana.fcpsstudentvue.utils.gson.Course;
import com.akotnana.fcpsstudentvue.utils.gson.Quarter;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportCardQFragment extends Fragment {

    private static final String TAG = "ReportCardQFragment";
    int quarterIndex;
    public String quarterName;
    public String semesterName;
    public static String reports;
    private List<ReportCourseCard> reportsCards;
    private RecyclerView rv;
    private RVAdapterReport adapter;

    public boolean hasAlreadyLoaded = false;

    public int visibleFragment = -1;

    public Snackbar errorSnack;

    public ReportCardQFragment() {
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
        View v = inflater.inflate(R.layout.fragment_report_card_q, container, false);

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
        adapter = new RVAdapterReport(reportsCards, getContext());
        rv.setAdapter(adapter);
    }

    public void initializeData() {
        Log.d(TAG, quarterName + " is loading new now!");
        reportsCards = new ArrayList<>();
        Gson gson = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Quarter quarter = gson.fromJson(new DataStorage(getContext()).getData("ReportCard"), Quarter.class);
        Course[] courses = quarter.getCourses();
        for (Course course : courses) {
            if(quarterName.equals("Q1") && course.getGrades().getFirstQuarter() != null) {
                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getFirstQuarter().getLetter(), "N/A", course.getGrades().getFirstQuarter().getPercentage(), "0.0"));
            } else if(quarterName.equals("Q2") && course.getGrades().getSecondQuarter() != null) {
                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getSecondQuarter().getLetter(), course.getGrades().getSemesterOne().getLetter(), course.getGrades().getSecondQuarter().getPercentage(), course.getGrades().getSemesterOne().getPercentage()));
            } else if(quarterName.equals("Q3") && course.getGrades().getThirdQuarter() != null) {
                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getThirdQuarter().getLetter(), "N/A", course.getGrades().getThirdQuarter().getPercentage(), "0.0"));
            } else if(course.getGrades().getFourthQuarter() != null){
                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getFourthQuarter().getLetter(), course.getGrades().getSemesterTwo().getLetter(), course.getGrades().getFourthQuarter().getPercentage(), course.getGrades().getSemesterTwo().getPercentage()));
            }

        }
        initializeAdapter();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            /*if(quarterName != null) {
                this.visibleFragment = Integer.parseInt(String.valueOf(quarterName.charAt(1))) - 1;
            }
            Log.d(TAG, "The index of the visible fragment is: " + this.visibleFragment + " called from fragment " + quarterIndex);
            if(getContext() != null) {
                //Log.d(TAG, Integer.parseInt(new DataStorage(getContext()).getData("selectedQuarter")) + "");
            }
            if(this.visibleFragment == quarterIndex) {
                Log.d(TAG, quarterName + " is loading new now!");
                reportsCards = new ArrayList<>();
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
                BackendUtils.doPostRequest("/report_card", new HashMap<String, String>() {{
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        //Log.d(TAG, result);
                        Quarter quarter = finalGson.fromJson(result, Quarter.class);
                        Course[] courses = quarter.getCourses();
                        for (Course course : courses) {
                            if(quarterName.equals("Q1") && course.getGrades().getFirstQuarter() != null) {
                                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getFirstQuarter().getLetter(), "N/A", course.getGrades().getFirstQuarter().getPercentage(), "0.0"));
                            } else if(quarterName.equals("Q2") && course.getGrades().getSecondQuarter() != null) {
                                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getSecondQuarter().getLetter(), course.getGrades().getSemesterOne().getLetter(), course.getGrades().getSecondQuarter().getPercentage(), course.getGrades().getSemesterOne().getPercentage()));
                            } else if(quarterName.equals("Q3") && course.getGrades().getThirdQuarter() != null) {
                                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getThirdQuarter().getLetter(), "N/A", course.getGrades().getThirdQuarter().getPercentage(), "0.0"));
                            } else if(course.getGrades().getFourthQuarter() != null){
                                reportsCards.add(new ReportCourseCard(course.getPeriodNumber(), course.getCourseName(), course.getTeacher(), course.getRoomNumber(), quarterName, semesterName, course.getGrades().getFourthQuarter().getLetter(), course.getGrades().getSemesterTwo().getLetter(), course.getGrades().getFourthQuarter().getPercentage(), course.getGrades().getSemesterTwo().getPercentage()));
                            }

                        }
                        if(reportsCards.size() < 1) {
                            Log.i(TAG, "Snackbar called!");
                            errorSnack = Snackbar.make(((Activity) getContext()).findViewById(android.R.id.content), "The report card for this quarter is currently unavailable.", Snackbar.LENGTH_LONG);
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
                    }
                }, getContext());

            }

        } else {
            reportsCards = new ArrayList<>();
            if(adapter != null)
                initializeAdapter();
            hasAlreadyLoaded = false;
            Log.d(TAG, "Can no longer see fragment " + quarterIndex);
            if (errorSnack != null) {
                Log.i(TAG, "Snackbar dismissed!");
                errorSnack.dismiss();
            }
            */
            if(reportsCards != null && reportsCards.size() < 1) {
                Log.i(TAG, "Snackbar called!");
                errorSnack = Snackbar.make(((Activity) getContext()).findViewById(android.R.id.content), "The report card for this quarter is currently unavailable.", Snackbar.LENGTH_LONG);
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
        } else {
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