package com.akotnana.gradeview.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anees on 11/21/2017.
 */

public class BackendUtils {
    public static String TAG = "BackendUtils";

    private static String IP = "https://sis.okulkarni.me";

    private static String result = "";

    public static void doGetRequest(String address, Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {

        Map<String, String> params = parameters;
        if(new PreferenceManager(activity).getMyPreference("notifications")){
            params.put("save_password", "true");
        }
        String request = IP + address + "?";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            request += entry.getKey() + "=" + entry.getValue() + "&";
        }
        request = request.substring(0, request.length()-1);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                        callback.onError(error);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String[] creds = new AccountManager(context).retrieveCredentials();
                String credentials = creds[0] + ":" + creds[1];
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }

        };
        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }

    public static void doCustomGetRequest(String address, Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {
        String request = address + "?";
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            request += entry.getKey() + "=" + entry.getValue() + "&";
        }

        Log.d("location", request);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "received callback");
                        callback.onSuccess(response);
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
                        callback.onError(error);
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String[] creds = new AccountManager(context).retrieveCredentials();
                String credentials = creds[0] + ":" + creds[1];
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }

        };
        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }

    public static void doPostRequest(String address, final Map<String, String> parameters, final VolleyCallback callback, final Context context, final Activity activity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                IP + address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
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
                callback.onError(error);
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

                if(new PreferenceManager(activity).getMyPreference("notifications")){
                    params.put("save_password", "true");
                    String hello1 = "";
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        hello1 += entry.getKey() + "=" + entry.getValue() + "&";
                    }
                    Log.d(TAG, "just put save_password in the params: " + hello1);
                }
                return params;
            }

            /*@Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String[] creds = new AccountManager(context).retrieveCredentials();
                String credentials = creds[0] + ":" + creds[1];
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

        };
        RequestQueueSingleton.getInstance(context)
                .getRequestQueue().add(stringRequest);
    }
}
