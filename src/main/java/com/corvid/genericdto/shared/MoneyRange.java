/**
 * Copyright (c) Apr 3, 2012 3:13:10 PM mokua,kodero
 */
package com.corvid.genericdto.shared;


import javax.persistence.*;

/**
 * @author 3:13:10 PM mokua,kodero
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class MoneyRange extends Range<Money> {

    public MoneyRange() {
        super(Money.ZERO, Money.ZERO);
    }

    /**
     * @param lowerBound
     * @param upperBound
     * @throws IllegalArgumentException
     */
    public MoneyRange(Money lowerBound, Money upperBound) throws IllegalArgumentException {
        super(lowerBound, upperBound);

    }


    @AttributeOverrides({
            @AttributeOverride(name = "forPersistentMapping_Amount",
                    column = @Column(name = "UPPER_BOUND_AMOUNT")),
            @AttributeOverride(name = "forPersistentMapping_Currency",
                    column = @Column(name = "UPPER_BOUND_CURRENCY"))})
    public Money getUpperBound() {
        return upperBound;
    }


    @AttributeOverrides({
            @AttributeOverride(name = "forPersistentMapping_Amount",
                    column = @Column(name = "LOWER_BOUND_AMOUNT")),
            @AttributeOverride(name = "forPersistentMapping_Currency",
                    column = @Column(name = "LOWER_BOUND_CURRENCY"))})
    public Money getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Money lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(Money upperBound) {
        this.upperBound = upperBound;
    }
}
