package com.corvid.genericdto.shared.time;

import javax.persistence.Embeddable;

@Embeddable
public class HourOfDay {

    int hour;


    public HourOfDay() {
        super();
    }

    public static HourOfDay value(int initial) {
        return new HourOfDay(initial);
    }

    public static HourOfDay value(int initial, String am_pm) {
        return HourOfDay.value(convertTo24hour(initial, am_pm));
    }

    private static int convertTo24hour(int hour, String am_pm) {
        if (!("AM".equalsIgnoreCase(am_pm) || "PM".equalsIgnoreCase(am_pm)))
            throw new IllegalArgumentException("AM PM indicator invalid: "
                    + am_pm + ", please use AM or PM");
        if (hour < 0 | hour > 12)
            throw new IllegalArgumentException("Illegal hour for 12 hour: "
                    + hour + ", please use a hour between 0 and 11");
        int translatedAmPm = "AM".equalsIgnoreCase(am_pm) ? 0 : 12;
        translatedAmPm -= (hour == 12) ? 12 : 0;
        return hour + translatedAmPm;
    }

    private HourOfDay(int initial) {
        if (initial < 0 || initial > 23)
            throw new IllegalArgumentException("Illegal hour for 24 hour: "
                    + initial + ", please use a hour between 0 and 23");
        hour = initial;
    }

    public boolean equals(Object another) {
        if (!(another instanceof HourOfDay))
            return false;
        return equals((HourOfDay) another);
    }

    public boolean equals(HourOfDay another) {
        return hour == another.hour;
    }

    public int hashCode() {
        return hour;
    }

    public boolean isAfter(HourOfDay another) {
        return hour > another.hour;
    }

    public boolean isBefore(HourOfDay another) {
        return hour < another.hour;
    }

    public int value() {
        return hour;
    }

    public String toString() {
        return String.valueOf(hour);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}