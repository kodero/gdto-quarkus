/**
 * Copyright (c) Mar 30, 2012 10:01:40 AM mokua,kodero
 */
package com.corvid.genericdto.shared.time;

import java.util.List;

/**
 * @author 10:01:40 AM mokua,kodero
 *         <p/>
 *         The rules of this interface  are consistent with the common mathematical
 *         definition of "interval". For a simple explanation, see
 *         http://en.wikipedia.org/wiki/Interval_(mathematics)
 *         <p/>
 *         Interval (and its "ConcreteInterval" subclass) can be used for any objects
 *         that have a natural ordering reflected by implementing the Comparable
 *         interface. For example, Integer implements Comparable, so if you want to
 *         check if an Integer is within a range, make an Interval. Any class of yours
 *         which implements Comparable can have intervals defined this way.
 */
public interface IntervalInterface<T extends Comparable<? super T>> {


    public Comparable<? super T> upperLimit();

    public boolean includesUpperLimit();

    public boolean hasUpperLimit();


    public Comparable lowerLimit();


    public boolean includesLowerLimit();


    public boolean hasLowerLimit();

    public IntervalInterface<T> newOfSameType(Comparable lower, boolean isLowerClosed, Comparable upper, boolean isUpperClosed);

    public IntervalInterface<T> emptyOfSameType();

    public boolean includes(Comparable value);

    public boolean covers(IntervalInterface<T> other);

    public boolean isOpen();

    public boolean isClosed();

    public boolean isEmpty();

    public boolean isSingleElement();

    public boolean isBelow(Comparable value);

    public boolean isAbove(Comparable value);

    public int compareTo(Object arg);

    public String toString();

    public boolean equals(Object other);

    public boolean equals(IntervalInterface<T> other);

    public int hashCode();

    public boolean intersects(IntervalInterface<T> other);

    public IntervalInterface<T> intersect(IntervalInterface<T> other);

    public IntervalInterface<T> gap(IntervalInterface<T> other);

    /**
     * see: http://en.wikipedia.org/wiki/Set_theoretic_complement
     */
    public List<T> complementRelativeTo(IntervalInterface<T> other);

}