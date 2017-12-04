package com.akotnana.gradeview.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.activities.SignInActivity;
import com.akotnana.gradeview.fragments.gradebook.GradeBookQFragment;
import com.akotnana.gradeview.fragments.reportcard.ReportCardQFragment;
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.DataStorage;
import com.akotnana.gradeview.utils.adapters.ViewPagerAdapter;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.akotnana.gradeview.utils.cards.ReportCourseCard;
import com.akotnana.gradeview.utils.gson.Course;
import com.akotnana.gradeview.utils.gson.Quarter;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by anees on 11/23/2017.
 */

public class ReportCardFragment extends Fragment {

    private static final String TAG = "ReportCardFragment";
    private OnFragmentInteractionListener mListener;

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    public ReportCardFragment() {
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
                viewPager.setAdapter(null);
                viewPager.setOffscreenPageLimit(2);
                adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
                viewPager.setSaveFromParentEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                BackendUtils.doGetRequest("/report_card/", new HashMap<String, String>() {{
                    put("force", "true");
                    put("format", "json");
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        new DataStorage(getContext()).storeData("ReportCard", result, false);
                        int goToPage = Integer.parseInt(String.valueOf(new DataStorage(getContext()).getData("currentQuarter").charAt(1)))-1;

                        Fragment quarterOne = new ReportCardQFragment();
                        Bundle bundle0 = new Bundle();
                        bundle0.putInt("index", 0);
                        quarterOne.setArguments(bundle0);
                        Fragment quarterTwo = new ReportCardQFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("index", 1);
                        quarterTwo.setArguments(bundle1);
                        Fragment quarterThree = new ReportCardQFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("index", 2);
                        quarterThree.setArguments(bundle2);
                        Fragment quarterFour = new ReportCardQFragment();
                        Bundle bundle3 = new Bundle();
                        bundle3.putInt("index", 3);
                        quarterFour.setArguments(bundle3);
                        adapter.addFragment(quarterOne, "Q1");
                        adapter.addFragment(quarterTwo, "Q2");
                        adapter.addFragment(quarterThree, "Q3");
                        adapter.addFragment(quarterFour, "Q4");
                        viewPager.setAdapter(adapter);

                        viewPager.setCurrentItem(goToPage);

                        tabLayout.setupWithViewPager(viewPager);
                        progressDialog.setCancelable(true);
                        try {
                            if (getActivity().getWindow().getDecorView().isShown())
                                progressDialog.dismiss();
                        } catch (NullPointerException e) {

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.setCancelable(true);
                        try {
                            if (getActivity().getWindow().getDecorView().isShown())
                                progressDialog.dismiss();
                        } catch (NullPointerException e) {

                        }
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
        View v = inflater.inflate(R.layout.fragment_report_card, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
        viewPager.setSaveFromParentEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        BackendUtils.doGetRequest("/report_card/", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                new DataStorage(getContext()).storeData("ReportCard", result, false);
                int goToPage = Integer.parseInt(String.valueOf(new DataStorage(getContext()).getData("currentQuarter").charAt(1)))-1;

                Fragment quarterOne = new ReportCardQFragment();
                Bundle bundle0 = new Bundle();
                bundle0.putInt("index", 0);
                quarterOne.setArguments(bundle0);
                Fragment quarterTwo = new ReportCardQFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("index", 1);
                quarterTwo.setArguments(bundle1);
                Fragment quarterThree = new ReportCardQFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("index", 2);
                quarterThree.setArguments(bundle2);
                Fragment quarterFour = new ReportCardQFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("index", 3);
                quarterFour.setArguments(bundle3);
                adapter.addFragment(quarterOne, "Q1");
                adapter.addFragment(quarterTwo, "Q2");
                adapter.addFragment(quarterThree, "Q3");
                adapter.addFragment(quarterFour, "Q4");
                viewPager.setAdapter(adapter);

                viewPager.setCurrentItem(goToPage);

                tabLayout.setupWithViewPager(viewPager);
                progressDialog.setCancelable(true);
                try {
                    if (getActivity().getWindow().getDecorView().isShown())
                        progressDialog.dismiss();
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.setCancelable(true);
                try {
                    if (getActivity().getWindow().getDecorView().isShown())
                        progressDialog.dismiss();
                } catch (NullPointerException e) {

                }
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
