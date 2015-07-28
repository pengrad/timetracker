package com.pengrad.timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.ArrayList;
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
        db.execSQL("create table events_raw (_id integer primary key autoincrement, package text, time integer, duration integer default 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists events_raw");
        onCreate(db);
    }

    private SQLiteDatabase getDb() {
        return getWritableDatabase();
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = getDb();
        Event lastEvent = getLastEvent(db);
        if (lastEvent != null) {
            updateEventDuration(lastEvent._id, event.time - lastEvent.time);
            if (!lastEvent.packageName.equals(event.packageName)) {
                insertEvent(db, event);
            }
        } else {
            insertEvent(db, event);
        }
    }

    @Nullable
    private Event getLastEvent(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from events_raw where _id=(select max(_id) from events_raw)", null);
        Event event = null;
        if (cursor.moveToNext()) {
            event = eventFromCursor(cursor);
        }
        cursor.close();
        return event;
    }

    private void insertEvent(SQLiteDatabase db, Event event) {
        ContentValues values = new ContentValues();
        values.put("package", event.packageName);
        values.put("time", event.time);
        values.put("duration", event.duration);
        db.insert("events_raw", null, values);
    }

    private Event eventFromCursor(Cursor cursor) {
        return new Event(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3));
    }

    public void updateEventDuration(long eventId, long duration) {
        getWritableDatabase().execSQL("update events_raw set duration=duration+" + duration + " where _id =" + eventId);
    }

    public List<Event> getEvents() {
        Cursor cursor = getReadableDatabase().rawQuery("select * from events_raw", null);
        List<Event> events = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            events.add(eventFromCursor(cursor));
        }
        cursor.close();
        return events;
    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
