/**
 * Copyright (c) Apr 3, 2012 11:08:31 AM mokua,kodero
 */
package com.corvid.genericdto.shared;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 11:08:31 AM mokua,kodero
 */
@Embeddable
public class Ratio {
    private BigDecimal numerator;

    private BigDecimal denominator;

    public static Ratio of(BigDecimal numerator, BigDecimal denominator) {
        return new Ratio(numerator, denominator);
    }

    public static Ratio of(long numerator, long denominator) {
        return new Ratio(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }

    public static Ratio of(BigDecimal fractional) {
        return new Ratio(fractional, BigDecimal.valueOf(1));
    }

    public Ratio(BigDecimal numerator, BigDecimal denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public BigDecimal decimalValue(int scale, RoundingMode roundingRule) {
        return numerator.divide(denominator, scale, roundingRule);
    }

    public boolean equals(Object anObject) {
        try {
            return equals((Ratio) anObject);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    public boolean equals(Ratio other) {
        return
                other != null &&
                        this.numerator.equals(other.numerator) && this.denominator.equals(other.denominator);
    }

    public int hashCode() {
        return numerator.hashCode();
    }

    public Ratio times(BigDecimal multiplier) {
        return Ratio.of(numerator.multiply(multiplier), denominator);
    }

    public Ratio times(Ratio multiplier) {
        return Ratio.of(numerator.multiply(multiplier.numerator), denominator.multiply(multiplier.denominator));
    }

    public String toString() {
        return numerator.toString() + "/" + denominator;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    public Ratio() {
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private BigDecimal getForPersistentMapping_Denominator() {
        return denominator;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Denominator(BigDecimal denominator) {
        this.denominator = denominator;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private BigDecimal getForPersistentMapping_Numerator() {
        return numerator;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Numerator(BigDecimal numerator) {
        this.numerator = numerator;
    }


}