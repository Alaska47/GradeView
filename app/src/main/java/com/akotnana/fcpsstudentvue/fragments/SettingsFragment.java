package com.akotnana.fcpsstudentvue.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akotnana.fcpsstudentvue.R;

import com.akotnana.fcpsstudentvue.activities.AssignmentViewActivity;
import com.akotnana.fcpsstudentvue.activities.NavigationActivity;
import com.akotnana.fcpsstudentvue.activities.SignInActivity;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.PreferenceManager;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

import java.util.HashMap;

/**
 * Created by anees on 11/23/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompatDividers   {

    private static final String TAG = "SettingsFragment";
    private OnFragmentInteractionListener mListener;

    private CheckBoxPreference colorCodeGrades;
    private CheckBoxPreference sendNotifications;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
        // additional setup
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {

            Preference textPreference = (Preference) getPreferenceManager().findPreference("version");
            textPreference.setSummary("Logged in as: " + new DataStorage(getContext()).getData("userFullName"));

            colorCodeGrades = (CheckBoxPreference)  getPreferenceManager().findPreference("color");
            colorCodeGrades.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    new PreferenceManager(getActivity()).setMyPreference("color", (Boolean) newValue);
                    return true;
                }
            });


            sendNotifications = (CheckBoxPreference)  getPreferenceManager().findPreference("notifications");
            if(new DataStorage(getContext()).getData("notificationsFirstValue").equals("")) {
                Log.d(TAG, "first time opening preferences");
                sendNotifications.setChecked(false);
                colorCodeGrades.setChecked(true);
                new DataStorage(getContext()).storeData("notificationsFirstValue", "true", true);
                new PreferenceManager(getActivity()).setMyPreference("notifications", false);
                new PreferenceManager(getActivity()).setMyPreference("color", true);
            }
            sendNotifications.setChecked(new PreferenceManager(getActivity()).getMyPreference("notifications"));
            colorCodeGrades.setChecked(new PreferenceManager(getActivity()).getMyPreference("color"));
            Log.d(TAG, "Notifications: " + String.valueOf(new PreferenceManager(getActivity()).getMyPreference("notifications")));
            Log.d(TAG, "Color: " + String.valueOf(new PreferenceManager(getActivity()).getMyPreference("color")));
            sendNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue instanceof Boolean && ((Boolean) newValue) != new PreferenceManager(getActivity()).getMyPreference("notifications")) {
                        final boolean isEnabled = (Boolean) newValue;
                        if (isEnabled) {

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Confirm")
                                    .setMessage("If you turn on notifications, your password will need to be stored, but encrypted securely, on TJ servers, so that we can provide you with continuous grade updates.")
                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            new PreferenceManager(getActivity()).setMyPreference("notifications", false);
                                            sendNotifications.setChecked(false);

                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                                    R.style.AppTheme_Dark_Dialog);
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setMessage("Loading...");
                                            progressDialog.show();

                                            BackendUtils.doGetRequest("/devices/", new HashMap<String, String>() {{
                                            }}, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    Log.d(TAG, result);
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onError(VolleyError error) {
                                                    Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                                                    progressDialog.dismiss();
                                                    if(error.networkResponse.statusCode == 401) {
                                                        Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(getContext(), SignInActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        getActivity().startActivity(intent);
                                                    }

                                                }
                                            }, getContext(), getActivity());
                                        }
                                    }).setCancelable(false)
                                    .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // OK has been pressed => force the new value and update the checkbox display
                                            new PreferenceManager(getActivity()).setMyPreference("notifications", true);
                                            sendNotifications.setChecked(true);

                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                                    R.style.AppTheme_Dark_Dialog);
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setMessage("Loading...");
                                            progressDialog.show();

                                            BackendUtils.doPostRequest("/devices/", new HashMap<String, String>() {{
                                                put("registration_id", new DataStorage(getContext()).getData("firebaseID"));
                                                put("active", "true");
                                                put("type", "android");
                                            }}, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    Log.d(TAG, result);
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onError(VolleyError error) {
                                                    progressDialog.dismiss();
                                                    Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                                                    if(error.networkResponse.statusCode == 401) {
                                                        Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(getContext(), SignInActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        getActivity().startActivity(intent);
                                                    }

                                                }
                                            }, getContext(), getActivity());

                                        }
                                    }).create().show();

                            // by default ignore the pref change, which can only be validated when OK is pressed
                            return true;

                        } else {
                            new PreferenceManager(getActivity()).setMyPreference("notifications", false);
                            sendNotifications.setChecked(false);
                            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();

                            BackendUtils.doGetRequest("/devices/", new HashMap<String, String>() {{
                            }}, new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.d(TAG, result);
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                                    progressDialog.dismiss();
                                    if(error.networkResponse.statusCode == 401) {
                                        Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(getContext(), SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getActivity().startActivity(intent);
                                    }

                                }
                            }, getContext(), getActivity());
                        }
                    }
                    return false;
                }
            });
            return super.onCreateView(inflater, container, savedInstanceState);
        } finally {
            setDividerPreferences(DIVIDER_PADDING_CHILD | DIVIDER_CATEGORY_AFTER_LAST | DIVIDER_CATEGORY_BETWEEN);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh : {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                BackendUtils.doPostRequest("/devices/", new HashMap<String, String>() {{
                    put("registration_id", new DataStorage(getContext()).getData("firebaseID"));
                    put("active", String.valueOf(new PreferenceManager(getActivity()).getMyPreference("notifications")));
                    put("type", "android");
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                        progressDialog.dismiss();
                        if(error.networkResponse.statusCode == 401) {
                            Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }

                    }
                }, getContext(), getActivity());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
