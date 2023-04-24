package com.example.myweatherbase.activities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class MyPreferenceManager {

    private static MyPreferenceManager instance;
    private SharedPreferences preferences;
    private MyPreferenceManager(){
    }
    public static MyPreferenceManager getInstance(Context context){
        if (instance==null)
            instance = new MyPreferenceManager();

        instance.initialize(context);
        return instance;
    }
    private void initialize(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getApi() {
        return preferences.getString("API", "default view");
    }
    public String getUnits(){
        return preferences.getString("listUnits", "standard");
    }
    public String getLang(){
        return preferences.getString("listLang", "en");
    }

}
