package com.example.webstiteblocker;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_start_time)
    TextView tv_start_time;

    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_delete)
    TextView tv_delete;

    @BindView(R.id.sw_mode)
    SwitchCompat sw_mode;

    @BindView(R.id.tv_whitelist)
    TextView tv_whitelist;

    @BindView(R.id.tv_blacklist)
    TextView tv_blacklist;

    @BindView(R.id.tv_description)
    TextView tv_description;

    private AppPreference preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        sw_mode.setOnCheckedChangeListener(this);
        preference = new AppPreference(getApplicationContext());

        setTimeValues();
    }

    public void setTimeValues(){
        String start = preference.getStartTime();
        String end = preference.getEndTime();

        tv_start_time.setText(start);
        tv_end_time.setText(end);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            Toast.makeText(getApplicationContext(),"Black List",Toast.LENGTH_SHORT).show();
            preference.setIsWhiteList(false);
        }else {
            Toast.makeText(getApplicationContext(),"White List",Toast.LENGTH_SHORT).show();
            preference.setIsWhiteList(true);
        }
    }

    public boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            return false;
        } else {
            return true;
        }
    }

}