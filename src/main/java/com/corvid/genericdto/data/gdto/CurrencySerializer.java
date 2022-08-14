package com.corvid.genericdto.data.gdto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Currency;

/**
 * Created by mokua,kodero on 10/5/17.
 *  {
 *         <p/>
 *         "code": "KES",
 *         "name": "Kenyan Shilling"
 *         "symbol": "KES
 *         }
 */
public class CurrencySerializer extends JsonSerializer<Currency> {
  @Override public void serialize(Currency value, JsonGenerator jsonGenerator, SerializerProvider provider)
      throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("code", value.getCurrencyCode());
    jsonGenerator.writeStringField("name", value.getDisplayName());
    jsonGenerator.writeStringField("symbol", value.getSymbol());
    jsonGenerator.writeEndObject();

  }
}
