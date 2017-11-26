package com.akotnana.fcpsstudentvue.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anees on 11/26/2017.
 */

public class PreferenceManager {

    Activity a;

    public PreferenceManager(Activity a) { this.a = a; }

    public boolean getMyPreference(String key) {
        return a.getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean(key, true);
    }

    public void setMyPreference(String key, boolean newValue) {
        SharedPreferences pref = a.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, newValue);
        edit.apply();
    }
}
