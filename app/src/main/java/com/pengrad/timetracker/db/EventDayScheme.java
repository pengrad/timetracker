package com.pengrad.timetracker.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pengrad.timetracker.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * stas
 * 7/31/15
 */
public class EventDayScheme {

    public static final String CREATE_TABLE = "create table events_day (date text, package text, duration integer default 0, primary key(date, package))";
    public static final String DROP_TABLE = "drop table if exists events_day";

    private static final String SELECT_ALL = "select * from events_day";
    private static final String SELECT_BY_DATE = "select * from events_day where date='%s'";

    private static final String UPDATE = "update events_day set duration = duration + %d where date='%s' and package='%s'";
    private static final String INSERT = "insert or ignore into events_day(date, package, duration) values('%s', '%s', %d)";

    public static Event eventFromCursor(Cursor cursor) {
        return new Event(cursor.getString(1), cursor.getLong(2));
    }

    public static List<Event> getEventsByDay(SQLiteDatabase db, Date date) {
        String dateString = StringDate.fromDate(date);
//        String sql = String.format(SELECT_BY_DATE, dateString);
        String sql = SELECT_ALL;
        Cursor cursor = db.rawQuery(sql, null);
        List<Event> events = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            events.add(EventDayScheme.eventFromCursor(cursor));
        }
        cursor.close();
        return events;
    }

    public static void addEvent(SQLiteDatabase db, Event event) {
        db.execSQL(updateQuery(event));
        db.execSQL(insertQuery(event));
    }

    private static String updateQuery(Event event) {
        String date = StringDate.fromMillis(event.time);
        return String.format(UPDATE, event.duration, date, event.packageName);
    }

    private static String insertQuery(Event event) {
        String date = StringDate.fromMillis(event.time);
        return String.format(INSERT, date, event.packageName, event.duration);
    }

    public static class StringDate {

        private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        public static String fromDate(Date date) {
            return FORMATTER.format(date);
        }

        public static String fromMillis(long millis) {
            return FORMATTER.format(new Date(millis));
        }

    }
}
