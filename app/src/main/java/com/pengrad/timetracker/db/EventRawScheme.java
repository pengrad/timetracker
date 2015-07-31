package com.pengrad.timetracker.db;

import android.database.Cursor;

import com.pengrad.timetracker.Event;

/**
 * stas
 * 7/31/15
 */
public class EventRawScheme {

    public static final String CREATE_TABLE = "create table events_raw (_id integer primary key autoincrement, package text, time integer, duration integer default 0)";
    public static final String DROP_TABLE = "drop table if exists events_raw";

    public static final String SELECT_LAST_EVENT = "select * from events_raw where _id=(select max(_id) from events_raw)";
    public static final String SELECT_ALL = "select * from events_raw";


    public static Event eventFromCursor(Cursor cursor) {
        return new Event(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3));
    }

    public static String insert(Event event) {
        String sql = "insert into events_raw(package, time, duration) values(\"%s\",%d,%d)";
        return String.format(sql, event.packageName, event.time, event.duration);
    }

    public static String updateDuration(long eventId, long duration) {
        return "update events_raw set duration=duration+" + duration + " where _id =" + eventId;
    }

    public static String deleteRange(long minId, long maxId) {
        return "delete from events_raw where _id >=" + minId + " and _id <=" + maxId;
    }
}
