package com.corvid.genericdto.data.gdto.types;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeType extends AbstractType<LocalTime> {


    public LocalTimeType() {
        this(null);
    }

    public LocalTimeType(String regExp) {
        super(regExp);
    }

    public LocalTimeType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public LocalTime defaultValue(){
        return LocalTime.now();
    }

    protected LocalTime construct(String content) {

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(content,tf);
        } catch (NullPointerException e) {
            //ignore NPE coz content can be null
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "Local Time ='" + getValue() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalTimeType)) return false;
        LocalTimeType that = (LocalTimeType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}