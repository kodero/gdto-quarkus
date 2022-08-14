package com.corvid.genericdto.shared.time;

import com.corvid.genericdto.shared.ImmutableIterator;

import java.util.Iterator;

public abstract class AnnualDateSpecification extends DateSpecification {

    public abstract CalendarDate ofYear(int year);

    public CalendarDate firstOccurrenceIn(CalendarInterval interval) {
        CalendarDate firstTry = ofYear(interval.start().breachEncapsulationOf_year());
        if (interval.includes(firstTry))
            return firstTry;
        CalendarDate secondTry = ofYear(interval.start().breachEncapsulationOf_year() + 1);
        if (interval.includes(secondTry))
            return secondTry;
        return null;
    }

    public Iterator iterateOver(final CalendarInterval interval) {
        final AnnualDateSpecification spec = this;
        return new ImmutableIterator() {
            CalendarDate next = firstOccurrenceIn(interval);

            int year = next.breachEncapsulationOf_year();

            public boolean hasNext() {
                return next != null;
            }

            public Object next() {
                if (next == null)
                    return null;
                Object current = next;
                year += 1;
                next = spec.ofYear(year);
                if (!interval.includes(next))
                    next = null;
                return current;
            }
        };
    }
}