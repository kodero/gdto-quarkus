
package com.corvid.genericdto.data.gdto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Adam Bien, www.adam-bien.com
 */

public class CompositeValidationException extends Exception {
    private Map<String, ValidationException> validationExceptions;

    private String gdtoName = null;

    public CompositeValidationException(String gdtoName) {
        super("ValidationError in: " + gdtoName);
        this.validationExceptions = new HashMap<>();
        this.gdtoName = gdtoName;
    }


    public void add(String attributeName, ValidationException validationException) {
        this.validationExceptions.put(attributeName, validationException);
    }

    public Map<String, ValidationException> getValidationExceptions() {
        return this.validationExceptions;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GDTO Name: ").append(this.gdtoName);
        Set<Map.Entry<String, ValidationException>> exceptions = this.validationExceptions.entrySet();
        Iterator<Map.Entry<String, ValidationException>> exceptionIterator = exceptions.iterator();
        while (exceptionIterator.hasNext()) {
            Map.Entry<String, ValidationException> entry = exceptionIterator.next();
            result.append("Name: ").append(entry.getKey()).append(" Message: ").append(entry.getValue().getMessage());
        }
        return result.toString();
    }

    public boolean isEmpty() {
        return this.validationExceptions.isEmpty();
    }
}
