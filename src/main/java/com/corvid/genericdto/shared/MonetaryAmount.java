package com.corvid.genericdto.shared;

import com.corvid.genericdto.data.gdto.MonetaryAmountDeserializer;
import com.corvid.genericdto.data.gdto.MonetaryAmountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;


/* 
   This value-typed class should be <code>java.io.Serializable</code>: When Hibernate stores entity
   instance data in the shared second-level cache (see <a href="#Caching"/>), it <em>disassembles</em>
   the entity's state. If an entity has a <code>MonetaryAmount</code> property, the serialized
   representation of the property value will be stored in the second-level cache region. When entity
   data is retrieved from the cache region, the property value will be deserialized and reassembled.
 */

@JsonDeserialize(using = MonetaryAmountDeserializer.class)
@JsonSerialize(using = MonetaryAmountSerializer.class)
public class MonetaryAmount implements Serializable {
  private static final String DEFAULT_CURRENCY = "KES";

  public static final Currency KES = Currency.getInstance("KES");

  public static final Currency USD = Currency.getInstance("USD");

  private static final Currency EUR = Currency.getInstance("EUR");

  public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
  public static final String SPACE = " ";

  public static MonetaryAmount ZERO =
      new MonetaryAmount(BigDecimal.ZERO, Currency.getInstance(DEFAULT_CURRENCY));

  /*
The class does not need a special constructor, you can make it immutable, even with
      <code>final</code> fields, as your code will be the only place an instance is created.
   */
  protected final BigDecimal value;
  protected final Currency currency;

  public MonetaryAmount() {
    this.value = BigDecimal.ZERO;
    this.currency = Currency.getInstance(DEFAULT_CURRENCY);
  }

  public MonetaryAmount(BigDecimal value, Currency currency) {
    this.value = value;
    this.currency = currency;
  }

  public BigDecimal getValue() {
    return value;
  }

  public Currency getCurrency() {
    return currency;
  }

  /*
You should implement the <code>equals()</code> and <code>hashCode()</code>
      methods, and compare monetary amounts "by value".
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MonetaryAmount)) return false;

    final MonetaryAmount monetaryAmount = (MonetaryAmount) o;

    if (!value.equals(monetaryAmount.value)) return false;
    if (!currency.equals(monetaryAmount.currency)) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = value.hashCode();
    result = 29 * result + currency.hashCode();
    return result;
  }

  /*
You will need a <code>String</code> representation of a monetary
      amount. Implement the <code>toString()</code> method and a static method to
      create an instance from a <code>String</code>.
   */
  public String toString() {
    return getValue() + SPACE + getCurrency();
  }

  public static MonetaryAmount fromString(String s) {
    String[] split = s.split(" ");
    return new MonetaryAmount(
        new BigDecimal(split[0]),
        Currency.getInstance(split[1])
    );
  }

  // ********************** Business Methods ********************** //

  public static MonetaryAmount fromString(String amount, String currencyCode) {
    return new MonetaryAmount(new BigDecimal(amount), Currency.getInstance(currencyCode));
  }

