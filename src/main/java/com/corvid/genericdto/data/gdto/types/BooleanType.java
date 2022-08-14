package com.corvid.genericdto.data.gdto.types;

public class BooleanType extends AbstractType<Boolean> {

    public BooleanType(String regExp) {
        super(regExp);
    }

    public BooleanType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Boolean defaultValue(){
        return false;
    }

    public BooleanType() {
        this(null);
    }

    protected Boolean construct(String content) {
        return content == null ? null : Boolean.parseBoolean(content);
    }

    @Override
    public String toString() {
        return "{" +
                "BooleanType ='" + getValue() + '\'' +
                '}';
    }

    //TODO1 can do better?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BooleanType)) return false;
        BooleanType that = (BooleanType) o;
        if (!t.equals(that.t)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}