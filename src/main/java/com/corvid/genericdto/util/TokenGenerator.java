package com.corvid.genericdto.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class TokenGenerator {

    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public static String nextToken() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}