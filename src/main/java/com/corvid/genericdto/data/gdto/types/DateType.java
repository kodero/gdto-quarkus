package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.data.gdto.DateConverter;

import java.text.ParseException;
import java.util.Date;

public class DateType extends AbstractType<Date> {


    public DateType() {
        this(null);
    }

    public DateType(String regExp) {
        super(regExp);
    }

    public DateType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Date defaultValue(){
        return new Date(0);
    }

    protected Date construct(String content) {
        DateConverter sdf = new DateConverter(true);
        try {
            return sdf.parse(content);
        } catch (ParseException | NullPointerException e) {
            //ignore NPE coz content can be null
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "DateType ='" + getValue() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateType)) return false;
        DateType that = (DateType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }


}