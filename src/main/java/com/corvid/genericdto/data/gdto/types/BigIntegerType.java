package com.corvid.genericdto.data.gdto.types;

import java.math.BigInteger;

public class BigIntegerType extends AbstractType<BigInteger> {

    public BigIntegerType(String regExp) {
        super(regExp);
    }

    public BigIntegerType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public BigInteger defaultValue(){
        return BigInteger.ZERO;
    }

    public BigIntegerType() {
        this(null);
    }

    protected BigInteger construct(String content) {
        return content == null ? null : new BigInteger(content);
    }

    @Override
    public String toString() {
        return "{" +
                "BigInteger ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BigIntegerType)) return false;
        BigIntegerType that = (BigIntegerType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}