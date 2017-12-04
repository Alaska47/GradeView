package com.akotnana.gradeview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.OnSwipeTouchListener;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.akotnana.gradeview.utils.adapters.RVAdapterAssignment;
import com.akotnana.gradeview.utils.cards.AssignmentCard;
import com.akotnana.gradeview.utils.gson.Assignment;
import com.akotnana.gradeview.utils.gson.Course;
import com.akotnana.gradeview.utils.gson.Quarter;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anees on 11/25/2017.
 */

public class AssignmentViewActivity extends AppCompatActivity {

    public static String TAG = "AssignmentViewActivity";

    Toolbar toolbar;
    TextView toolbarTitle;

    private List<AssignmentCard> assignmentCards;
    private RecyclerView rv;
    private RVAdapterAssignment adapter;

    public Snackbar errorSnack;

    String assignments;
    String period;
    String fromNotification = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout_id);
        rootLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {

            }

            @Override
            public void onSwipeLeft() {

            }

            @Override
            public void onSwipeUp() {

            }

            @Override
            public void onSwipeRight() {
                if (fromNotification == null || fromNotification.equals("0")) {
                    finish();
                    overridePendingTransition(R.xml.left_in, R.xml.right_out);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.xml.left_in, R.xml.right_out);
                    finish();
                }
            }
        });

        FrameLayout flLayout = (FrameLayout) findViewById(R.id.flContent);
        flLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {

            }

            @Override
            public void onSwipeLeft() {

            }

            @Override
            public void onSwipeUp() {

            }

            @Override
            public void onSwipeRight() {
                if (fromNotification == null || fromNotification.equals("0")) {
                    finish();
                    overridePendingTransition(R.xml.left_in, R.xml.right_out);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.xml.left_in, R.xml.right_out);
                    finish();
                }
            }
        });

        String currentQuarter = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                currentQuarter = null;
            } else {
                currentQuarter = extras.getString("currentQuarter");
            }
        } else {
            currentQuarter = (String) savedInstanceState.getSerializable("currentQuarter");
        }

        fromNotification = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                fromNotification = "";
            } else {
                fromNotification = extras.getString("fromNotification");
            }
        } else {
            fromNotification = (String) savedInstanceState.getSerializable("fromNotification");
        }

        period = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                period = null;
            } else {
                period = extras.getString("period");
            }
        } else {
            period = (String) savedInstanceState.getSerializable("period");
        }

        assignments = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                assignments = null;
            } else {
                assignments = extras.getString("assignments");
            }
        } else {
            assignments = (String) savedInstanceState.getSerializable("assignments");
        }

        try {
            toolbarTitle.setText(new JSONObject(assignments).getString("name").split(" \\(")[0] + " - " + currentQuarter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        initializeAdapter();
        initializeData();

    }

    private void initializeAdapter() {
        adapter = new RVAdapterAssignment(assignmentCards, this);
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        assignmentCards = new ArrayList<>();
        Gson gson = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Course course = gson.fromJson(assignments, Course.class);
        Assignment[] assignments = course.getAssignments();
        if (assignments.length < 1) {
            if(errorSnack == null) {
                errorSnack = Snackbar.make((AssignmentViewActivity.this).findViewById(android.R.id.content), "There are currently no grades for this course", Snackbar.LENGTH_LONG);
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
                Log.i(TAG, "Snackbar shown!");
            }
        }
        for (Assignment assignment : assignments) {
            if (!assignment.getPoints().contains("Points Possible")) {
                String[] points = assignment.getPoints().split(" / ");
                assignmentCards.add(new AssignmentCard(assignment.getAssignmentName(), assignment.getScore(), assignment.getPoints(), scoreToLetterGrade((double) Double.parseDouble(points[0]) / Double.parseDouble(points[1])*100)));
            } else {
                assignmentCards.add(new AssignmentCard(assignment.getAssignmentName(), assignment.getScore(), assignment.getPoints(), "N/A"));
            }
        }
        initializeAdapter();
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
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fromNotification == null || fromNotification.equals("0")) {
                    finish();
                    overridePendingTransition(R.xml.left_in, R.xml.right_out);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                overridePendingTransition(R.xml.left_in, R.xml.right_out);
                    finish();
                }
                finish();
                return true;
            case R.id.refresh:
                assignmentCards = new ArrayList<>();
                Gson gson = null;
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final ProgressDialog progressDialog = new ProgressDialog(AssignmentViewActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                final Gson finalGson = gson;
                BackendUtils.doGetRequest("/grades/class/" + period, new HashMap<String, String>() {{
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        Quarter quarter = finalGson.fromJson(result, Quarter.class);
                        Assignment[] assignments = quarter.getCourses()[0].getAssignments();
                        if (assignments.length < 1) {
                            if(errorSnack == null) {
                                errorSnack = Snackbar.make((AssignmentViewActivity.this).findViewById(android.R.id.content), "Assignments for this course are currently unavailable", Snackbar.LENGTH_LONG);
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
                                Log.i(TAG, "Snackbar shown!");
                            }
                        }
                        for (Assignment assignment : assignments) {
                            if (!assignment.getPoints().contains("Points Possible")) {
                                String[] points = assignment.getPoints().split(" / ");
                                assignmentCards.add(new AssignmentCard(assignment.getAssignmentName(), assignment.getScore(), assignment.getPoints(), scoreToLetterGrade((double) Double.parseDouble(points[0]) / Double.parseDouble(points[1])*100)));
                            } else {
                                assignmentCards.add(new AssignmentCard(assignment.getAssignmentName(), assignment.getScore(), assignment.getPoints(), "N/A"));
                            }
                        }
                        progressDialog.dismiss();
                        initializeAdapter();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse.statusCode == 401) {
                            Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }

                    }
                }, getApplicationContext(), this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
