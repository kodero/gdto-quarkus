package com.corvid.genericdto.shared.time;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class TimeOfDay {
    @Embedded
    private HourOfDay hour;

    @Embedded
    private MinuteOfHour minute;

    public static TimeOfDay hourAndMinute(int hour, int minute) {
        return new TimeOfDay(hour, minute);
    }

    private TimeOfDay(int hour, int minute) {
        this.hour = HourOfDay.value(hour);
        this.minute = MinuteOfHour.value(minute);
    }


    public String toString() {
        return hour.toString() + ":" + minute.toString();
    }

    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof TimeOfDay))
            return false;
        return equals((TimeOfDay) anotherObject);
    }

    public boolean equals(TimeOfDay another) {
        if (another == null)
            return false;
        return hour.equals(another.hour) && minute.equals(another.minute);
    }

    public int hashCode() {
        return hour.hashCode() ^ minute.hashCode();
    }

    public boolean isAfter(TimeOfDay another) {
        return hour.isAfter(another.hour) || hour.equals(another)
                && minute.isAfter(another.minute);
    }

    public boolean isBefore(TimeOfDay another) {
        return hour.isBefore(another.hour) || hour.equals(another)
                && minute.isBefore(another.minute);
    }

    int getHour() {
        return hour.value();
    }

    int getMinute() {
        return minute.value();
    }


    public TimeOfDay() {
    }

    private int getForPersistentMapping_Hour() {
        return hour.value();
    }

    private void setForPersistentMapping_Hour(int hour) {
        this.hour = HourOfDay.value(hour);
    }

    private int getForPersistentMapping_Minute() {
        return minute.value();
    }

    private void setForPersistentMapping_Minute(int minute) {
        this.minute = MinuteOfHour.value(minute);
    }


}