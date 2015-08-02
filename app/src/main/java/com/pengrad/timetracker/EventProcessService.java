package com.pengrad.timetracker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;

import com.pengrad.timetracker.db.DatabaseHelper;

public class EventProcessService extends IntentService {

    private static final String ACTION_PROCESS_EVENT = "com.pengrad.timetracker.action.PROCESS";
    private static final String EXTRA_EVENT = "com.pengrad.timetracker.extra.EVENT";

    public static void startActionProcess(Context context, AccessibilityEvent event) {
        Intent intent = new Intent(context, EventProcessService.class);
        intent.setAction(ACTION_PROCESS_EVENT);
        intent.putExtra(EXTRA_EVENT, event);
        context.startService(intent);
    }

    public EventProcessService() {
        super("EventProcessService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_EVENT.equals(action)) {
                handleEvent(intent.getParcelableExtra(EXTRA_EVENT));
            }
        }
    }

    private void handleEvent(AccessibilityEvent accEvent) {
        long time = toUtcTime(accEvent.getEventTime());
        String packageName = accEvent.getPackageName().toString();
        EventRaw event = new EventRaw(packageName, time);

        DatabaseHelper databaseHelper = MyApp.getDatabaseHelper(this);
        databaseHelper.addEvent(event);
    }

    private long toUtcTime(long eventTime) {
        return System.currentTimeMillis() - SystemClock.uptimeMillis() + eventTime;
    }
}
