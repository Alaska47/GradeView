package com.akotnana.gradeview.activities;

/**
 * Created by anees on 11/23/2017.
 */

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akotnana.gradeview.R;
import com.akotnana.gradeview.fragments.GradeBookFragment;
import com.akotnana.gradeview.fragments.ReportCardFragment;
import com.akotnana.gradeview.fragments.ScheduleFragment;
import com.akotnana.gradeview.fragments.SettingsFragment;
import com.akotnana.gradeview.fragments.StudentInformationFragment;
import com.akotnana.gradeview.utils.AccountManager;
import com.akotnana.gradeview.utils.BackendUtils;
import com.akotnana.gradeview.utils.DataStorage;
import com.akotnana.gradeview.utils.PreferenceManager;
import com.akotnana.gradeview.utils.RequestQueueSingleton;
import com.akotnana.gradeview.utils.VolleyCallback;
import com.akotnana.gradeview.utils.gson.User;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavigationActivity extends AppCompatActivity implements GradeBookFragment.OnFragmentInteractionListener, ReportCardFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener, StudentInformationFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    public static String TAG = "NavigationActivity";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView toolbarTitle;
    private int prevIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        checkForNotification(savedInstanceState);

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

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        Fragment fragment = null;
        try {
            fragment = (Fragment) GradeBookFragment.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("grades", type);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        toolbarTitle.setText("Grade Book");

        final HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("registration_id", new DataStorage(getApplicationContext()).getData("firebaseID"));
            put("active", "true");
            put("type", "android");
        }};
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://sis.okulkarni.me" + "/devices/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "sent to server successfully");
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                String body = "";
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }

                Log.e(TAG, body + "\n");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                String hello = "";
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    hello += entry.getKey() + "=" + entry.getValue() + "&";
                }
                Log.d(TAG, "original parameter: " +  hello);
                Map<String, String> params = parameters;
                return params;
            }

            /*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String[] creds = new AccountManager(getApplicationContext()).retrieveCredentials();
                String credentials = creds[0] + ":" + creds[1];
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplicationContext())
                .getRequestQueue().add(stringRequest);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        View hView = navigationView.getHeaderView(0);
        final CircleImageView imageView = (CircleImageView) hView.findViewById(R.id.materialup_profile_image);
        final TextView nameView = (TextView) hView.findViewById(R.id.profile_name);

        hView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked");
                BackendUtils.doGetRequest("/user/", new HashMap<String, String>() {{
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = null;
                        try {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        User user = gson.fromJson(result, User.class);
                        imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmapSizeByScale(user.getPhoto(), 7)));
                        nameView.setText(user.getFullName());
                        new DataStorage(getApplicationContext()).storeData("userFullName", user.getFullName(), true);
                        Log.d(TAG, "updated");
                    }

                    @Override
                    public void onError(VolleyError error) {
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
                }, getApplicationContext(), NavigationActivity.this);
            }
        });


        BackendUtils.doGetRequest("/user/", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = null;
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                User user = gson.fromJson(result, User.class);
                imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmapSizeByScale(user.getPhoto(), 7)));
                nameView.setText(user.getFullName());
                new DataStorage(getApplicationContext()).storeData("userFullName", user.getFullName(), true);
            }

            @Override
            public void onError(VolleyError error) {
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
        }, getApplicationContext(), NavigationActivity.this);
    }

    public void signOut() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://sis.okulkarni.me/user/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Signed out!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }

                        Log.e(TAG, body + "\n");
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String[] creds = new AccountManager(getApplicationContext()).retrieveCredentials();
                String credentials = creds[0] + ":" + creds[1];
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }

        };
        RequestQueueSingleton.getInstance(getApplicationContext())
                .getRequestQueue().add(stringRequest);
    }

    public Bitmap bitmapSizeByScale(Bitmap bitmapIn, float scall_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scall_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);

        return bitmapOut;
    }

    public void checkForNotification(Bundle savedInstanceState) {
        String fromNotification = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                fromNotification = null;
            } else {
                fromNotification = extras.getString("fromNotification");
            }
        } else {
            fromNotification = (String) savedInstanceState.getSerializable("fromNotification");
        }
        Log.d(TAG, "fromNotification: " + fromNotification);
        if(fromNotification != null && fromNotification.equals("1")) {
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
            String period = "";
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
            String assignments = "";
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
            Log.d(TAG, "assignments: " + assignments);
            Intent intent = new Intent(this, AssignmentViewActivity.class);
            intent.putExtra("currentQuarter", currentQuarter);
            intent.putExtra("period", period);
            intent.putExtra("assignments", assignments);
            intent.putExtra("fromNotification", "1");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.grade_book_fragment:
                mDrawer.closeDrawers();
                fragmentClass = GradeBookFragment.class;
                Log.d("Navigation", "regular");
                break;
            case R.id.report_card_fragment:
                mDrawer.closeDrawers();
                fragmentClass = ReportCardFragment.class;
                Log.d("Navigation", "regular");
                break;
            case R.id.schedule_fragment:
                mDrawer.closeDrawers();
                fragmentClass = ScheduleFragment.class;
                Log.d("Navigation", "regular");
                break;
            case R.id.student_info_fragment:
                mDrawer.closeDrawers();
                fragmentClass = StudentInformationFragment.class;
                Log.d("Navigation", "regular");
                break;
            case R.id.settings_fragment:
                mDrawer.closeDrawers();
                fragmentClass = SettingsFragment.class;
                Log.d("Navigation", "regular");
                break;
            case R.id.sign_out:
                mDrawer.closeDrawers();
                FirebaseAuth.getInstance().signOut();
                signOut();
                Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }

        Log.d("MainActivity", "" + (fragmentClass == null));

        if(fragmentClass == null) {
            //profile
            try {
                fragment = (Fragment) GradeBookFragment.class.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            toolbarTitle.setText(menuItem.getTitle());

        } else {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            toolbarTitle.setText(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
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
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 0) {
            int toCheck = 0;
            CharSequence i = toolbarTitle.getText();
            if (i.equals("Grade Book")) {
                toCheck = 0;
                Log.d("Navigation", "regular");

            } else if(i.equals("Sign Out")) {
                toCheck = 5;
            }
            nvDrawer.getMenu().getItem(toCheck).setChecked(true);
        }
    }
}