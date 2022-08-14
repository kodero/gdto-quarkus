package com.corvid.genericdto.util.mirror;

public interface TypeExtractor {

    Class<?>[] extract(Mirror<?> mirror);

}