package com.corvid.genericdto.util.mirror;

@SuppressWarnings("serial")
public class FailToSetValueException extends RuntimeException {

    public FailToSetValueException(String message, Throwable e) {
        super(message, e);
    }

}