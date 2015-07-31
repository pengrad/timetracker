package com.pengrad.timetracker.db;

import android.database.Cursor;

import com.pengrad.timetracker.Event;

/**
 * stas
 * 7/31/15
 */
public class EventDayScheme {

    public static final String CREATE_TABLE = "create table events_day (date text, package text, duration integer default 0, primary key(date, package))";
    public static final String DROP_TABLE = "drop table if exists events_day";

    public static final String SELECT_ALL = "select * from events_day";

    public static String selectByDay(String date) {
        return "select * from events_day"; // where date='" + date + "'";
    }

    public static Event eventFromCursor(Cursor cursor) {
        return Event.withDuration(cursor.getString(1), cursor.getLong(2));
    }

    public static String update(Event event) {
        String sql = "update events_day set duration = duration + %d where date=\"%s\" and package=\"%s\"";
        String date = StringDate.fromMillis(event.time);
        return String.format(sql, event.duration, date, event.packageName);
    }

    public static String insert(Event event) {
        String sql = "insert or ignore into events_day(date, package, duration) values(\"%s\", \"%s\", %d)";
        String date = StringDate.fromMillis(event.time);
        return String.format(sql, date, event.packageName, event.duration);
    }
}
