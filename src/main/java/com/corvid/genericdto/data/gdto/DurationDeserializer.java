package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.shared.time.Duration;
import com.corvid.genericdto.shared.time.TimeUnit;
import com.corvid.genericdto.util.LoggingUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger.Level;

/**
 * @author mokua,kodero
 *         {
 *         "unit": "day",
 *         "quantity": 10
 *         }
 */
public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("All fields for duration [%s]", fields));
        Map.Entry<String, JsonNode> next = fields.next();
        String unitCode = next.getValue().textValue();
        final TimeUnit timeUnit = TimeUnit.of(unitCode);
        //get next
        next = fields.next();
        final long value = next.getValue().longValue();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("Unit code [%s], [%s], [%s]", timeUnit, value, value));

        return Duration.of(value, timeUnit);
    }
}
