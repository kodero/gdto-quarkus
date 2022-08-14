package com.corvid.genericdto.data.gdto;

import com.corvid.genericdto.data.gdto.types.BigDecimalType;
import com.corvid.genericdto.data.gdto.types.BigIntegerType;
import com.corvid.genericdto.data.gdto.types.BooleanType;
import com.corvid.genericdto.data.gdto.types.CalendarDateType;
import com.corvid.genericdto.data.gdto.types.CharacterType;
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
import com.corvid.genericdto.shared.MonetaryAmount;
import com.corvid.genericdto.data.gdto.types.MoneyType;
import com.corvid.genericdto.data.gdto.types.StringType;
import com.corvid.genericdto.shared.Money;
import com.corvid.genericdto.shared.time.CalendarDate;
import com.corvid.genericdto.shared.time.Duration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.corvid.genericdto.data.gdto.Assert.notNull;

@SuppressWarnings("rawtypes")
@JsonDeserialize(using = GenericDTODeserializer.class)
@JsonSerialize(using = GenericDTOSerializer.class)
public class GenericDTO implements Serializable {
  private static final String THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL = "The name of the relation cannot be null !";

  private static final String ATTRIBUTE_NAME_CANNOT_BE_NULL = "Attribute name cannot be null";

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String DATE_FORMAT_2 = "yyyy-MM-dd";
  // public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private Map<String, Attribute> attributes = null;

  @JsonProperty
  private Map<String, Set<GenericDTO>> relations = null;

  @JsonProperty
  private Map<String, GenericDTO> relations2 = null;

  private String name = null;

  public GenericDTO() {
    this.attributes = new HashMap<>();
    this.relations = new HashMap<>();
    this.relations2 = new HashMap<>();
  }

  public GenericDTO(String name) {
    notNull(name, "The name of the DTO cannot be null...");
    this.attributes = new HashMap<>();
    this.relations = new HashMap<>();
    this.relations2 = new HashMap<>();
    this.name = name;
  }

