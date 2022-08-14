package com.corvid.genericdto.data.gdto.types;

public class CharacterType extends AbstractType<Character> {
    public CharacterType() {
        this(null);
    }

    public CharacterType(String regExp, String contentAsString) {
        this(regExp);
        this.instantiateFromString(contentAsString);
    }

    public CharacterType(String regExp) {
        super(regExp);
    }

    protected Character construct(String content) {
        return content.charAt(0);
    }

    @Override
    public Character defaultValue(){
        return '-';
    }

    @Override
    public String toString() {
        return "{" +
                "CharacterType ='" + getValue() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterType)) return false;
        CharacterType that = (CharacterType) o;
        if (t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}