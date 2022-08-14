package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.shared.Money;
import com.corvid.genericdto.util.LoggingUtil;

import java.math.BigDecimal;
import java.util.Currency;

import org.jboss.logging.Logger.Level;

public class MoneyType extends AbstractType<Money> {

    public MoneyType(String regExp) {
        super(regExp);
    }

    public MoneyType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    public MoneyType() {
        this(null);
    }

    protected Money construct(String content) {
        LoggingUtil.log(MoneyType.class, Level.DEBUG, String.format("Constructing money instance from string [%s]", content));
        if (content == null) return null;
        String[] contentArray = content.split(" ");
        assert contentArray.length == 2;
        if(contentArray[0].equalsIgnoreCase("-")) return null;
        return Money.valueOf(new BigDecimal(contentArray[1]), Currency.getInstance(contentArray[0]));
    }

    @Override
    public String toString() {
        return "{" +
                "MoneyType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyType)) return false;
        MoneyType that = (MoneyType) o;
        if (this.t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}