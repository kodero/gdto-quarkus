package com.corvid.genericdto.data.gdto.types;

public class IntType extends AbstractType<Integer> {

    public IntType(String regExp) {
        super(regExp);
    }

    public IntType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Integer defaultValue(){
        return 0;
    }

    public IntType() {
        this(null);
    }

    protected Integer construct(String content) {
        return content == null ? null : Integer.parseInt(content);
    }

    @Override
    public String toString() {
        return "{" +
                "IntType ='" + getValue() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntType)) return false;
        IntType that = (IntType) o;
        if (!t.equals(that.t)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}