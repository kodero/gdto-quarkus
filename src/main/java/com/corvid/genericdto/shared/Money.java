package com.corvid.genericdto.shared;

import com.corvid.genericdto.data.gdto.MoneyDeserializer;
import com.corvid.genericdto.data.gdto.MoneySerializer;
import com.corvid.genericdto.shared.time.Duration;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author mokua,kodero Jan 5, 2012 10:13:49 AM
 */

@Embeddable
@Access(AccessType.PROPERTY)
@JsonDeserialize(using = MoneyDeserializer.class)
@JsonSerialize(using = MoneySerializer.class)
public class Money implements Serializable, Comparable<Money> {
    private static final String DEFAULT_CURRENCY = "KES";

    public static final Currency KES = Currency.getInstance("KES");

    private static final Currency USD = Currency.getInstance("USD");

    private static final Currency EUR = Currency.getInstance("EUR");

    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static Money ZERO = new Money(BigDecimal.ZERO, Currency.getInstance(DEFAULT_CURRENCY));

    private static final long serialVersionUID = 1L;


    // ********************** Business Methods ********************** //


    public static Money fromString(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    //TODO move this method to some service class
    public static Money convert(Money amount, Currency toConcurrency) {
        //TODO lookup table ExchangeRate contains the necessary rates
        //TODO get the exchange rate with (inputCurrency = this.currency, outputCurrency = toCurrency)
        // TODO: This requires some conversion magic and is therefore not implemented
        //inject the ExchangeRate
        return new Money(amount.getValue(), toConcurrency);
    }


    private BigDecimal value;

    private Currency currency;

    /**
     * The constructor does not complex computations and requires simple, inputs
     * consistent with the class invariant. Other creation methods are available
     * for convenience.
     */
    public Money(BigDecimal value, Currency currency) {
        //TODO if (amount.scale() != currency.getDefaultFractionDigits()) throw new IllegalArgumentException("Scale of amount does not match currency");
        this.currency = currency;
        this.value = value;
    }

    /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
    public static Money valueOf(BigDecimal amount, Currency currency) {
        return Money.valueOf(amount, currency, RoundingMode.UNNECESSARY);
    }

    /**
     * For convenience, an amount can be rounded to create a Money.
     */
    public static Money valueOf(BigDecimal rawAmount, Currency currency, RoundingMode roundingMode) {
        BigDecimal amount = rawAmount.setScale(currency.getDefaultFractionDigits(), roundingMode);
        return new Money(amount, currency);
    }

    /**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
    public static Money valueOf(double dblAmount, Currency currency) {
        return Money.valueOf(dblAmount, currency, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Because of the indefinite precision of double, this method must round off
     * the value. This method gives the client control of the rounding mode.
     */
    public static Money valueOf(double dblAmount, Currency currency, RoundingMode roundingMode) {
        BigDecimal rawAmount = BigDecimal.valueOf(dblAmount);
        return Money.valueOf(rawAmount, currency, roundingMode);
    }

    /**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
    public static Money shillings(double amount) {
        return Money.valueOf(amount, KES);
    }

    /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
    public static Money shillings(BigDecimal amount) {
        return Money.valueOf(amount, KES);
    }

    /**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
    public static Money dollars(double amount) {
        return Money.valueOf(amount, USD);
    }

    /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
    public static Money dollars(BigDecimal amount) {
        return Money.valueOf(amount, USD);
    }

    /**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
    public static Money euros(double amount) {
        return Money.valueOf(amount, EUR);
    }

    /**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
    public static Money euros(BigDecimal amount) {
        return Money.valueOf(amount, EUR);
    }

    public static Money sum(Collection<Money> monies) {
        //TODO Return Default Currency
        if (monies.isEmpty())
            return Money.dollars(0.00);
        Iterator<Money> iterator = monies.iterator();
        Money sum = iterator.next();
        while (iterator.hasNext()) {
            Money each = iterator.next();
            sum = sum.plus(each);
        }
        return sum;
    }

    /**
     * How best to handle access to the internals? It is needed for
     * database mapping, UI presentation, and perhaps a few other
     * uses. Yet giving public access invites people to do the
     * real work of the Money object elsewhere.
     * Here is an experimental approach, giving access with a
     * warning label of sorts. Let us know how you like it.
     */
    public BigDecimal breachEncapsulationOfAmount() {
        return value;
    }

    public Currency breachEncapsulationOfCurrency() {
        return currency;
    }


    /**
     * This probably should be Currency responsibility. Even then, it may need
     * to be customized for specialty apps because there are other cases, where
     * the smallest increment is not the smallest unit.
     */
    public Money minimumIncrement() {
        BigDecimal one = new BigDecimal(1);
        BigDecimal increment = one.movePointLeft(currency.getDefaultFractionDigits());
        return Money.valueOf(increment, currency);
    }

    public Money incremented() {
        return this.plus(minimumIncrement());
    }

    boolean hasSameCurrencyAs(Money arg) {
        return currency.equals(arg.currency);
    }

    public Money negated() {
        return Money.valueOf(value.negate(), currency);
    }

    public Money abs() {
        return Money.valueOf(value.abs(), currency);
    }

    @Transient
    public boolean isNegative() {
        return value.compareTo(new BigDecimal(0)) < 0;
    }

    @Transient
    public boolean isPositive() {
        return value.compareTo(new BigDecimal(0)) > 0;
    }

    @Transient
    public boolean isZero() {
        return this.equals(Money.valueOf(0.0, currency));
    }

    public Money plus(Money other) {
        assertHasSameCurrencyAs(other);
        return Money.valueOf(value.add(other.value), currency);
    }

    public Money minus(Money other) {
        return this.plus(other.negated());
    }

    public Money dividedBy(BigDecimal divisor, RoundingMode roundingMode) {
        BigDecimal newAmount = value.divide(divisor, roundingMode);
        return Money.valueOf(newAmount, currency);
    }

    public Money dividedBy(double divisor) {
        return dividedBy(divisor, DEFAULT_ROUNDING_MODE);
    }

    public Money dividedBy(double divisor, RoundingMode roundingMode) {
        return dividedBy(BigDecimal.valueOf(divisor), roundingMode);
    }

    public Ratio dividedBy(Money divisor) {
        assertHasSameCurrencyAs(divisor);
        return Ratio.of(value, divisor.value);
    }

    public Money applying(Ratio ratio, RoundingMode roundingRule) {
        return applying(ratio, currency.getDefaultFractionDigits(), roundingRule);
    }

    public Money applying(Ratio ratio, int scale, RoundingMode roundingRule) {
        BigDecimal newAmount = ratio.times(value).decimalValue(scale, roundingRule);
        return Money.valueOf(newAmount, currency);
    }

    /**
     * TODO: Many apps require carrying extra precision in intermediate
     * calculations. The use of Ratio is a beginning, but need a comprehensive
     * solution. Currently, an invariant of Money is that the scale is the
     * currencies standard scale, but this will probably have to be suspended or
     * elaborated in intermediate calcs, or handled with defered calculations
     * like Ratio.
     */

    public Money times(BigDecimal factor) {
        return times(factor, DEFAULT_ROUNDING_MODE);
    }

    /**
     * TODO: BigDecimal.multiply() scale is sum of scales of two multiplied
     * numbers. So what is scale of times?
     */
    public Money times(BigDecimal factor, RoundingMode roundingMode) {
        return Money.valueOf(value.multiply(factor), currency, roundingMode);
    }

    public Money times(double amount, RoundingMode roundingMode) {
        return times(BigDecimal.valueOf(amount), roundingMode);
    }

    public Money times(double amount) {
        return times(BigDecimal.valueOf(amount));
    }

    public Money times(int i) {
        return times(new BigDecimal(i));
    }


    public int compareTo(Money other) {
        if (!hasSameCurrencyAs(other))
            throw new IllegalArgumentException("Compare is not defined between different currencies");
        return value.compareTo(other.value);
    }

    public boolean isGreaterThan(Money other) {
        return (compareTo(other) > 0);
    }

    public boolean isLessThan(Money other) {
        return (compareTo(other) < 0);
    }

    public boolean equals(Object other) {
        try {
            return equals((Money) other);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    public boolean equals(Money other) {
        return
                other != null &&
                        hasSameCurrencyAs(other) &&
                        value.equals(other.value);
    }

    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        return (currency == null? "-" : currency.getSymbol()) + " " + value;
    }

    public String toString(Locale locale) {
        return currency.getSymbol(locale) + " " + value;
    }

    public MoneyTimeRate per(Duration duration) {
        return new MoneyTimeRate(this, duration);
    }

//  TODO: Provide some currency-dependent formatting. Java 1.4 Currency doesn't
//  do it.
//  public String formatString() {
//      return currency.formatString(amount());
//  }
//  public String localString() {
//      return currency.getFormat().format(amount());
//  }

    //@Transient
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    //@Transient
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }

    private void assertHasSameCurrencyAs(Money aMoney) {
        if (!hasSameCurrencyAs(aMoney))
            throw new IllegalArgumentException(aMoney.toString() + " is not same currency as " + this.toString());
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    public Money() {
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    //@Column(name = "AMOUNT")
    /*
    private BigDecimal getForPersistentMapping_Amount() {
        return amount;
    }


    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Amount(BigDecimal amount) {
        this.amount = amount;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    //@Column(name = "CURRENCY")
    private Currency getForPersistentMapping_Currency() {
        return currency;
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Currency(Currency currency) {
        this.currency = currency;
    }
    */
}