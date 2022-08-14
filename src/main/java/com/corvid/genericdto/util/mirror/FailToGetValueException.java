package com.corvid.genericdto.util.mirror;

@SuppressWarnings("serial")
public class FailToGetValueException extends RuntimeException {

    public FailToGetValueException(String message, Throwable e) {
        super(message, e);
    }

}