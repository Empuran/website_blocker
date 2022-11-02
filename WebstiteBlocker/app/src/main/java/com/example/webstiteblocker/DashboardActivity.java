package com.example.webstiteblocker;
/**
 * author: Jayasankar Punnakunnil
 * Date : 01-11-2022
 *
 * class : DashboardActivity.java
 * */
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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
    private String startTime,endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        sw_mode.setOnCheckedChangeListener(this);
        preference = new AppPreference(getApplicationContext());

        sw_mode.setChecked(!preference.isWhiteList());

        setTimeValues();
        tv_edit.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    /** Set Time value from preference */
    public void setTimeValues(){
        String meridian = "";
        String start = preference.getStartTime();
        String end = preference.getEndTime();

        String[] time =start.split(":");
        Calendar datetime = Calendar.getInstance();

        //FROM
        datetime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        datetime.set(Calendar.MINUTE, Integer.parseInt(time[1]));

        String hour = (datetime.get(Calendar.HOUR) == 0) ?"12":String.valueOf(datetime.get(Calendar.HOUR));
        String minute= time[1];
        if (datetime.get(Calendar.AM_PM) == Calendar.AM) {
            meridian = " AM";
        }else if (datetime.get(Calendar.AM_PM) == Calendar.PM) {
            meridian = " PM";
        }

        tv_start_time.setText(new StringBuilder().append(hour).append(":").append(minute).append(" ").append(meridian).toString());

        //TO
        time =end.split(":");

        datetime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        datetime.set(Calendar.MINUTE, Integer.parseInt(time[1]));

         hour = (datetime.get(Calendar.HOUR) == 0) ?"12":String.valueOf(datetime.get(Calendar.HOUR));
         minute= time[1];
        if (datetime.get(Calendar.AM_PM) == Calendar.AM) {
            meridian = " AM";
        }else if (datetime.get(Calendar.AM_PM) == Calendar.PM) {
            meridian = " PM";
        }

        tv_end_time.setText(new StringBuilder().append(hour).append(":").append(minute).append(" ").append(meridian).toString());

    }

    /** Mode changer */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            preference.setIsWhiteList(false);
            tv_description.setText(R.string.blacklist_desp);
        }else {
            preference.setIsWhiteList(true);
            tv_description.setText(R.string.whitelist_desp);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }
    public void exitDialog(){}

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_edit){
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            LinearLayout ll_from =  bottomSheetDialog.findViewById(R.id.ll_from);
            LinearLayout ll_to =  bottomSheetDialog.findViewById(R.id.ll_to);
            Button bt_save = bottomSheetDialog.findViewById(R.id.bt_save);
            ImageButton bt_close = bottomSheetDialog.findViewById(R.id.bt_close);
            TextView tv_start = bottomSheetDialog.findViewById(R.id.tv_start_time);
            TextView tv_end = bottomSheetDialog.findViewById(R.id.tv_end_time);

            tv_start.setText(tv_start_time.getText());
            tv_end.setText(tv_end_time.getText());

            ll_from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime(tv_start);
                }
            });

            ll_to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime(tv_end);
                }
            });

            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preference.setStartTime(startTime);
                    preference.setEndTime(endTime);
                    preference.setRunningStatus(true);

                    tv_start_time.setText(tv_start.getText());
                    tv_end_time.setText(tv_end.getText());

                    bottomSheetDialog.cancel();
                }
            });

            bt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.cancel();
                }
            });

            bottomSheetDialog.show();
        }
        else if(view.getId() == R.id.tv_delete){
            preference.restart();
            Intent i = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    /** Dialog for set time */
    public void setTime( TextView textView){

        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(DashboardActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String meridian = "";
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);

                        String hour = (datetime.get(Calendar.HOUR) == 0) ?"12":String.valueOf(datetime.get(Calendar.HOUR));

                        if (datetime.get(Calendar.AM_PM) == Calendar.AM) {
                            meridian = " AM";
                        }else if (datetime.get(Calendar.AM_PM) == Calendar.PM) {
                            meridian = " PM";
                        }

                        if(textView.getId() ==R.id.tv_start_time){
                            startTime =  hourOfDay+":"+minute ;
                        }else {
                            endTime = hourOfDay+":"+minute ;
                        }
                        textView.setText(hour+":"+minute+" "+meridian);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

}