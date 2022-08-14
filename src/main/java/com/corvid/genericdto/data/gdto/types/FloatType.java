package com.corvid.genericdto.data.gdto.types;

public class FloatType extends AbstractType<Float> {
    public FloatType() {
        this(null);
    }

    public FloatType(String regExp) {
        super(regExp);
    }

    public FloatType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Float defaultValue(){
        return Float.valueOf("0.0");
    }

    protected Float construct(String content) {
        return content == null ? null : Float.parseFloat(content);
    }

    @Override
    public String toString() {
        return "{" + "FloatType ='" + getValue() + '\'' +  '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatType)) return false;
        FloatType that = (FloatType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }


}