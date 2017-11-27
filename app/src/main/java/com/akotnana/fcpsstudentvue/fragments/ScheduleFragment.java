package com.akotnana.fcpsstudentvue.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.akotnana.fcpsstudentvue.utils.adapters.RVAdapterSchedule;
import com.akotnana.fcpsstudentvue.utils.cards.ScheduleCard;
import com.akotnana.fcpsstudentvue.utils.gson.Period;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anees on 11/23/2017.
 */

public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";
    private OnFragmentInteractionListener mListener;

    private List<ScheduleCard> scheduleCards;
    private RecyclerView rv;
    private RVAdapterSchedule adapter;
    
    private Snackbar errorSnack;
    
    public ScheduleFragment() {
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
                initializeAdapter();
                initializeData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        initializeAdapter();
        initializeData();
        return v;
    }

    private void initializeAdapter() {
        adapter = new RVAdapterSchedule(scheduleCards);
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        scheduleCards = new ArrayList<>();
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
        BackendUtils.doGetRequest("/user/", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, result);
                User user = finalGson.fromJson(result, User.class);
                Period[] periods = user.getSchedule();
                if (periods.length < 1) {
                    if(errorSnack == null) {
                        errorSnack = Snackbar.make((getActivity()).findViewById(android.R.id.content), "Your schedule is currently unavailable", Snackbar.LENGTH_LONG);
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
                for (Period period : periods) {
                    scheduleCards.add(new ScheduleCard(period.getPeriod(), period.getPeriodName(), period.getTeacher(), period.getPeriodLocation()));
                }
                progressDialog.dismiss();
                initializeAdapter();
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
