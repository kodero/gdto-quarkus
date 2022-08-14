package com.corvid.genericdto.shared;

import java.util.Iterator;

abstract public class ImmutableIterator<T> implements Iterator<T> {

    @Override
    public void remove() {
        throw new UnsupportedOperationException("sorry, no can do :-(");
    }
}