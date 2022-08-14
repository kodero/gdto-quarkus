package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.data.gdto.DateConverter;
import com.corvid.genericdto.shared.time.CalendarDate;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

public class CalendarDateType extends AbstractType<CalendarDate> {
    private static Logger log = Logger.getLogger(CalendarDateType.class.getName());

    public CalendarDateType(String regExp) {
        super(regExp);
    }

    public CalendarDateType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    public CalendarDate defaultValue(){
        return CalendarDate.date(1971, 1, 1);
    }

    public CalendarDateType() {
        this(null);
    }

    protected CalendarDate construct(String content) {
        log.info(" construct calendar date instance from string ' " + content + " '");
        if (content == null) return null;
        DateConverter dc = new DateConverter(false); /* MUST be false */
        try {
            Date date = dc.parse(content);
            return CalendarDate.fromDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "CalendarDateType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarDateType)) return false;
        CalendarDateType that = (CalendarDateType) o;
        if (this.t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}