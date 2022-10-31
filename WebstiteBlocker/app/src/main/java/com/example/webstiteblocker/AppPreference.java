package com.example.webstiteblocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreference {
    private Context context;
    private SharedPreferences pref;

    public AppPreference(Context context) {
        this.context=context;
        pref = context.getSharedPreferences(Constants.MY_PREFS_NAME,Context.MODE_PRIVATE);
    }

    public void setStartTime(String time){
        pref.edit().putString(Constants.PREF_START_TIME,time).apply();
    }
    public String getStartTime(){
        String time = pref.getString(Constants.PREF_START_TIME,"");
        return time;
    }

    public void setEndTime(String time){
        pref.edit().putString(Constants.PREF_END_TIME,time).apply();
    }
    public String getEndTime(){
        String time = pref.getString(Constants.PREF_END_TIME,"");
        return time;
    }

    public void setRunningStatus(Boolean status){
        pref.edit().putBoolean(Constants.PREF_IS_RUNNING,status).apply();
    }
    public Boolean getRunningStatus(){
        Boolean status = pref.getBoolean(Constants.PREF_IS_RUNNING,false);
        return status;
    }


    public void setIsWhiteList(boolean isWhiteList) {
        pref.edit().putBoolean(Constants.PREF_IS_WHITELIST,isWhiteList).apply();
    }

    public Boolean isWhiteList() {
        Boolean status = pref.getBoolean(Constants.PREF_IS_WHITELIST,false);
        return status;
    }
}
