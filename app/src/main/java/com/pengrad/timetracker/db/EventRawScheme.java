package com.pengrad.timetracker.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.pengrad.timetracker.EventRaw;

/**
 * stas
 * 7/31/15
 */
public class EventRawScheme {

    public static final String CREATE_TABLE = "create table events_raw (_id integer primary key autoincrement, package text, time integer)";
    public static final String DROP_TABLE = "drop table if exists events_raw";

    private static final String SELECT_LAST_EVENT = "select * from events_raw where _id=(select max(_id) from events_raw)";
    private static final String SELECT_ALL = "select * from events_raw";

    private static final String INSERT = "insert into events_raw(package, time) values(\"%s\", %d)";
    private static final String DELETE = "delete from events_raw where _id = %d";


    private static EventRaw eventFromCursor(Cursor cursor) {
        return new EventRaw(cursor.getLong(0), cursor.getString(1), cursor.getLong(2));
    }

    public static void addEvent(SQLiteDatabase db, EventRaw event) {
        String sql = String.format(INSERT, event.packageName, event.time);
        db.execSQL(sql);
    }

    @Nullable
    public static EventRaw getLastEvent(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SELECT_LAST_EVENT, null);
        EventRaw event = null;
        if (cursor.moveToNext()) {
            event = eventFromCursor(cursor);
        }
        cursor.close();
        return event;
    }

    public static void deleteEvent(SQLiteDatabase db, EventRaw event) {
        String sql = String.format(DELETE, event._id);
        db.execSQL(sql);
    }
}