  public GenericDTO add(String name, Attribute<?> attribute) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, attribute);
    return this;
  }

  public GenericDTO addString(String name, String value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    if (value != null) {
      this.attributes.put(name, new StringType(null, value)); //only bother if value is not null
    }
    return this;
  }

  public GenericDTO addGenericEnum(String name, String value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    if (value != null) {
      this.attributes.put(name, new EnumType(null, value)); //only bother if value is not null
    }
    return this;
  }

  public GenericDTO addChar(String name, Character value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new CharacterType(null, String.valueOf(value)));
    return this;
  }

  public GenericDTO addDate(String name, Date value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    DateConverter sdf = new DateConverter(true);
    this.attributes.put(name, value == null ? null : new DateType(null, sdf.format(value)));
    return this;
  }

  public GenericDTO addInt(String name, Integer value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new IntType(null, value == null ? null : String.valueOf(value)));
    return this;
  }

  public GenericDTO addBigDecimal(String name, BigDecimal value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new BigDecimalType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addLong(String name, Long value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new LongType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addFloat(String name, Float value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new FloatType(null, value == null ? null : String.valueOf(value)));
    return this;
  }

  public GenericDTO addDouble(String name, Double value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new DoubleType(null, value == null ? null : String.valueOf(value)));
    return this;
  }

  public GenericDTO addBoolean(String fieldName, Boolean value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(fieldName,
        new BooleanType(null, value == null ? null : String.valueOf(value)));
    return this;
  }

  public GenericDTO addBigInteger(String name, BigInteger value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new BigIntegerType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addMoney(String name, Money value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name,
        new MoneyType(null, value == null ? Money.shillings(0.0).toString() : value.toString()));
    return this;
  }

  public GenericDTO addMonetaryAmount(String name, MonetaryAmount value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    //notNull(value, "Attribute with name: " + name + " is null!");
    this.attributes.put(name,
        new MonetaryAmountType(null, value == null ? MonetaryAmount.shillings(0.0).toString() : value.toString()));
    return this;
  }



  public GenericDTO addCalendarDate(String name, CalendarDate value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new CalendarDateType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addDuration(String name, Duration value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new DurationType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addLocalTime(String name, LocalTime value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new LocalTimeType(null, value == null ? null : value.toString()));
    return this;
  }

  public GenericDTO addCurrency(String name, Currency value) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.put(name, new CurrencyType(value == null ? null : value.getCurrencyCode()));
    return this;
  }

  public GenericDTO remove(String name) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    this.attributes.remove(name);
    return this;
  }

  public GenericDTO addRelation(String relationName, GenericDTO genericDTO) {
    notNull(relationName, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);
    notNull(genericDTO,
        "The target cannot for the relation with name " + relationName + " be null");
    addTarget(relationName, genericDTO);
    return this;
  }

  public GenericDTO addRelation2(String relationName, GenericDTO genericDTO) {
    Assert.notNull(relationName, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);
    notNull(genericDTO,
        "The target cannot for the relation with name " + relationName + " be null");
    //TODO1 check that it does not exist
    if(genericDTO.getAttributes().hasNext()) //empty empty relation2 specified, so skip
      this.relations2.put(relationName, genericDTO);
    return this;
  }

  public GenericDTO removeRelation2(String relationName) {
    Assert.notNull(relationName, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);

    this.relations2.remove(relationName);
    return this;
  }

  private GenericDTO addTarget(String relationName, GenericDTO target) {
    this.relations.merge(relationName, new LinkedHashSet<>(), (targets, t) -> {
      targets.add(target);
      return targets;
    });
    return this;
  }

  public Attribute get(String name) {
    notNull(name, ATTRIBUTE_NAME_CANNOT_BE_NULL);
    return this.attributes.get(name);
  }

  public Set<GenericDTO> getTargets(String name) {
    notNull(name, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);
    return this.relations.get(name);
  }

  public GenericDTO getTarget(String name) {
    notNull(name, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);
    return this.relations.get(name).iterator().next();
  }

  public GenericDTO getTarget2(String name) {
    notNull(name, THE_NAME_OF_THE_RELATION_CANNOT_BE_NULL);
    return this.relations2.get(name);
  }

  public Iterator<String> getAttributeNames() {
    return this.attributes.keySet().iterator();
  }

  public Iterator<String> getRelationNames() {
    return this.relations.keySet().iterator();
  }

  public Iterator<Attribute> getAttributes() {
    return this.attributes.values().iterator();
  }

  public Iterator<Set<GenericDTO>> getTargets() {
    return this.relations.values().iterator();
  }

  public GenericDTO validate() throws CompositeValidationException {
    Set<Map.Entry<String, Attribute>> attributeEntries = this.attributes.entrySet();
    Iterator<Map.Entry<String, Attribute>> attributeIterator = attributeEntries.iterator();
    CompositeValidationException compositeValidationException =
        new CompositeValidationException(this.name);
    Map.Entry<String, Attribute> entry = null;
    while (attributeIterator.hasNext()) {
      try {
        entry = attributeIterator.next();
        Attribute<?> attributeEntry = entry.getValue();
        attributeEntry.validate();
      } catch (ValidationException ex) {
        compositeValidationException.add(entry.getKey(), ex);
      }
      //some validation errors occurred
      if (!compositeValidationException.isEmpty()) {
        throw compositeValidationException;
      }
    }
    return this;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("GDTO Name: ").append(this.name);
    result.append("\n").append("--- attributes ---").append("\n");
    Set<Map.Entry<String, Attribute>> attributeEntries = this.attributes.entrySet();
    Iterator<Map.Entry<String, Attribute>> attributeIterator = attributeEntries.iterator();
    while (attributeIterator.hasNext()) {
      Map.Entry<String, Attribute> entry = attributeIterator.next();
      result.append(" Name: ")
          .append(entry.getKey())
          .append(", Content: ")
          .append(entry.getValue() == null ? "" : entry.getValue().getValue());
    }
    return result.toString();
  }

  public Attribute getId() {
    Iterator<Attribute> iterator = this.getAttributes();
    while (iterator.hasNext()) {
      Attribute<?> attribute = iterator.next();
      if (attribute.isId()) {
        return attribute;
      }
    }
    return null;
  }

  public int getNumberOfAttributes() {
    return this.attributes.size();
  }

  public boolean isEmpty() {
    return (this.attributes.isEmpty() && this.relations.isEmpty());
  }

  //TODO1 get the default field names in the format a, or a:b (for relationship)
  public static Collection<String> fieldNames(GenericDTO dto) {
    Set<String> attributeFields = new LinkedHashSet<>();
    final Iterator<String> attributeNames = dto.getAttributeNames();
    while (attributeNames.hasNext()) {
      String next = attributeNames.next();
      attributeFields.add(next);
    }
    return attributeFields;
  }

  /**
   * ******************************************
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Attribute> attributeMap() {
    return attributes;
  }

  public Map<String, Set<GenericDTO>> getRelations() {
    return relations;
  }

  public void setRelations(Map<String, Set<GenericDTO>> relations) {
    this.relations = relations;
  }

  public void setAttributes(Map<String, Attribute> attributes) {
    this.attributes = attributes;
  }

  public Map<String, GenericDTO> getRelations2() {
    return relations2;
  }
}
