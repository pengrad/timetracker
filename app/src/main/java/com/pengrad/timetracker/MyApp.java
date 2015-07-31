package com.pengrad.timetracker;

import android.app.Application;
import android.content.Context;

import com.pengrad.timetracker.db.DatabaseHelper;

/**
 * stas
 * 7/29/15
 */
public class MyApp extends Application {

    public static DatabaseHelper getDatabaseHelper(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        return app.databaseHelper;
    }

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
