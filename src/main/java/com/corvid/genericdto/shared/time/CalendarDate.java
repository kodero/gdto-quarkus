package com.corvid.genericdto.shared.time;

import com.corvid.genericdto.data.gdto.CalendarDateDeserializer;
import com.corvid.genericdto.data.gdto.CalendarDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Embeddable
@JsonDeserialize(using = CalendarDateDeserializer.class)
@JsonSerialize(using = CalendarDateSerializer.class)
public class CalendarDate implements Comparable, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "MONTH")
    private int month; // 1 based: January = 1, February = 2, ...

    @Column(name = "DAY")
    private int day;

    public static CalendarDate date(int year, int month, int day) {
        return CalendarDate.from(year, month, day);
    }

    public static CalendarDate from(int year, int month, int day) {
        CalendarDate result = new CalendarDate(year, month, day);
        return result;

    }

    public static CalendarDate from(String dateString, String pattern) {
        TimeZone arbitraryZone = TimeZone.getTimeZone("Universal");
        // Any timezone works, as long as the same one is used throughout.
        TimePoint point = TimePoint.parseFrom(dateString, pattern, arbitraryZone);
        return CalendarDate.from(point, arbitraryZone);
    }

    public static CalendarDate from(TimePoint timePoint, TimeZone zone) {
        Calendar calendar = timePoint.asJavaCalendar();
        calendar.setTimeZone(zone);
        return CalendarDate._from(calendar);
    }

    public static CalendarDate now() {
        return _from(Calendar.getInstance());
    }

    public static CalendarDate fromDate(Date date) {
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(date);
        return _from(myCal);
    }

    public static CalendarDate _from(Calendar calendar) {
        // Use timezone already set in calendar.
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // T&M Lib counts January
        // as 1
        int date = calendar.get(Calendar.DATE);
        return CalendarDate.from(year, month, date);
    }

    public CalendarDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String toString() {
        return toString("yyyy-MM-dd"); // default for console
    }

    public String toString(String pattern) {
        TimeZone arbitraryZone = TimeZone.getTimeZone("Universal");
        // Any timezone works, as long as the same one is used throughout.
        TimePoint point = startAsTimePoint(arbitraryZone);
        return point.toString(pattern, arbitraryZone);
    }

    public boolean isBefore(CalendarDate other) {
        if (other == null)
            return false;
        if (year < other.year)
            return true;
        if (year > other.year)
            return false;
        if (month < other.month)
            return true;
        if (month > other.month)
            return false;
        return day < other.day;
    }

    public boolean isAfter(CalendarDate other) {
        if (other == null)
            return false;
        return !isBefore(other) && !this.equals(other);
    }

    public int compareTo(Object other) {
        try {
            return compareTo((CalendarDate) other);
        } catch (ClassCastException ex) {
            return -1;
        }
    }

    public int compareTo(CalendarDate other) {
        if (other == null)
            return -1;
        if (isBefore(other))
            return -1;
        if (isAfter(other))
            return 1;
        return 0;
    }

    public boolean equals(Object object) {
        try {
            return equals((CalendarDate) object);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    public boolean equals(CalendarDate other) {
        return other != null && this.year == other.year
                && this.month == other.month && this.day == other.day;
    }

    public int hashCode() {
        return year * month * day;
    }

    public CalendarDate start() {
        return this;
    }

    public CalendarDate end() {
        return this;
    }

    public CalendarDate nextDay() {
        return this.plusDays(1);
    }

    public CalendarDate previousDay() {
        return this.plusDays(-1);
    }


    public CalendarInterval month() {
        return CalendarInterval.month(year, month);
    }

    public CalendarInterval year() {
        return CalendarInterval.year(year);
    }

    public CalendarDate plusDays(int increment) {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        calendar.add(Calendar.DATE, increment);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return CalendarDate.from(year, month, day);
    }

    public CalendarDate plusMonths(int increment) {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        calendar.add(Calendar.MONTH, increment);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return CalendarDate.from(year, month, day);
    }

    public CalendarDate plus(Duration length) {
        return length.addedTo(this);
    }

    public Calendar asJavaCalendarUniversalZoneMidnight() {
        TimeZone zone = TimeZone.getTimeZone("Universal");
        Calendar calendar = Calendar.getInstance(zone);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public TimeInterval asTimeInterval(TimeZone zone) {
        return TimeInterval.startingFrom(startAsTimePoint(zone), true,
                Duration.days(1), false);
    }

    public TimePoint startAsTimePoint(TimeZone zone) {
        return TimePoint.atMidnight(year, month, day, zone);
    }

    public CalendarInterval through(CalendarDate otherDate) {
        return CalendarInterval.inclusive(this, otherDate);
    }

    public int dayOfWeek() {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int breachEncapsulationOf_day() {
        return day;
    }

    public int breachEncapsulationOf_month() {
        return month;
    }

    public int breachEncapsulationOf_year() {
        return year;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    protected CalendarDate() {
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private int getForPersistentMapping_Day() {
        return day;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private void setForPersistentMapping_Day(int day) {
        this.day = day;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private int getForPersistentMapping_Month() {
        return month;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private void setForPersistentMapping_Month(int month) {
        this.month = month;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private int getForPersistentMapping_Year() {
        return year;
    }

    // Only for use by persistence mapping frameworks
    // <rant>These methods break encapsulation and we put them in here
    // begrudgingly</rant>
    private void setForPersistentMapping_Year(int year) {
        this.year = year;
    }

	/*public CalendarMinute at(TimeOfDay timeOfDay) {
        return CalendarMinute.dateAndTimeOfDay(this, timeOfDay);
	}
*/

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}