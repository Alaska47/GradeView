package com.akotnana.gradeview.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.activities.SignInActivity;
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.akotnana.gradeview.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anees on 11/23/2017.
 */

public class StudentInformationFragment extends Fragment {

    private static final String TAG = "StudentInformationFragment";
    private OnFragmentInteractionListener mListener;

    CircleImageView circleImageView;
    TextView name;
    TextView schoolName;
    TextView schoolGrade;
    TextView studentID;

    public StudentInformationFragment() {
        // Required empty public constructor
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
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                BackendUtils.doGetRequest("/user/", new HashMap<String, String>() {{
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        progressDialog.dismiss();
                        //Log.d(TAG, result);
                        Gson gson = null;
                        try {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        User user = gson.fromJson(result, User.class);
                        name.setText(user.getFullName());
                        schoolName.setText(user.getSchoolName());
                        studentID.setText(user.getUsername());
                        schoolGrade.setText(user.getGrade());
                        circleImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmapSizeByScale(user.getPhoto(), 7)));
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                        if(error.networkResponse.statusCode == 401) {
                            Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                        }

                    }
                }, getContext(), getActivity());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_information, container, false);

        circleImageView = (CircleImageView) v.findViewById(R.id.materialup_profile_image);
        name = (TextView) v.findViewById(R.id.name);
        schoolGrade = (TextView) v.findViewById(R.id.school_grade);
        schoolName = (TextView) v.findViewById(R.id.school_name);
        studentID = (TextView) v.findViewById(R.id.student_id);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        BackendUtils.doGetRequest("/user/", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                //Log.d(TAG, result);
                Gson gson = null;
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                User user = gson.fromJson(result, User.class);
                name.setText(user.getFullName());
                schoolName.setText(user.getSchoolName());
                studentID.setText(user.getUsername());
                schoolGrade.setText(user.getGrade());
                circleImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmapSizeByScale(user.getPhoto(), 7)));
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                if(error.networkResponse.statusCode == 401) {
                    Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();
                }

            }
        }, getContext(), getActivity());

        return v;
    }

    public Bitmap bitmapSizeByScale(Bitmap bitmapIn, float scall_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scall_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);

        return bitmapOut;
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
