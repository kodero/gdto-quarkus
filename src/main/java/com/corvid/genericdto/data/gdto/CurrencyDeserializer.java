package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Currency;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger.Level;

/**
 * @author mokua,kodero {
 * {"code": "KES", "name": "Kenyan Shilling" "symbol": "KES }
 */
public class CurrencyDeserializer extends JsonDeserializer<Currency> {

  @Override
  public Currency deserialize(JsonParser jsonParser, DeserializationContext ctxt)
      throws IOException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);
    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
    LoggingUtil.log(CurrencyDeserializer.class, Level.DEBUG, String.format("All fields for currency [%s]", fields));
    
    Map.Entry<String, JsonNode> next = fields.next();
    String currencyCode = next.getValue().textValue();
    LoggingUtil.log(CurrencyDeserializer.class, Level.DEBUG, "Currency code ' " + currencyCode);
    return Currency.getInstance(currencyCode);
  }
}
