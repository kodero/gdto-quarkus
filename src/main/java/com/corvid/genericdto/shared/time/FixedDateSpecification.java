

package com.corvid.genericdto.shared.time;

import javax.persistence.Embeddable;

@Embeddable
public class FixedDateSpecification extends AnnualDateSpecification {
    private int month;

    private int day;

    public FixedDateSpecification(int month, int day) {
        this.month = month;
        this.day = day;
    }

    public CalendarDate ofYear(int year) {
        return CalendarDate.date(year, month, day);
    }

    public boolean isSatisfiedBy(CalendarDate date) {
        return day == date.breachEncapsulationOf_day() && month == date.breachEncapsulationOf_month();
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    protected FixedDateSpecification() {
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Day() {
        return day;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Day(int day) {
        this.day = day;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Month() {
        return month;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Month(int month) {
        this.month = month;
    }

}