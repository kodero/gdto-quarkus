package com.corvid.genericdto.data.gdto.visitor;

import com.corvid.genericdto.data.gdto.types.AbstractType;
import com.corvid.genericdto.data.gdto.types.BigDecimalType;
import com.corvid.genericdto.data.gdto.types.BooleanType;

public interface EntityVisitor<T, K> {
    T visit(AbstractType unknown, K args);

    T visit(BigDecimalType unknown, K args);

    T visit(BooleanType source, K args);

}