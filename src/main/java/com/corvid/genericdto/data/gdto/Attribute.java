
package com.corvid.genericdto.data.gdto;

import java.io.Serializable;

/**
 * @author adam-bien.com
 */


public interface Attribute<T> extends Serializable {
    public void validate() throws ValidationException;

    public void instantiateFromString(String content);

    public void setRegexp(String regExp);

    public void setId();

    public T getValue();

    public boolean isId();

}
