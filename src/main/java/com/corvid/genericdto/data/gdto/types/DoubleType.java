package com.corvid.genericdto.data.gdto.types;

public class DoubleType extends AbstractType<Double> {
    public DoubleType() {
        this(null);
    }

    public DoubleType(String regExp) {
        super(regExp);
    }

    public DoubleType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Double defaultValue(){
        return 0.0;
    }

    protected Double construct(String content) {
        return content == null ? null : Double.parseDouble(content);
    }

    @Override
    public String toString() {
        return "{" +
                "DoubleType ='" + getValue() + '\'' +
                '}';
    }

    //TODO1 can do better?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleType)) return false;
        DoubleType that = (DoubleType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}