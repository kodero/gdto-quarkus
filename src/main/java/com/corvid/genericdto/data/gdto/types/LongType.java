package com.corvid.genericdto.data.gdto.types;

public class LongType extends AbstractType<Long> {

    public LongType(String regExp) {
        super(regExp);
    }

    public LongType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public Long defaultValue(){
        return 0L;
    }

    public LongType() {
        this(null);
    }

    protected Long construct(String content) {
        return content == null ? null : Long.parseLong(content);
    }

    @Override
    public String toString() {
        return "{" +
                "LongType ='" + getValue() + '\'' +
                '}';
    }

    //TODO1 can do better?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongType)) return false;
        LongType that = (LongType) o;
        if (!t.equals(that.t)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}