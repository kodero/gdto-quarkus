package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.time.CalendarDate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author mokua,kodero
 *         {
 *         <p/>
 *         yyy-MM-dd
 *         }
 */
public class CalendarDateSerializer extends JsonSerializer<CalendarDate> {

    @Override
    public void serialize(CalendarDate value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(value.toString());
    }
}
