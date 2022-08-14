package com.corvid.genericdto.shared.time;

import javax.persistence.Embeddable;

@Embeddable
public class MinuteOfHour {

    int minute;


    public MinuteOfHour() {
        super();
    }

    public static MinuteOfHour value(int initial) {
        return new MinuteOfHour(initial);
    }

    private MinuteOfHour(int initial) {
        if (initial < 0 || initial > 59)
            throw new IllegalArgumentException("Illegal hour for minute: "
                    + initial + ", please use a hour between 0 and 59");
        minute = initial;
    }

    public boolean equals(Object another) {
        if (!(another instanceof MinuteOfHour))
            return false;
        return equals((MinuteOfHour) another);
    }

    public boolean equals(MinuteOfHour another) {
        return minute == another.minute;
    }

    public int hashCode() {
        return minute;
    }

    public boolean isAfter(MinuteOfHour another) {
        return minute > another.minute;
    }

    public boolean isBefore(MinuteOfHour another) {
        return minute < another.minute;
    }

    public int value() {
        return minute;
    }

    public String toString() {
        return String.valueOf(minute);
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}