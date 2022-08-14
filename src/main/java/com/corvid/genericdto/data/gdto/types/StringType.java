package com.corvid.genericdto.data.gdto.types;

public class StringType extends AbstractType<String> {
    public StringType() {
        this(null);
    }

    public StringType(String regExp, String contentAsString) {
        this(regExp);
        this.instantiateFromString(contentAsString);
    }

    @Override
    public String defaultValue(){
        return "";
    }

    public StringType(String regExp) {
        super(regExp);
    }

    protected String construct(String content) {
        return content;
    }

    @Override
    public String toString() {
        return "{" +
                "StringType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringType)) return false;
        StringType that = (StringType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}