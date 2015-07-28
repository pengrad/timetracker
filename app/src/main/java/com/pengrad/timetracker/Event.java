package com.pengrad.timetracker;

import java.util.concurrent.TimeUnit;

/**
 * stas
 * 7/28/15
 */
public class Event {

    public final long _id;
    public final String packageName;
    public final long time;
    public final long duration;

    public Event(String packageName, long time) {
        this(0, packageName, time, 0);
    }

    public Event(long _id, String packageName, long time, long duration) {
        this._id = _id;
        this.packageName = packageName;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return packageName + " " + TimeUnit.MILLISECONDS.toSeconds(duration);
    }
}
