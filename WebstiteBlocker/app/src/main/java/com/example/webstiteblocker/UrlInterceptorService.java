package com.example.webstiteblocker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UrlInterceptorService extends AccessibilityService {
    private HashMap<String, Long> previousUrlDetections = new HashMap<>();
    private List<String> urls = Arrays.asList("facebook.com","twitter.com","instagram.com","reddit.com","9gag.com");
    AppPreference preference;


    @Override
    protected void onServiceConnected() {
        preference =new  AppPreference(getApplicationContext());
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.packageNames = packageNames();
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        info.notificationTimeout = 300;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;

        this.setServiceInfo(info);
    }

    private String captureUrl(AccessibilityNodeInfo info, SupportedBrowserConfig config) {
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId);
        if (nodes == null || nodes.size() <= 0) {
            return null;
        }

        AccessibilityNodeInfo addressBarNodeInfo = nodes.get(0);
        String url = null;
        if (addressBarNodeInfo.getText() != null) {
            url = addressBarNodeInfo.getText().toString();
        }
        addressBarNodeInfo.recycle();
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAccessibilityEvent(@NonNull AccessibilityEvent event) {
        AccessibilityNodeInfo parentNodeInfo = event.getSource();
        if (parentNodeInfo == null) {
            return;
        }

        String packageName = event.getPackageName().toString();
        SupportedBrowserConfig browserConfig = null;
        for (SupportedBrowserConfig supportedConfig: getSupportedBrowsers()) {
            if (supportedConfig.packageName.equals(packageName)) {
                browserConfig = supportedConfig;
            }
        }

        if (browserConfig == null) {
            return;
        }

        String capturedUrl = captureUrl(parentNodeInfo, browserConfig);
        ((AccessibilityNodeInfo) parentNodeInfo).recycle();

        if (capturedUrl == null) {
            return;
        }

        long eventTime = event.getEventTime();
        String detectionId = packageName + ", and url " + capturedUrl;
        long lastRecordedTime = previousUrlDetections.containsKey(detectionId) ? previousUrlDetections.get(detectionId) : 0;
        if (eventTime - lastRecordedTime > 2000) {
            previousUrlDetections.put(detectionId, eventTime);

//            if(isInsideTimer()) {
                analyzeCapturedUrl(capturedUrl, browserConfig.packageName);
//            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isInsideTimer(){
        String[] start = preference.getStartTime().split(" ");
        String[] end = preference.getEndTime().split(" ");
        //TODO change time 12 to 24
        try {
            String startTime = start[0];
            Date time1 = new SimpleDateFormat("HH:mm").parse(startTime);
            Calendar startCalender = Calendar.getInstance();
            startCalender.setTime(time1);
            startCalender.add(Calendar.DATE, 1);


            String endTime = end[0];
            Date time2 = new SimpleDateFormat("HH:mm").parse(endTime);
            Calendar endCaleneder = Calendar.getInstance();
            endCaleneder.setTime(time2);
            endCaleneder.add(Calendar.DATE, 1);

            String currentTime = LocalDateTime.now().toString().split("T")[1];
            Date d = new SimpleDateFormat("HH:mm").parse(currentTime);
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(d);
            currentDate.add(Calendar.DATE, 1);

            Date x = currentDate.getTime();
            if (x.after(startCalender.getTime()) && x.before(endCaleneder.getTime())) {
               return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private void analyzeCapturedUrl(@NonNull String capturedUrl, @NonNull String browserPackage) {
        String redirectUrl = "https://nokkiirunnoippokittum.com";
        Boolean isWhiteList = preference.isWhiteList();
        boolean isFound = false;

        if(isWhiteList == false){
            for (String url : urls) {
                if (capturedUrl.contains(url)) {
                    isFound =true;
                }
            }
            if(isFound){
                performRedirect(redirectUrl,browserPackage);
            }
        }else {
            //TODO some issues are still there .workout hear
            for (String url : urls) {
                if (capturedUrl.contains(url) || capturedUrl.equals("search or type web address")) {
                    isFound = true;
                }
            }
            if(!isFound){

                performRedirect(redirectUrl,browserPackage);
            }
        }

    }


    private void performRedirect(@NonNull String redirectUrl, @NonNull String browserPackage) {
        Toast.makeText(this, "Url Found!!!", Toast.LENGTH_LONG).show();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData( Uri.parse(redirectUrl));
            intent.setPackage(browserPackage);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        catch(Exception e) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData( Uri.parse(redirectUrl));
            startActivity(i);
        }
    }

    @Override
    public void onInterrupt() { }

    @NonNull
    private static String[] packageNames() {
        List<String> packageNames = new ArrayList<>();
        for (SupportedBrowserConfig config: getSupportedBrowsers()) {
            packageNames.add(config.packageName);
        }
        return packageNames.toArray(new String[0]);
    }

    private static class SupportedBrowserConfig {
        public String packageName, addressBarId;
        public SupportedBrowserConfig(String packageName, String addressBarId) {
            this.packageName = packageName;
            this.addressBarId = addressBarId;
        }
    }

    @NonNull
    private static List<SupportedBrowserConfig> getSupportedBrowsers() {
        List<SupportedBrowserConfig> browsers = new ArrayList<>();
        browsers.add( new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
//        browsers.add( new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/url_bar_title"));
        return browsers;
    }
}