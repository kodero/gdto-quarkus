

package com.corvid.genericdto.shared.time;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.TimeZone;

@Embeddable
public class ConcreteCalendarInterval extends CalendarInterval {
    private static final long serialVersionUID = 1L;

    @Embedded
    private CalendarDate start;

    @Embedded
    private CalendarDate end;

    public static ConcreteCalendarInterval from(CalendarDate start, CalendarDate end) {

        return new ConcreteCalendarInterval(start, end);
    }

    ConcreteCalendarInterval(CalendarDate start, CalendarDate end) {
        assertStartIsBeforeEnd(start, end);
        this.start = start;
        this.end = end;
    }

    @Override
    public TimeInterval asTimeInterval(TimeZone zone) {
        TimePoint startPoint = start.asTimeInterval(zone).start();
        TimePoint endPoint = end.asTimeInterval(zone).end();
        return TimeInterval.over(startPoint, endPoint);
    }

    @Override
    public Comparable upperLimit() {
        return end;
    }

    @Override
    public Comparable lowerLimit() {
        return start;
    }

    private static void assertStartIsBeforeEnd(CalendarDate start, CalendarDate end) {
        if (start != null && end != null && start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + " is not before or equal to " + end);
        }
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    public ConcreteCalendarInterval() {
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private CalendarDate getForPersistentMapping_End() {
        return end;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_End(CalendarDate end) {
        this.end = end;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private CalendarDate getForPersistentMapping_Start() {
        return start;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Start(CalendarDate start) {
        this.start = start;
    }

    public CalendarDate getStart() {
        return start;
    }

    public void setStart(CalendarDate start) {
        this.start = start;
    }

    public CalendarDate getEnd() {
        return end;
    }

    public void setEnd(CalendarDate end) {
        this.end = end;
    }
}