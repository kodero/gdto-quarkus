package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.time.Duration;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author mokua,kodero
 *         {
 *         "unit": "day",
 *         "quantity": 10
 *         }
 */
public class DurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("unit", value.getForPersistentMapping_Unit().toString());
        jsonGenerator.writeNumberField("quantity", value.getForPersistentMapping_Quantity());
        jsonGenerator.writeEndObject();
    }
}
