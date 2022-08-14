

package com.corvid.genericdto.shared.time;

import java.util.Iterator;

public abstract class DateSpecification {

    public static DateSpecification fixed(int month, int day) {
        return new FixedDateSpecification(month, day);
    }

    public static DateSpecification nthOccuranceOfWeekdayInMonth(int month, int dayOfWeek, int n) {
        return new FloatingDateSpecification(month, dayOfWeek, n);
    }

    public abstract boolean isSatisfiedBy(CalendarDate date);

    public abstract CalendarDate firstOccurrenceIn(CalendarInterval interval);

    public abstract Iterator iterateOver(CalendarInterval interval);

}