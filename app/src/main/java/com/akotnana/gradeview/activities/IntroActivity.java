package com.akotnana.gradeview.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.DataStorage;
import com.akotnana.gradeview.utils.PreferenceManager;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Created by anees on 11/26/2017.
 */

public class IntroActivity extends MaterialIntroActivity {

    String finalType;
    public static String TAG = "IntroActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enableLastSlideAlphaExitTransition(true);
        String type = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                type = null;
            } else {
                type = extras.getString("grades");
            }
        } else {
            type = (String) savedInstanceState.getSerializable("grades");
        }

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.grade_book)
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title("Grade Book")
                .description("View all your current grades using the Grade Book. Swipe from tab to tab to view grades from previous quarters. Grades can optionally be color-coded.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.assignment)
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title("Assignments")
                .description("Click on any course in any quarter of the Grade Book to view your assignments for that course in that quarter. Assignment grades can optionally be color-coded.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.report_card)
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title("Report Card")
                .description("View all your official grades using the Report Card. Swipe from tab to tab to view report cards from previous quarters. Grades can optionally be color-coded.")
                .build());

        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.schedule)
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title("Schedule")
                .description("View your current schedule, including period numbers, course names, teacher names, and room numbers")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.user_profile_screen)
                .title("Student Information")
                .description("View your official SIS profile picture, along with your school, Student ID, and grade")
                .build());

        finalType = type;
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimary)
                        .buttonsColor(R.color.colorAccent)
                        .image(R.drawable.notification)
                        .title("Notifications")
                        .description("Notifications indicate when a new grade has been posted. If you enable notifications, your password will be stored, but encrypted securely, on TJ servers to provide continuous grade updates.")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(new PreferenceManager(IntroActivity.this).getMyPreference("notifications") != true) {
                            new PreferenceManager(IntroActivity.this).setMyPreference("notifications", true);
                            new DataStorage(getApplicationContext()).storeData("notificationsFirstValue", "true", true);

                            final ProgressDialog progressDialog = new ProgressDialog(IntroActivity.this,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Enabling...");
                            progressDialog.show();

                            BackendUtils.doPostRequest("/devices/", new HashMap<String, String>() {{
                                put("registration_id", new DataStorage(getApplicationContext()).getData("firebaseID"));
                                put("active", "true");
                                put("type", "android");
                            }}, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.d(TAG, result);
                                    progressDialog.dismiss();
                                    
                                }

                                @Override
                                public void onError(VolleyError notif) {
                                    progressDialog.dismiss();
                                    Log.d(TAG, String.valueOf(notif.networkResponse.statusCode));
                                    if(notif.networkResponse.statusCode == 401) {
                                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        IntroActivity.this.startActivity(intent);
                                        overridePendingTransition(0, 0);
                                        finish();
                                    }

                                }
                            }, getApplicationContext(), IntroActivity.this);
                        } else {
                            final Snackbar notifSnack = Snackbar.make(findViewById(android.R.id.content), "Notifications already enabled", Snackbar.LENGTH_SHORT);
                            notifSnack.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    notifSnack.dismiss();
                                }
                            });
                            notifSnack.show();
                        }
                    }
                }, "Enable notifications"));
    }

    @Override
    public void onFinish() {
        new DataStorage(getApplicationContext()).storeData("finishedTutorial", "true", false);
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        if(finalType != null && !finalType.equals(""))
            intent.putExtra("grades", finalType);
        startActivity(intent);
        super.onFinish();
    }
}
