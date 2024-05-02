package com.tienvm.accessservice;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessService extends AccessibilityService {
    private AccessibilityServiceInfo info = new AccessibilityServiceInfo();

    @Override
    protected void onServiceConnected() {
        Log.println(Log.ASSERT, "On Accessbility Event", "action onServiceConnected");
        // Set the type of events that this service wants to listen to. Others
        // aren't passed to this service.

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED |
                AccessibilityEvent.CONTENT_CHANGE_TYPE_DRAG_STARTED |
                AccessibilityEvent.CONTENT_CHANGE_TYPE_DRAG_DROPPED;

        // If you only want this service to work with specific apps, set their
        // package names here. Otherwise, when the service is activated, it listens
        // to events from all apps.
//        info.packageNames = new String[]
//                {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};

        // Set the type of feedback your service provides.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        // Default services are invoked only if no package-specific services are
        // present for the type of AccessibilityEvent generated. This service is
        // app-specific, so the flag isn't necessary. For a general-purpose service,
        // consider setting the DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    private void doRightThenDownDrag() {
        Path dragRightPath = new Path();
        dragRightPath.moveTo(200, 200);
        dragRightPath.lineTo(400, 200);
        long dragRightDuration = 500L; // 0.5 second

        // The starting point of the second path must match
        // the ending point of the first path.
        Path dragDownPath = new Path();
        dragDownPath.moveTo(100, 200);
        dragDownPath.lineTo(200, 400);
        long dragDownDuration = 500L;
        GestureDescription.StrokeDescription rightThenDownDrag =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rightThenDownDrag = new GestureDescription.StrokeDescription(dragRightPath, 0L,
                    dragRightDuration, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rightThenDownDrag.continueStroke(dragDownPath, dragRightDuration,
                    dragDownDuration, false);
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.println(Log.ASSERT, "On Accessbility Event", "action key event " + event.getCharacters());
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        doRightThenDownDrag();
        Log.println(Log.ASSERT, "On Accessbility Event", "action " + event.getAction());
    }

    @Override
    public void onInterrupt() {
        Log.println(Log.ASSERT, "On onInterrupt", "hello interrupt");
    }
}