  //TODO move this method to some service class
  public static MonetaryAmount convert(MonetaryAmount amount, Currency toConcurrency, BigDecimal rate) {
    //TODO lookup table ExchangeRate contains the necessary rates
    //TODO get the exchange rate with (inputCurrency = this.currency, outputCurrency = toCurrency)
    // TODO: This requires some conversion magic and is therefore not implemented
    //inject the ExchangeRate
    return new MonetaryAmount(amount.getValue().divide(rate), toConcurrency);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not round off the amount.
   */
  public static MonetaryAmount valueOf(BigDecimal amount, Currency currency) {
    return MonetaryAmount.valueOf(amount, currency, RoundingMode.UNNECESSARY);
  }

  /**
   * For convenience, an amount can be rounded to create a MonetaryAmount.
   */
  public static MonetaryAmount valueOf(BigDecimal rawAmount, Currency currency, RoundingMode roundingMode) {
    BigDecimal amount = rawAmount.setScale(currency.getDefaultFractionDigits(), roundingMode);
    return new MonetaryAmount(amount, currency);
  }

  /**
   * WARNING: Because of the indefinite precision of double, this method must round off the value.
   */
  public static MonetaryAmount valueOf(double dblAmount, Currency currency) {
    return MonetaryAmount.valueOf(dblAmount, currency, DEFAULT_ROUNDING_MODE);
  }

  /**
   * Because of the indefinite precision of double, this method must round off the value. This
   * method gives the client control of the rounding mode.
   */
  public static MonetaryAmount valueOf(double dblAmount, Currency currency, RoundingMode roundingMode) {
    BigDecimal rawAmount = BigDecimal.valueOf(dblAmount);
    return MonetaryAmount.valueOf(rawAmount, currency, roundingMode);
  }

  /**
   * WARNING: Because of the indefinite precision of double, this method must round off the value.
   */
  public static MonetaryAmount shillings(double amount) {
    return MonetaryAmount.valueOf(amount, KES);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not round off the amount.
   */
  public static MonetaryAmount shillings(BigDecimal amount) {
    return MonetaryAmount.valueOf(amount, KES);
  }

  /**
   * WARNING: Because of the indefinite precision of double, this method must round off the value.
   */
  public static MonetaryAmount dollars(double amount) {
    return MonetaryAmount.valueOf(amount, USD);
  }

  public static MonetaryAmount identity(Currency currency) {
    return MonetaryAmount.valueOf(BigDecimal.ZERO, currency);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not round off the amount.
   */
  public static MonetaryAmount dollars(BigDecimal amount) {
    return MonetaryAmount.valueOf(amount, USD);
  }

  /**
   * WARNING: Because of the indefinite precision of double, this method must round off the value.
   */
  public static MonetaryAmount euros(double amount) {
    return MonetaryAmount.valueOf(amount, EUR);
  }

  /**
   * This creation method is safe to use. It will adjust scale, but will not round off the amount.
   */
  public static MonetaryAmount euros(BigDecimal amount) {
    return MonetaryAmount.valueOf(amount, EUR);
  }

  public static MonetaryAmount sum(Collection<MonetaryAmount> monies) {
    if (monies.isEmpty()) {
      throw new IllegalArgumentException("Empty list ...");
    }

    return monies.stream().reduce(MonetaryAmount::plus).get();
  }

  /**
   * This probably should be Currency responsibility. Even then, it may need to be customized for
   * specialty apps because there are other cases, where the smallest increment is not the smallest
   * unit.
   */
  public MonetaryAmount minimumIncrement() {
    BigDecimal one = new BigDecimal(1);
    BigDecimal increment = one.movePointLeft(currency.getDefaultFractionDigits());
    return MonetaryAmount.valueOf(increment, currency);
  }

  public MonetaryAmount incremented() {
    return this.plus(minimumIncrement());
  }

  boolean hasSameCurrencyAs(MonetaryAmount arg) {
    return currency.equals(arg.currency);
  }

  public MonetaryAmount negated() {
    return MonetaryAmount.valueOf(value.negate(), currency);
  }

  public MonetaryAmount abs() {
    return MonetaryAmount.valueOf(value.abs(), currency);
  }


  public boolean isNegative() {
    return value.compareTo(new BigDecimal(0)) < 0;
  }

  public boolean isPositive() {
    return value.compareTo(new BigDecimal(0)) > 0;
  }

  /**
   * -1, 0, or 1 as the value of this BigDecimal is negative, zero, or positive.
   * @see BigDecimal#signum()
   * @return
   */
  public int signum() {  return getValue().signum(); }

  public boolean isZero() {
    return (this.value.compareTo(BigDecimal.ZERO) == 0);
  }

  public MonetaryAmount plus(MonetaryAmount other) {
    assertHasSameCurrencyAs(other);
    return MonetaryAmount.valueOf(value.add(other.value), currency);
  }

  public MonetaryAmount minus(MonetaryAmount other) {
    return this.plus(other.negated());
  }

  public MonetaryAmount dividedBy(BigDecimal divisor, RoundingMode roundingMode) {
    BigDecimal newAmount = value.divide(divisor, roundingMode);
    return MonetaryAmount.valueOf(newAmount, currency);
  }

  public MonetaryAmount dividedBy(double divisor) {
    return dividedBy(divisor, DEFAULT_ROUNDING_MODE);
  }

  public MonetaryAmount dividedBy(double divisor, RoundingMode roundingMode) {
    return dividedBy(BigDecimal.valueOf(divisor), roundingMode);
  }

  public Ratio dividedBy(MonetaryAmount divisor) {
    assertHasSameCurrencyAs(divisor);
    return Ratio.of(value, divisor.getValue());
  }

  public MonetaryAmount applying(Ratio ratio, RoundingMode roundingRule) {
    return applying(ratio, currency.getDefaultFractionDigits(), roundingRule);
  }

  public MonetaryAmount applying(Ratio ratio, int scale, RoundingMode roundingRule) {
    BigDecimal newAmount = ratio.times(value).decimalValue(scale, roundingRule);
    return MonetaryAmount.valueOf(newAmount, currency);
  }

  /**
   * TODO: Many apps require carrying extra precision in intermediate calculations. The use of Ratio
   * is a beginning, but need a comprehensive solution. Currently, an invariant of MonetaryAmount is
   * that the scale is the currencies standard scale, but this will probably have to be suspended or
   * elaborated in intermediate calcs, or handled with defered calculations like Ratio.
   */

  public MonetaryAmount times(BigDecimal factor) {
    return times(factor, DEFAULT_ROUNDING_MODE);
  }

  /**
   * TODO1: BigDecimal.multiply() scale is sum of scales of two multiplied numbers. So what is scale
   * of times?
   */
  public MonetaryAmount times(BigDecimal factor, RoundingMode roundingMode) {
    return MonetaryAmount.valueOf(value.multiply(factor), currency, roundingMode);
  }

  public MonetaryAmount times(double value, RoundingMode roundingMode) {
    return times(BigDecimal.valueOf(value), roundingMode);
  }

  public MonetaryAmount times(double value) {
    return times(BigDecimal.valueOf(value));
  }

  public MonetaryAmount times(int i) {
    return times(new BigDecimal(i));
  }

  public int compareTo(MonetaryAmount other) {
    if (!hasSameCurrencyAs(other)) {
      throw new IllegalArgumentException("Compare is not defined between different currencies");
    }
    return value.compareTo(other.value);
  }

  public boolean isGreaterThan(MonetaryAmount other) {
    return (compareTo(other) > 0);
  }

  public boolean isLessThan(MonetaryAmount other) {
    return (compareTo(other) < 0);
  }

  public String toString(Locale locale) {
    return currency.getSymbol(locale) + " " + value;
  }

  private void assertHasSameCurrencyAs(MonetaryAmount aMoney) {
    if (!hasSameCurrencyAs(aMoney)) {
      throw new IllegalArgumentException(
          aMoney.toString() + " is not same currency as " + this.toString());
    }
  }
}

