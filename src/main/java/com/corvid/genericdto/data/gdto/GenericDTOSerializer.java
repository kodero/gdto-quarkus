package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.data.gdto.types.BigDecimalType;
import com.corvid.genericdto.data.gdto.types.BigIntegerType;
import com.corvid.genericdto.data.gdto.types.BooleanType;
import com.corvid.genericdto.data.gdto.types.CalendarDateType;
import com.corvid.genericdto.data.gdto.types.CurrencyType;
import com.corvid.genericdto.data.gdto.types.DateType;
import com.corvid.genericdto.data.gdto.types.DoubleType;
import com.corvid.genericdto.data.gdto.types.DurationType;
import com.corvid.genericdto.data.gdto.types.EnumType;
import com.corvid.genericdto.data.gdto.types.FloatType;
import com.corvid.genericdto.data.gdto.types.IntType;
import com.corvid.genericdto.data.gdto.types.LocalTimeType;
import com.corvid.genericdto.data.gdto.types.LongType;
import com.corvid.genericdto.data.gdto.types.MonetaryAmountType;
import com.corvid.genericdto.data.gdto.types.MoneyType;
import com.corvid.genericdto.data.gdto.types.StringType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class GenericDTOSerializer extends JsonSerializer<GenericDTO> {

  @Override
  public void serialize(GenericDTO value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("name", value.getName());
    //attributes
    jsonGenerator.writeObjectFieldStart("attributes");
    Set<Map.Entry<String, Attribute>> allAttributes = value.attributeMap().entrySet();
    //System.out.println(" the attributes to serialize " + allAttributes);
    for (Map.Entry<String, Attribute> entry : allAttributes) {
      Attribute next = entry.getValue();
      final String fieldName = entry.getKey();
      if (next == null || next.getValue() == null) {
        jsonGenerator.writeNullField(fieldName);
      } else {
        if (next instanceof DurationType) {
          DurationType durationType = (DurationType) next;
          jsonGenerator.writeObjectField(entry.getKey(), durationType.getValue());
        } else if (next instanceof MonetaryAmountType) {
          MonetaryAmountType monetaryAmountType = (MonetaryAmountType) next;
          jsonGenerator.writeObjectField(entry.getKey(), monetaryAmountType.getValue());
        } else if (next instanceof CurrencyType) {
          CurrencyType currencyType = (CurrencyType) next;
          jsonGenerator.writeObjectField(entry.getKey(), currencyType.getValue());
        } else if (next instanceof CalendarDateType) {
          CalendarDateType calendarDateType = (CalendarDateType) next;
          jsonGenerator.writeObjectField(entry.getKey(), calendarDateType.getValue());
        } else if (next instanceof MoneyType) {
          MoneyType moneyType = (MoneyType) next;
          jsonGenerator.writeObjectField(entry.getKey(), moneyType.getValue());
        } else if (next instanceof StringType) {
          StringType stringType = (StringType) next;
          jsonGenerator.writeStringField(fieldName, stringType.getValue());
        } else if (next instanceof LongType) {
          LongType longType = (LongType) next;
          jsonGenerator.writeNumberField(fieldName, longType.getValue());
        } else if (next instanceof IntType) {
          IntType intType = (IntType) next;
          jsonGenerator.writeNumberField(fieldName, intType.getValue());
        } else if (next instanceof FloatType) {
          FloatType floatType = (FloatType) next;
          jsonGenerator.writeNumberField(fieldName, floatType.getValue());
        } else if (next instanceof DoubleType) {
          DoubleType doubleType = (DoubleType) next;
          jsonGenerator.writeNumberField(fieldName, doubleType.getValue());
        } else if (next instanceof BigDecimalType) {
          BigDecimalType bigDecimalType = (BigDecimalType) next;
          jsonGenerator.writeNumberField(fieldName, bigDecimalType.getValue());
        } else if (next instanceof BigIntegerType) {
          BigIntegerType bigIntegerType = (BigIntegerType) next;
          jsonGenerator.writeNumberField(fieldName, bigIntegerType.getValue().longValue());
        } else if (next instanceof DateType) {
          DateType dateType = (DateType) next;
          DateConverter dc = new DateConverter(true);
          jsonGenerator.writeStringField(fieldName, dc.format(dateType.getValue()));
        } else if (next instanceof LocalTimeType) {
          LocalTimeType localTimeType = (LocalTimeType) next;
          DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
          jsonGenerator.writeStringField(fieldName, tf.format(localTimeType.getValue()));
        } else if (next instanceof BooleanType) {
          BooleanType booleanType = (BooleanType) next;
          jsonGenerator.writeBooleanField(fieldName, booleanType.getValue());
        } else if (next instanceof EnumType) {
          EnumType enumType = (EnumType) next;
          jsonGenerator.writeObjectField(fieldName, enumType.getValue());
        } else {
          throw new IllegalStateException(" problems and worries " + next + "  >> " + next);
        }
      }
    }

    //many to one relations
    Set<Map.Entry<String, GenericDTO>> r2 = value.getRelations2().entrySet();
    for (Map.Entry<String, GenericDTO> entry : r2) {
      jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
    }
    jsonGenerator.writeEndObject();
    //one to many relations
    jsonGenerator.writeObjectFieldStart("relations");
    Set<Map.Entry<String, Set<GenericDTO>>> allRelations = value.getRelations().entrySet();
    for (Map.Entry<String, Set<GenericDTO>> entry : allRelations) {
      Set<GenericDTO> next = entry.getValue();
      jsonGenerator.writeArrayFieldStart(entry.getKey());
      for (GenericDTO element : next) {
        jsonGenerator.writeObject(element);
      }
      jsonGenerator.writeEndArray();

    }
    jsonGenerator.writeEndObject(); //end one to many relations
    jsonGenerator.writeEndObject();
  }
}
