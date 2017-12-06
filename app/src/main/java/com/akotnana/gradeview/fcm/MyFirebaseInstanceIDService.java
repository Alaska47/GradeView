package com.akotnana.gradeview.fcm;

import com.akotnana.gradeview.utils.AccountManager;
import com.akotnana.gradeview.utils.PreferenceManager;
import com.akotnana.gradeview.utils.RequestQueueSingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.akotnana.gradeview.utils.DataStorage;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        final String[] creds = new AccountManager(getApplicationContext()).retrieveCredentials();

        final HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("registration_id", token);
            put("active", "true");
            put("type", "android");
        }};
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://sis.okulkarni.me" + "/devices/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "sent to server successfully");
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

        if(creds[0].equals("") || creds[1].equals("")) {
            RequestQueueSingleton.getInstance(getApplicationContext())
                    .getRequestQueue().add(stringRequest);
        }
        new DataStorage(getApplicationContext()).storeData("firebaseID", token, true);
    }


}