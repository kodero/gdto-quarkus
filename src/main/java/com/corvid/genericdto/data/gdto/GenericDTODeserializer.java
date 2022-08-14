package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.data.annotations.JsonContent;
import com.corvid.genericdto.shared.MonetaryAmount;
import com.corvid.genericdto.shared.Money;
import com.corvid.genericdto.shared.time.CalendarDate;
import com.corvid.genericdto.shared.time.Duration;
import com.corvid.genericdto.util.LoggingUtil;
import com.corvid.genericdto.util.Reflections;
import com.corvid.genericdto.util.mirror.Mirror;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.jboss.logging.Logger.Level;

@SuppressWarnings("rawtypes")
public class GenericDTODeserializer extends JsonDeserializer<GenericDTO> {

  @Override
  public GenericDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);
    GenericDTO g = new GenericDTO();
    try {
      g = extractGenericDTO(node, g);
      LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The extracted dto [%s]", g));
      return g;
    } catch (ClassNotFoundException | NoSuchFieldException | ParseException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(" " + e.getMessage());
    }

  }

  private GenericDTO extractGenericDTO(JsonNode node, GenericDTO g)
      throws IOException, ClassNotFoundException, NoSuchFieldException, ParseException, InstantiationException, IllegalAccessException {
    Iterator<Map.Entry<String, JsonNode>> allFields = node.fields();
    while (allFields.hasNext()) {
      Map.Entry<String, JsonNode> next = allFields.next();
      final String nextKey = next.getKey();
      if (nextKey.compareTo("name") == 0) {
        handleName(g, next.getValue());
      } else if (nextKey.compareTo("relations") == 0) {
        handleRelation(g, next.getValue());
      } else if (nextKey.compareTo("attributes") == 0) {
        handleAttributes(g, next.getValue());
      }
    }
    return g;
  }

  private GenericDTO handleAttributes(GenericDTO g, JsonNode value)
      throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IOException, ParseException {
    Iterator<String> allAttributesNames = value.fieldNames();
    LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("JSON Node : [%s], GenericDTO : [%s]", value, g));
    //target entity
    Object targetEntity = Mirror.loadClass(g.getName()).newInstance();
    /*Mirror m = Mirror.me(targetEntity);*/
    while (allAttributesNames.hasNext()) {
      final String nextAttributeName = allAttributesNames.next();
      final Field targetField = Reflections.getField(targetEntity.getClass(), nextAttributeName);
      final Class<?> targetFieldType = targetField.getType();
      final JsonNode currentAttributeNode = value.get(nextAttributeName);

      LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The current attribute node: %s", currentAttributeNode));

      LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The target filed type: %s", targetFieldType));

      Mirror m = Mirror.me(targetFieldType);

      if (m.is(MonetaryAmount.class.getName())) {
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The monetary amount field , node '%s', and string value '%s'", currentAttributeNode, currentAttributeNode.textValue()));
        ObjectMapper mapper = new ObjectMapper();
        MonetaryAmount monetaryAmount = mapper
            .treeToValue(currentAttributeNode, MonetaryAmount.class);
        g.addMonetaryAmount(nextAttributeName, monetaryAmount);
      } else if (m.is(Currency.class.getName())) {
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The currency field, node [%s], and string value [%s]", currentAttributeNode, currentAttributeNode.textValue()));
        ObjectMapper mapper = new ObjectMapper();
        Currency currency = mapper.treeToValue(currentAttributeNode, Currency.class);
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("The currency [%s]", currency));
        g.addCurrency(nextAttributeName, currency);
      } else if (m.is(Duration.class.getName())) {
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format(
            "The duration field, node [%s], and string value [%s]", currentAttributeNode, currentAttributeNode.textValue()));
        ObjectMapper mapper = new ObjectMapper();
        Duration duration = mapper.treeToValue(currentAttributeNode, Duration.class);
        g.addDuration(nextAttributeName, duration);
      } else if (m.is(CalendarDate.class.getName())) {
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format(
            "The calendar date field, node [%s], and string value [%s]", currentAttributeNode, currentAttributeNode.textValue()));
        ObjectMapper mapper = new ObjectMapper();
        CalendarDate calendarDate = mapper.treeToValue(currentAttributeNode, CalendarDate.class);
        g.addCalendarDate(nextAttributeName, calendarDate);
      } else if (m.is(Money.class.getName())) {
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format(
            "The money field, node [%s], and string value [%s]", currentAttributeNode, currentAttributeNode.textValue()));
        ObjectMapper mapper = new ObjectMapper();
        Money money = mapper.treeToValue(currentAttributeNode, Money.class);
        g.addMoney(nextAttributeName, money);
      } else if (m.is(BigDecimal.class.getName())) {
        g.addBigDecimal(nextAttributeName, currentAttributeNode.decimalValue());
      } else if (m.is(BigInteger.class.getName())) {
        g.addBigInteger(nextAttributeName, currentAttributeNode.bigIntegerValue());
      } else if (m.isString()) {
        if (targetField.isAnnotationPresent(JsonContent.class)) {
          g.addString(nextAttributeName,
              currentAttributeNode.textValue() == null ? currentAttributeNode.toString()
                  : currentAttributeNode.textValue());
        } else {
          g.addString(nextAttributeName, currentAttributeNode.textValue());
        }
      } else if (m.isStringLike()) {
        g.addString(nextAttributeName, currentAttributeNode.textValue());
      } else if (m.isChar()) {
        g.addChar(nextAttributeName, currentAttributeNode.textValue().charAt(0));
      } else if (m.isEnum()) {
        if (currentAttributeNode.has("key")) {//object value representation
          g.addString(nextAttributeName, currentAttributeNode.get("key").textValue());
        } else {
          g.addString(nextAttributeName, currentAttributeNode.textValue());
        }
      } else if (m.isBoolean()) {
        g.addBoolean(nextAttributeName, currentAttributeNode.booleanValue());
      } else if (m.isFloat()) { //TODO1 the cast below is not safe
        g.addFloat(nextAttributeName, (float) currentAttributeNode.doubleValue());
      } else if (m.isDouble()) {
        g.addDouble(nextAttributeName, currentAttributeNode.doubleValue());
      } else if (m.isInt()) { // int or Integer
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format(
            "The int field, node [%s], and string value [%s]", currentAttributeNode, currentAttributeNode.textValue()));
        if (m.is(int.class)) {
          g.addInt(nextAttributeName, currentAttributeNode.intValue());
        } else {
          int v = currentAttributeNode.textValue() == null ? currentAttributeNode.intValue()
              : Integer.parseInt(currentAttributeNode.textValue());
          g.addInt(nextAttributeName, v);
        }

      } else if (m.isLong()) {
        g.addLong(nextAttributeName, currentAttributeNode.longValue());
      } else if (m.isShort()) {
        g.addInt(nextAttributeName, currentAttributeNode.intValue());
      } else if (m.isByte()) {
        g.addInt(nextAttributeName, currentAttributeNode.intValue());
      } else if (m.isLocalTime() ) {
        String content = currentAttributeNode.textValue();
        LoggingUtil.log(this.getClass(), Level.DEBUG, String.format("constructing from string [%s]", content));
        if (content == null) continue;
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(content, tf);
        g.addLocalTime(nextAttributeName, localTime);
      } else if (m.isDateTimeLike()) {
        //handle the dates
        //dates are serialized to strings
        //apparently, they can also be longs
        Date date = null;
        if (currentAttributeNode.isTextual()) {
          final String valueToAssign = currentAttributeNode.textValue();
          DateConverter sp = new DateConverter(true);

          try {
            date = sp.parse(valueToAssign);
          } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
          }
        } else if (currentAttributeNode.isLong()) {
          date = new Date(currentAttributeNode.longValue());
        }
        g.addDate(nextAttributeName, date);
      } else if (targetField.isAnnotationPresent(ManyToOne.class) || targetField
          .isAnnotationPresent(OneToOne.class)) {
        //ManyToOne relation
        g.addRelation2(nextAttributeName,
            extractGenericDTO(currentAttributeNode, new GenericDTO()));
      } else {
        throw new IllegalStateException(
            "unknown field type " + targetFieldType.getName() + " value is "
                + currentAttributeNode);
      }
    }
    return g;
  }

  private GenericDTO handleRelation(GenericDTO g, JsonNode value)
      throws IOException, ParseException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    Iterator<String> allRelationsNames = value.fieldNames();
    while (allRelationsNames.hasNext()) {
      String nextRelationName = allRelationsNames.next();
      JsonNode relationObjectSet = value.get(nextRelationName);
      //this is an array node, containing generic dto objects
      Iterator<JsonNode> arrayElements = relationObjectSet.elements();
      while (arrayElements.hasNext()) {
        JsonNode nextArrayElement = arrayElements.next();
        GenericDTO child = new GenericDTO();
        g.addRelation(nextRelationName, extractGenericDTO(nextArrayElement, child));
      }

    }
    return g;
  }

  private GenericDTO handleName(GenericDTO g, JsonNode next) {
    g.setName(next.textValue());
    return g;
  }
}
