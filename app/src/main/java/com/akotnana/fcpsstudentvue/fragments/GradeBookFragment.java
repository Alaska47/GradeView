package com.akotnana.fcpsstudentvue.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.fragments.gradebook.GradeBookQFragment;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.adapters.ViewPagerAdapter;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.android.volley.VolleyError;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh : {
                Log.i("GradeBookFragment", "Save from fragment");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grade_book, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        if(retrievedGrades.equals("")) {
            Log.d(TAG, "retrievedGrades empty");
            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            BackendUtils.doPostRequest("/grades", new HashMap<String, String>() {{
            }}, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    retrievedGrades = result;
                    try {
                        String quarter = new JSONObject(result).getString("name");
                        Fragment quarterOne = new GradeBookQFragment();
                        Fragment quarterTwo = new GradeBookQFragment();
                        Fragment quarterThree = new GradeBookQFragment();
                        Fragment quarterFour = new GradeBookQFragment();

                        if(quarter.contains("First")) {
                            Log.d(TAG, "Q1");
                            Bundle bundle0 = new Bundle();
                            bundle0.putInt("index", 0);
                            bundle0.putString("grades", result);
                            quarterOne.setArguments(bundle0);
                        } else if(quarter.contains("Second")) {
                            Log.d(TAG, "Q2");
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("index", 1);
                            bundle1.putString("grades", result);
                            quarterTwo.setArguments(bundle1);
                        } else if(quarter.contains("Third")) {
                            Log.d(TAG, "Q3");
                            Bundle bundle2 = new Bundle();
                            bundle2.putInt("index", 2);
                            bundle2.putString("grades", result);
                            quarterThree.setArguments(bundle2);
                        } else {
                            Log.d(TAG, "Q4");
                            Bundle bundle3 = new Bundle();
                            bundle3.putInt("index", 3);
                            bundle3.putString("grades", result);
                            quarterFour.setArguments(bundle3);
                        }

                        adapter.addFragment(quarterOne, "Q1");
                        adapter.addFragment(quarterTwo, "Q2");
                        adapter.addFragment(quarterThree, "Q3");
                        adapter.addFragment(quarterFour, "Q4");
                        viewPager.setAdapter(adapter);

                        tabLayout.setupWithViewPager(viewPager);

                        if(quarter.contains("First")) {
                            viewPager.setCurrentItem(0);
                        } else if(quarter.contains("Second")) {
                            viewPager.setCurrentItem(1);
                        } else if(quarter.contains("Third")) {
                            viewPager.setCurrentItem(2);
                        } else {
                            viewPager.setCurrentItem(3);
                        }

                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    progressDialog.dismiss();
                }
            }, getContext());
        } else {
            Log.d(TAG, "retrievedGrades not empty");
            try {
                String quarter = new JSONObject(retrievedGrades).getString("name");
                Fragment quarterOne = new GradeBookQFragment();
                Fragment quarterTwo = new GradeBookQFragment();
                Fragment quarterThree = new GradeBookQFragment();
                Fragment quarterFour = new GradeBookQFragment();

                if(quarter.contains("First")) {
                    Log.d(TAG, "Q1");
                    Bundle bundle0 = new Bundle();
                    bundle0.putInt("index", 0);
                    bundle0.putString("grades", retrievedGrades);
                    quarterOne.setArguments(bundle0);
                } else if(quarter.contains("Second")) {
                    Log.d(TAG, "Q2");
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("index", 1);
                    bundle1.putString("grades", retrievedGrades);
                    quarterTwo.setArguments(bundle1);
                } else if(quarter.contains("Third")) {
                    Log.d(TAG, "Q3");
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("index", 2);
                    bundle2.putString("grades", retrievedGrades);
                    quarterThree.setArguments(bundle2);
                } else {
                    Log.d(TAG, "Q4");
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("index", 3);
                    bundle3.putString("grades", retrievedGrades);
                    quarterFour.setArguments(bundle3);
                }

                adapter.addFragment(quarterOne, "Q1");
                adapter.addFragment(quarterTwo, "Q2");
                adapter.addFragment(quarterThree, "Q3");
                adapter.addFragment(quarterFour, "Q4");
                viewPager.setAdapter(adapter);

                tabLayout.setupWithViewPager(viewPager);

                if(quarter.contains("First")) {
                    viewPager.setCurrentItem(0);
                } else if(quarter.contains("Second")) {
                    viewPager.setCurrentItem(1);
                } else if(quarter.contains("Third")) {
                    viewPager.setCurrentItem(2);
                } else {
                    viewPager.setCurrentItem(3);
                }
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
