package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.data.gdto.visitor.EntityVisitor;

import java.math.BigDecimal;

public class BigDecimalType extends AbstractType<BigDecimal> {

    public BigDecimalType(String regExp) {
        super(regExp);
    }

    public BigDecimalType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    public BigDecimal defaultValue(){
        return BigDecimal.ZERO;
    }

    public BigDecimalType() {
        this(null);
    }

    protected BigDecimal construct(String content) {
        return content == null ? null : new BigDecimal(content);
    }

    @Override
    public String toString() {
        return "{" +
                "BigDecimalType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BigDecimalType)) return false;
        BigDecimalType that = (BigDecimalType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }


    @Override
    public <T, K> T accept(EntityVisitor<T, K> visitor, K args) {
        return visitor.visit(this, args);
    }


}