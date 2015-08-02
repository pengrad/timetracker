package com.pengrad.timetracker;

/**
 * stas
 * 8/3/15
 */
public class EventRaw {

    public final long _id;
    public final String packageName;
    public final long time;

    public EventRaw(String packageName, long time) {
        this(0, packageName, time);
    }

    public EventRaw(long _id, String packageName, long time) {
        this._id = _id;
        this.packageName = packageName;
        this.time = time;
    }
}
