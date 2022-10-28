package com.example.webstiteblocker;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bt_add)
    ImageButton bt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_add) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            LinearLayout ll_from =  bottomSheetDialog.findViewById(R.id.ll_from);
            LinearLayout ll_to =  bottomSheetDialog.findViewById(R.id.ll_to);
            Button bt_save = bottomSheetDialog.findViewById(R.id.bt_save);
            ImageButton bt_close = bottomSheetDialog.findViewById(R.id.bt_close);
            TextView tv_start_time = bottomSheetDialog.findViewById(R.id.tv_start_time);
            TextView tv_end_time = bottomSheetDialog.findViewById(R.id.tv_end_time);

            ll_from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime(tv_start_time);
                }
            });

            ll_to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime(tv_end_time);
                }
            });

            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(i);
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
    }

    public void setTime( TextView textView){

        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay>=12?hourOfDay-12:hourOfDay;
                         hour = hour==0?12:hour;
                        String timestamp = hourOfDay>=12?" PM":" AM";
                        String time = hour + ":" + minute + timestamp;
                        textView.setText(time);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }
}