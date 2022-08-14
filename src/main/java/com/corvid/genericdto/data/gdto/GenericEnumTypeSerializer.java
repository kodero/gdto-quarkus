package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.data.gdto.types.GenericEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.WordUtils;

import java.io.IOException;

/**
 * Created by mokua,kodero on 3/1/16.
 */
public class GenericEnumTypeSerializer extends JsonSerializer<GenericEnum>{

    @Override
    public void serialize(GenericEnum genericEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("key", genericEnum.getEnumKey());
        jsonGenerator.writeStringField("name", WordUtils.capitalizeFully(genericEnum.getEnumName().replace("_", " ")));
        jsonGenerator.writeEndObject();
    }
}
