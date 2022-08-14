
package com.corvid.genericdto.data.gdto;

/**
 * @author Adam Bien, www.adam-bien.com
 */

public class Assert {

    private Assert(){}

    public static void notNull(Object actual, String message) {
        if (actual == null)
            throw new IllegalStateException(message);
    }
}
