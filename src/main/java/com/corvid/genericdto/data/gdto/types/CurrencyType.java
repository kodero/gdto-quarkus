package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.data.gdto.CurrencyDeserializer;
import com.corvid.genericdto.data.gdto.CurrencySerializer;
import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Currency;

import org.jboss.logging.Logger.Level;

/**
 * Created by mokua,kodero on 10/5/17.
 */

@JsonDeserialize(using = CurrencyDeserializer.class)
@JsonSerialize(using = CurrencySerializer.class)
public class CurrencyType extends AbstractType<Currency>{
  private Currency currency;

  public CurrencyType() {

  }

  public CurrencyType(String contentAsString) {
    LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("Creating currency type [%s]", contentAsString));
    this.instantiateFromString(contentAsString);
  }

  @Override protected Currency construct(String content) {
    LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("construct curreny type, code [%s]", content));
    return  Currency.getInstance(content);
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override public String toString() {
    return "CurrencyType{" +
        "currency=" + getValue().getCurrencyCode()+
        '}';
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CurrencyType)) return false;

    CurrencyType that = (CurrencyType) o;

    return getCurrency() != null ? getCurrency().equals(that.getCurrency())
        : that.getCurrency() == null;
  }

  @Override public int hashCode() {
    return getCurrency() != null ? getCurrency().hashCode() : 0;
  }
}
