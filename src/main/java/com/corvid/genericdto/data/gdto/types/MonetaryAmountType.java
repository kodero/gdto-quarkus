package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.shared.MonetaryAmount;
import com.corvid.genericdto.util.LoggingUtil;

import java.math.BigDecimal;
import java.util.Currency;

import org.jboss.logging.Logger.Level;

/**
 * types for money handling
 */
public class MonetaryAmountType extends AbstractType<MonetaryAmount> {

    public MonetaryAmountType(String regExp) {
        super(regExp);
    }

    public MonetaryAmountType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

  protected MonetaryAmount construct(String content) {
        LoggingUtil.log(MonetaryAmount.class, Level.DEBUG, String.format("Construct monetary amount instance from string [%s]", content));
        if (content == null) return null;
        String[] contentArray = content.split(" ");
        assert contentArray.length == 2;
        final String qty = contentArray[0];
        final String code = contentArray[1];
        final BigDecimal value = new BigDecimal(qty);
        return MonetaryAmount.valueOf(value, Currency.getInstance(code),MonetaryAmount.DEFAULT_ROUNDING_MODE);
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
        if (!(o instanceof MonetaryAmountType)) return false;
        MonetaryAmountType that = (MonetaryAmountType) o;
        if (this.t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}