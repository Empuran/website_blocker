package com.example.webstiteblocker;
/**
 * author: Jayasankar Punnakunnil
 * Date : 01-11-2022
 *
 * class : AppPreference.java
 * */
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppPreference {
    private Context context;
    private SharedPreferences pref;

    public AppPreference(Context context) {
        this.context=context;
        pref = context.getSharedPreferences(Constants.MY_PREFS_NAME,Context.MODE_PRIVATE);

    }

    public void addUrl(String url){
        String urls = pref.getString(Constants.PREFS_URL_LIST,"");
        StringBuilder csvList = new StringBuilder();
        csvList.append(urls);
        csvList.append(",");
        csvList.append(url);

        pref.edit().putString(Constants.PREFS_URL_LIST,csvList.toString()).apply();
    }

    public List<String> getUrl(){
        String urls = pref.getString(Constants.PREFS_URL_LIST,"");
        return urls !=""? Arrays.asList(urls.split(",")) : new ArrayList<>();
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

    public void setPermissionAsk(boolean status) {
        pref.edit().putBoolean(Constants.PREF_PERMISSION_ASK,status).apply();
    }

    public Boolean isPermissionAsked() {
        Boolean status = pref.getBoolean(Constants.PREF_PERMISSION_ASK,false);
        return status;
    }

    public void restart() {
        SharedPreferences.Editor editor =  pref.edit();
        editor.putString(Constants.PREF_START_TIME,"");
        editor.putString(Constants.PREF_END_TIME,"");
        editor.putBoolean(Constants.PREF_IS_RUNNING,false);
        editor.apply();

    }
}
