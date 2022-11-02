package com.example.webstiteblocker;
/**
 * author: Jayasankar Punnakunnil
 * Date : 01-11-2022
 *
 * class : MainActivity.java
 * */
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bt_add)
    ImageButton bt_add;


    private AppPreference preference;
    private String startTime,endTime;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setDefault();

        preference = new AppPreference(getApplicationContext());
        if(preference.getRunningStatus()){
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(i);
        }
        bt_add.setOnClickListener(this);

        initializeUrl();
    }

    /** initialize urls black-list urls */
    private void initializeUrl() {
        List<String>  list = preference.getUrl();
        if(preference.getUrl().size() == 0) {
            List<String> urls = Arrays.asList("facebook.com", "twitter.com", "instagram.com", "reddit.com", "9gag.com");
            for (String url : urls) {
                preference.addUrl(url);
            }
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }

    /** Set default time as current time */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDefault() {
        LocalDateTime now = LocalDateTime.now();
        String meridian ="";

        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY,now.getHour());
        current.set(Calendar.MINUTE,now.getMinute());
        if (current.get(Calendar.AM_PM) == Calendar.AM) {
            meridian = "AM";
        }else if (current.get(Calendar.AM_PM) == Calendar.PM) {
            meridian = " PM";
        }
        StringBuilder builder = new StringBuilder(new StringBuilder().append(now.getHour()).append(":").append(now.getMinute()).append(":").append(meridian).toString());
        startTime = endTime =builder.toString();
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_add) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);LinearLayout ll_from =  bottomSheetDialog.findViewById(R.id.ll_from);
            LinearLayout ll_to =  bottomSheetDialog.findViewById(R.id.ll_to);
            Button bt_save = bottomSheetDialog.findViewById(R.id.bt_save);
            ImageButton bt_close = bottomSheetDialog.findViewById(R.id.bt_close);
            TextView tv_start_time = bottomSheetDialog.findViewById(R.id.tv_start_time);
            TextView tv_end_time = bottomSheetDialog.findViewById(R.id.tv_end_time);

            LocalDateTime now = LocalDateTime.now();
            String meridian = "";

            Calendar current = Calendar.getInstance();
            current.set(Calendar.HOUR_OF_DAY,now.getHour());
            current.set(Calendar.MINUTE,now.getMinute());
            String hour = (current.get(Calendar.HOUR) == 0) ?"12":String.valueOf(current.get(Calendar.HOUR));
            if (current.get(Calendar.AM_PM) == Calendar.AM) {
                meridian = " AM";
            }else if (current.get(Calendar.AM_PM) == Calendar.PM) {
                meridian = " PM";
            }

            tv_start_time.setText(new StringBuilder().append(hour).append(":").append(now.getMinute()).append(" ").append(meridian).toString());
            tv_end_time.setText(new StringBuilder().append(hour).append(":").append(now.getMinute()).append(" ").append(meridian).toString());

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
                    preference.setStartTime(startTime);
                    preference.setEndTime(endTime);
                    preference.setRunningStatus(true);

                    if(preference.isPermissionAsked() == false) {
                        askAccessibilityPermissions();
                    }else {
                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }
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
    /** Time picker dialog */
    public void setTime( TextView textView){

        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
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

    /** Dialog for ask accessibility permission */
    public void askAccessibilityPermissions () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WebSiteBlocker needs permissions");
        builder.setMessage(R.string.permission_accessibility);
        builder.setPositiveButton("Ask Permission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent,101);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Dialog for ask pup-up permission  */
    public void askPopupPermissions () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WebSiteBlocker needs permissions");
        builder.setMessage(R.string.permission_popup);
        builder.setPositiveButton("Ask Permission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivityForResult(intent,102);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}});

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /** Permission handler */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==101){
            Log.i("DDD",requestCode+" ");
            askPopupPermissions();
        }else if(requestCode == 102){
            preference.setPermissionAsk(true);
            Log.i("DDD",requestCode+" ");
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(i);
        }else {
            Log.i("DDD","Something wrong");
        }
    }
}