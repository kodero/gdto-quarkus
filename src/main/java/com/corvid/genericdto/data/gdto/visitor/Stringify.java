package com.corvid.genericdto.data.gdto.visitor;

import com.corvid.genericdto.data.gdto.types.AbstractType;
import com.corvid.genericdto.data.gdto.types.BigDecimalType;
import com.corvid.genericdto.data.gdto.types.BooleanType;

class Stringify implements EntityVisitor<String, Integer> {


    public String visit(AbstractType unknown, Integer args) {
        throw new IllegalStateException("unknown: " + unknown + " args:" + args);
    }

    public String visit(BigDecimalType source, Integer args) {
        return "Big Decimal:" + source.getValue() + " with " + args;
    }

    @Override
    public String visit(BooleanType source, Integer args) {
        return "Boolean :" + source.getValue() + " with " + args;
    }

    public static void main(String[] args) {
        AbstractType a = new BigDecimalType(null, "19.8");
        AbstractType b = new BooleanType("", "true");
        System.out.println(new Stringify().visit(a, 3));
        System.out.println(new Stringify().visit(b, 87));
    }
}