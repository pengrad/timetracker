package com.pengrad.timetracker;

/**
 * stas
 * 7/28/15
 */
public class Event {

    public final String packageName;
    public final long duration;
    public final long time;

    public Event(String packageName, long duration, long time) {
        this.packageName = packageName;
        this.duration = duration;
        this.time = time;
    }

    public Event(String packageName, long duration) {
        this(packageName, duration, 0);
    }

    @Override
    public String toString() {
        return packageName + " " + duration / 1000;
    }
}
