package com.corvid.genericdto.data.gdto.types;
import com.corvid.genericdto.data.gdto.Attribute;
import com.corvid.genericdto.data.gdto.ValidationException;
import com.corvid.genericdto.data.gdto.visitor.EntityVisitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adam Bien, www.adambien.com
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractType<T> implements Attribute {

    protected T t = null;

    protected String regExp;

    private String contentAsString = null;

    private boolean id = false;

    public T defaultValue(){
       return  t;
    }

    protected AbstractType() {

    }

    public AbstractType(String regExp) {
        this.regExp = regExp;
    }

    public void instantiateFromString(String content) {
        this.contentAsString = content;
        t = construct(content);
    }

    protected abstract T construct(String content);


    public void validate() throws ValidationException {
        if (this.regExp != null) {
            Pattern p = Pattern.compile(this.regExp);
            Matcher m = p.matcher(this.contentAsString);
            boolean valid = m.matches();
            if (!valid) {
                throw new ValidationException(this.regExp, this.contentAsString);
            }
        }

    }

    public void setRegexp(String regExp) {
        this.regExp = regExp;
    }


    public T getValue() {
        return t;
    }

    public boolean isId() {
        return this.id;
    }

    public void setId() {
        this.id = true;
    }

    /**
     * visitor
     *
     * @param visitor
     * @param args
     * @param <T>
     * @param <K>
     * @return
     */
    public <T, K> T accept(EntityVisitor<T, K> visitor, K args) {
        return visitor.visit(this, args);
    }
}