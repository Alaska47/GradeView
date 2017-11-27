package com.akotnana.gradeview.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.DataStorage;
import com.akotnana.gradeview.utils.adapters.ViewPagerAdapter;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by anees on 11/23/2017.
 */

public class GradeBookFragment extends Fragment {

    private static final String TAG = "GradeBookFragment";
    private OnFragmentInteractionListener mListener;
    String retrievedGrades = "";
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    public GradeBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            retrievedGrades = bundle.getString("grades", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_grade_book, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
        viewPager.setSaveFromParentEnabled(false);

        if(retrievedGrades.equals("")) {
            Log.d(TAG, "retrievedGrades empty");
            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            BackendUtils.doGetRequest("/grades/", new HashMap<String, String>() {{
            }}, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    new DataStorage(getContext()).storeData("GradeBook", result, false);
                    try {
                        int goToPage = -1;
                        String quarter = new JSONObject(result).getString("name");
                        if(quarter.contains("First")) {
                            new DataStorage(getContext()).storeData("selectedQuarter", "0", false);
                            new DataStorage(getContext()).storeData("currentQuarter", "Q1", false);
                            Log.d(TAG, "stored 00");
                            goToPage = 0;
                        } else if(quarter.contains("Second")) {
                            new DataStorage(getContext()).storeData("selectedQuarter", "1", false);
                            new DataStorage(getContext()).storeData("currentQuarter", "Q2", false);
                            Log.d(TAG, "stored 01");
                            goToPage = 1;
                        } else if(quarter.contains("Third")) {
                            new DataStorage(getContext()).storeData("selectedQuarter", "2", false);
                            new DataStorage(getContext()).storeData("currentQuarter", "Q3", false);
                            Log.d(TAG, "stored 02");
                            goToPage = 2;
                        } else {
                            new DataStorage(getContext()).storeData("selectedQuarter", "3", false);
                            new DataStorage(getContext()).storeData("currentQuarter", "Q4", false);
                            Log.d(TAG, "stored 03");
                            goToPage = 3;
                        }
                        Fragment quarterOne = new GradeBookQFragment();
                        Bundle bundle0 = new Bundle();
                        bundle0.putInt("index", 0);
                        quarterOne.setArguments(bundle0);
                        Fragment quarterTwo = new GradeBookQFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("index", 1);
                        quarterTwo.setArguments(bundle1);
                        Fragment quarterThree = new GradeBookQFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("index", 2);
                        quarterThree.setArguments(bundle2);
                        Fragment quarterFour = new GradeBookQFragment();
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

                        if(v.isShown())
                            progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        } else {
            new DataStorage(getContext()).storeData("GradeBook", retrievedGrades, false);
            Log.d(TAG, "retrievedGrades not empty");
            try {
                String quarter = new JSONObject(retrievedGrades).getString("name");
                int goToPage = -1;
                if(quarter.contains("First")) {
                    new DataStorage(getContext()).storeData("selectedQuarter", "0", false);
                    new DataStorage(getContext()).storeData("currentQuarter", "Q1", false);
                    Log.d(TAG, "stored 00");
                    goToPage = 0;
                } else if(quarter.contains("Second")) {
                    new DataStorage(getContext()).storeData("selectedQuarter", "1", false);
                    new DataStorage(getContext()).storeData("currentQuarter", "Q2", false);
                    Log.d(TAG, "stored 01");
                    goToPage = 1;
                } else if(quarter.contains("Third")) {
                    new DataStorage(getContext()).storeData("selectedQuarter", "2", false);
                    new DataStorage(getContext()).storeData("currentQuarter", "Q3", false);
                    Log.d(TAG, "stored 02");
                    goToPage = 2;
                } else {
                    new DataStorage(getContext()).storeData("selectedQuarter", "3", false);
                    new DataStorage(getContext()).storeData("currentQuarter", "Q4", false);
                    Log.d(TAG, "stored 03");
                    goToPage = 3;
                }

                Fragment quarterOne = new GradeBookQFragment();
                Bundle bundle0 = new Bundle();
                bundle0.putInt("index", 0);
                quarterOne.setArguments(bundle0);
                Fragment quarterTwo = new GradeBookQFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("index", 1);
                quarterTwo.setArguments(bundle1);
                Fragment quarterThree = new GradeBookQFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("index", 2);
                quarterThree.setArguments(bundle2);
                Fragment quarterFour = new GradeBookQFragment();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        viewPager.setAdapter(null);
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.refresh:
                viewPager.setAdapter(null);
                adapter = new ViewPagerAdapter(getContext(), getChildFragmentManager());
                viewPager.setSaveFromParentEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                BackendUtils.doGetRequest("/grades/", new HashMap<String, String>() {{
                    put("force", "true");
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        new DataStorage(getContext()).storeData("GradeBook", result, false);
                        try {
                            int goToPage = -1;
                            String quarter = new JSONObject(result).getString("name");
                            if(quarter.contains("First")) {
                                new DataStorage(getContext()).storeData("selectedQuarter", "0", false);
                                new DataStorage(getContext()).storeData("currentQuarter", "Q1", false);
                                Log.d(TAG, "stored 00");
                                goToPage = 0;
                            } else if(quarter.contains("Second")) {
                                new DataStorage(getContext()).storeData("selectedQuarter", "1", false);
                                new DataStorage(getContext()).storeData("currentQuarter", "Q2", false);
                                Log.d(TAG, "stored 01");
                                goToPage = 1;
                            } else if(quarter.contains("Third")) {
                                new DataStorage(getContext()).storeData("selectedQuarter", "2", false);
                                new DataStorage(getContext()).storeData("currentQuarter", "Q3", false);
                                Log.d(TAG, "stored 02");
                                goToPage = 2;
                            } else {
                                new DataStorage(getContext()).storeData("selectedQuarter", "3", false);
                                new DataStorage(getContext()).storeData("currentQuarter", "Q4", false);
                                Log.d(TAG, "stored 03");
                                goToPage = 3;
                            }
                            Fragment quarterOne = new GradeBookQFragment();
                            Bundle bundle0 = new Bundle();
                            bundle0.putInt("index", 0);
                            quarterOne.setArguments(bundle0);
                            Fragment quarterTwo = new GradeBookQFragment();
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("index", 1);
                            quarterTwo.setArguments(bundle1);
                            Fragment quarterThree = new GradeBookQFragment();
                            Bundle bundle2 = new Bundle();
                            bundle2.putInt("index", 2);
                            quarterThree.setArguments(bundle2);
                            Fragment quarterFour = new GradeBookQFragment();
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

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
