package com.example.webstiteblocker;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        sw_mode.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

    }
}