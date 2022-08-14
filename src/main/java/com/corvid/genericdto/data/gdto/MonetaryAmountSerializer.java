package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.MonetaryAmount;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * @author mokua,kodero
 *         {
 *         <p/>
 *         "currency": "KES",
 *         "value": 4564
 *         }
 */
public class MonetaryAmountSerializer extends JsonSerializer<MonetaryAmount> {

    @Override
    public void serialize(MonetaryAmount value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("currency", value.getCurrency().getCurrencyCode());
        jsonGenerator.writeNumberField("value", value.getValue());
        jsonGenerator.writeEndObject();
    }
}
