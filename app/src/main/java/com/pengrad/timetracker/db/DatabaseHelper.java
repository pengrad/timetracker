package com.pengrad.timetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pengrad.timetracker.Event;
import com.pengrad.timetracker.EventRaw;

import java.util.Date;
import java.util.List;

/**
 * stas
 * 7/28/15
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDB.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventRawScheme.CREATE_TABLE);
        db.execSQL(EventDayScheme.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EventRawScheme.DROP_TABLE);
        db.execSQL(EventDayScheme.DROP_TABLE);
        onCreate(db);
    }

    public synchronized void addEvent(EventRaw eventRaw) {
        SQLiteDatabase db = getWritableDatabase();
        EventRaw lastEvent = EventRawScheme.getLastEvent(db);

        if (lastEvent != null) {
            long duration = eventRaw.time - lastEvent.time;
            Event event = new Event(lastEvent.packageName, duration, lastEvent.time);
            EventDayScheme.addEvent(db, event);
            EventRawScheme.deleteEvent(db, lastEvent);
        }
        EventRawScheme.addEvent(db, eventRaw);

    }

    public List<Event> getEventsByDay(Date date) {
        return EventDayScheme.getEventsByDay(getReadableDatabase(), date);
    }
}
