

package com.corvid.genericdto.shared.time;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class TimeUnit implements Comparable, Serializable, TimeUnitConversionFactors {
    public static final TimeUnit millisecond = new TimeUnit(Type.millisecond, Type.millisecond, 1);

    public static final TimeUnit second = new TimeUnit(Type.second, Type.millisecond, millisecondsPerSecond);

    public static final TimeUnit minute = new TimeUnit(Type.minute, Type.millisecond, millisecondsPerMinute);

    public static final TimeUnit hour = new TimeUnit(Type.hour, Type.millisecond, millisecondsPerHour);

    public static final TimeUnit day = new TimeUnit(Type.day, Type.millisecond, millisecondsPerDay);

    public static final TimeUnit week = new TimeUnit(Type.week, Type.millisecond, millisecondsPerWeek);

    public static final TimeUnit[] descendingMillisecondBased = {week, day, hour, minute, second, millisecond};

    public static final TimeUnit[] descendingMillisecondBasedForDisplay = {day, hour, minute, second, millisecond};

    public static final TimeUnit month = new TimeUnit(Type.month, Type.month, 1);

    public static final TimeUnit quarter = new TimeUnit(Type.quarter, Type.month, monthsPerQuarter);

    public static final TimeUnit year = new TimeUnit(Type.year, Type.month, monthsPerYear);

    public static final TimeUnit[] descendingMonthBased = {year, quarter, month};

    public static final TimeUnit[] descendingMonthBasedForDisplay = {year, month};

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "UNIT_TYPE"))
    private Type type;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "BASE_UNIT_TYPE"))
    private Type baseType;

    @Column
    private int factor;

    public static TimeUnit of(String name) {
        Map<String, TimeUnit> nameType = new HashMap<>(9);
        nameType.put(millisecond.getForPersistentMapping_Type().name, millisecond);
        nameType.put(second.getForPersistentMapping_Type().name, second);
        nameType.put(minute.getForPersistentMapping_Type().name, minute);
        nameType.put(hour.getForPersistentMapping_Type().name, hour);
        nameType.put(day.getForPersistentMapping_Type().name, day);
        nameType.put(week.getForPersistentMapping_Type().name, week);
        nameType.put(month.getForPersistentMapping_Type().name, month);
        nameType.put(quarter.getForPersistentMapping_Type().name, quarter);
        nameType.put(year.getForPersistentMapping_Type().name, year);
        TimeUnit timeUnit = nameType.get(name);
        if (timeUnit == null) {
            throw new IllegalArgumentException(" Unknown unit name ' " + name + "' ");
        }
        return timeUnit;
    }

    private TimeUnit(Type type, Type baseType, int factor) {
        this.type = type;
        this.baseType = baseType;
        this.factor = factor;
    }

    TimeUnit baseUnit() {
        return baseType.equals(Type.millisecond) ? millisecond : month;
    }

    public boolean isConvertibleToMilliseconds() {
        return isConvertibleTo(millisecond);
    }

    public boolean isConvertibleTo(TimeUnit other) {
        return baseType.equals(other.baseType);
    }

    public int compareTo(Object object) {
        TimeUnit other = (TimeUnit) object;
        if (other.baseType.equals(baseType))
            return factor - other.factor;
        if (baseType.equals(Type.month))
            return 1;
        return -1;
    }

    int javaCalendarConstantForBaseType() {
        if (baseType.equals(Type.millisecond))
            return Calendar.MILLISECOND;
        if (baseType.equals(Type.month))
            return Calendar.MONTH;
        return 0;
    }

    public String toString() {
        return type.name;
    }


    String toString(long quantity) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(quantity);
        buffer.append(" ");
        buffer.append(type.name);
        buffer.append(quantity == 1 ? "" : "s");
        return buffer.toString();
    }

    TimeUnit[] descendingUnits() {
        return isConvertibleToMilliseconds() ? descendingMillisecondBased : descendingMonthBased;
    }

    TimeUnit[] descendingUnitsForDisplay() {
        return isConvertibleToMilliseconds() ? descendingMillisecondBasedForDisplay : descendingMonthBasedForDisplay;
    }

    TimeUnit nextFinerUnit() {
        TimeUnit[] descending = descendingUnits();
        int index = -1;
        for (int i = 0; i < descending.length; i++)
            if (descending[i].equals(this))
                index = i;
        if (index == descending.length - 1)
            return null;
        return descending[index + 1];
    }

    public boolean equals(Object object) {
        //revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
        if (object == null || !(object instanceof TimeUnit))
            return false;
        TimeUnit other = (TimeUnit) object;
        return this.baseType.equals(other.baseType) && this.factor == other.factor && this.type.equals(other.type);
    }

    public int hashCode() {
        return factor + baseType.hashCode() + type.hashCode();
    }

    @SuppressWarnings("JpaObjectClassSignatureInspection")
    @Embeddable
    static class Type implements Serializable {
        static final Type millisecond = new Type("millisecond");

        static final Type second = new Type("second");

        static final Type minute = new Type("minute");

        static final Type hour = new Type("hour");

        static final Type day = new Type("day");

        static final Type week = new Type("week");

        static final Type month = new Type("month");

        static final Type quarter = new Type("quarter");

        static final Type year = new Type("year");

        @Basic
        private String name;


        Type(String name) {
            this.name = name;
        }

        //TODO cache this map
        public static Type of(final String name) {
            Map<String, Type> nameType = new HashMap<>(9);
            nameType.put(millisecond.name, millisecond);
            nameType.put(second.name, second);
            nameType.put(minute.name, minute);
            nameType.put(hour.name, hour);
            nameType.put(week.name, week);
            nameType.put(month.name, month);
            nameType.put(quarter.name, quarter);
            nameType.put(year.name, year);
            return nameType.get(name);

        }

        public boolean equals(Object other) {
            try {
                return equals((Type) other);
            } catch (ClassCastException ex) {
                return false;
            }
        }

        public boolean equals(Type another) {
            return another != null && this.name.equals(another.name);
        }

        public int hashCode() {
            return name.hashCode();
        }

        //Only for use by persistence mapping frameworks
        //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
        Type() {
        }

        private String getForPersistentMapping_Name() {
            return this.name;
        }

        private void setForPersistentMapping_Name(String name) {
            this.name = name;
        }


    }

    int getFactor() {
        return factor;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    protected TimeUnit() {
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Type getForPersistentMapping_BaseType() {
        return baseType;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_BaseType(Type baseType) {
        this.baseType = baseType;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Factor() {
        return factor;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Factor(int factor) {
        this.factor = factor;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Type getForPersistentMapping_Type() {
        return type;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Type(Type type) {
        this.type = type;
    }

    static TimeUnit exampleForPersistentMappingTesting() {
        return second;
    }

    static Type exampleTypeForPersistentMappingTesting() {
        return Type.hour;
    }
}