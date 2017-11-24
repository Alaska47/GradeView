package com.akotnana.fcpsstudentvue.fragments.gradebook;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.utils.ViewPagerAdapter;

/**
 * Created by anees on 11/23/2017.
 */

public class GradeBookFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    String retrievedGrades = "";

    public GradeBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            retrievedGrades = bundle.getString("grades", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grade_book, container, false);

        /*final TextView test = (TextView) v.findViewById(R.id.test);
        if(retrievedGrades.equals("")) {
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
                    progressDialog.dismiss();
                    test.setText(result);
                }

                @Override
                public void onError(VolleyError error) {
                    progressDialog.dismiss();
                }
            }, getContext());
        } else {
            test.setText(retrievedGrades);
        }
        */

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new GradeBookQFragment(), "Q1");
        adapter.addFragment(new GradeBookQ2Fragment(), "Q2");
        adapter.addFragment(new GradeBookQ3Fragment(), "Q3");
        adapter.addFragment(new GradeBookQ3Fragment(), "Q4");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
