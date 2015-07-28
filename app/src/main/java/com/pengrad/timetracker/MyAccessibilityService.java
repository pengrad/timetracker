package com.pengrad.timetracker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

public class MyAccessibilityService extends AccessibilityService {

    public static final String TAG = "MyAccessibilityService";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16) {
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        }

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, event.getPackageName() + "  " + event.toString());
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}
