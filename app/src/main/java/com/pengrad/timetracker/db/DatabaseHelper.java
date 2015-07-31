package com.pengrad.timetracker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.pengrad.timetracker.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private SQLiteDatabase getDb() {
        return getWritableDatabase();
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = getDb();
        Event lastEvent = getLastEvent(db);
        if (lastEvent != null) {
            updateEventDuration(lastEvent._id, event.time - lastEvent.time - lastEvent.duration);
            if (!lastEvent.packageName.equals(event.packageName)) {
                insertEvent(db, event);
            }
        } else {
            insertEvent(db, event);
        }
    }

    @Nullable
    private Event getLastEvent(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(EventRawScheme.SELECT_LAST_EVENT, null);
        Event event = null;
        if (cursor.moveToNext()) {
            event = EventRawScheme.eventFromCursor(cursor);
        }
        cursor.close();
        return event;
    }

    private void insertEvent(SQLiteDatabase db, Event event) {
        db.execSQL(EventRawScheme.insert(event));
    }

    public void updateEventDuration(long eventId, long duration) {
        getWritableDatabase().execSQL(EventRawScheme.updateDuration(eventId, duration));
    }

    private List<Event> getRawEvents() {
        Cursor cursor = getReadableDatabase().rawQuery(EventRawScheme.SELECT_ALL, null);
        List<Event> events = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            events.add(EventRawScheme.eventFromCursor(cursor));
        }
        cursor.close();
        return events;
    }

    public synchronized void aggregateEvents() {
        List<Event> events = getRawEvents();
        Map<String, Event> eventsMap = new HashMap<>(events.size());
        for (Event event : events) {
            Event eventMap = eventsMap.get(event.packageName);
            if (eventMap != null) {
                eventMap.addDuration(event.duration);
            } else {
                eventsMap.put(event.packageName, event);
            }
        }

        SQLiteDatabase db = getDb();
        db.beginTransaction();
        try {
            //insert
            for (Event event : events) {
                db.execSQL(EventDayScheme.update(event));
                db.execSQL(EventDayScheme.insert(event));
            }

            //delete
            if (events.size() > 0) {
                long minId = events.get(0)._id;
                long maxId = events.get(events.size() - 1)._id;
                db.execSQL(EventRawScheme.deleteRange(minId, maxId));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public synchronized List<Event> getEventsByDay(Date date) {
        String dateString = StringDate.fromDate(date);
        Cursor cursor = getReadableDatabase().rawQuery(EventDayScheme.selectByDay(dateString), null);
        List<Event> events = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            events.add(EventDayScheme.eventFromCursor(cursor));
        }
        cursor.close();
        return events;
    }
}
