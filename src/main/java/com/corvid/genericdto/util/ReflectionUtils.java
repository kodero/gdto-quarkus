package com.corvid.genericdto.util;

import com.corvid.genericdto.data.gdto.DateConverter;
import com.corvid.genericdto.data.gdto.GenericDTO;
import com.corvid.genericdto.shared.MonetaryAmount;
import com.corvid.genericdto.shared.Money;
import com.corvid.genericdto.shared.time.CalendarDate;
import com.corvid.genericdto.shared.time.Duration;
import com.corvid.genericdto.util.mirror.Mirror;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA. User: mokua,kodero Date: 2/27/14 Time: 11:50 AM
 */
public class ReflectionUtils {
  private static Logger log = Logger.getLogger(ReflectionUtils.class.getName());
  public static String[] ignoreFields = new String[] {
      "version",
      "createdByUser",
      "updatedByUser",
      "deletedById",
      "lockedById"
  };

  public static List<Field> primitiveFields(Class<?> clazz) {
    List<Field> allFields = new ArrayList<>();
    List<Field> primitiveFieldsNames = new ArrayList<>();
    getAllFields(allFields, clazz);
    Set<String> in = new HashSet<>(Arrays.asList(ignoreFields));
    List<Field> manyToOneFields = Reflections.getFields(clazz, ManyToOne.class);
    manyToOneFields.addAll(Reflections.getFields(clazz, OneToMany.class));
    for (Field rr : manyToOneFields) {
      in.add(rr.getName());
    }
    for (Field field : allFields) {
      if (in.contains(field.getName())) continue;
      if (!isCollectionField(field)) {
        primitiveFieldsNames.add(field);
      }
    }
    return primitiveFieldsNames;
  }

  public static List<String> primitiveFieldNames(Class<?> clazz) {
    List<Field> allFields = new ArrayList<>();
    List<String> primitiveFieldsNames = new ArrayList<>();
    getAllFields(allFields, clazz);
    Set<String> in = new HashSet<>(Arrays.asList(ignoreFields));
    List<Field> manyToOneFields = Reflections.getFields(clazz, ManyToOne.class);
    manyToOneFields.addAll(Reflections.getFields(clazz, OneToMany.class));
    for (Field rr : manyToOneFields) {
      in.add(rr.getName());
    }
    for (Field field : allFields) {
      if (in.contains(field.getName()) || Modifier.isStatic(field.getModifiers()) || isTransientField(field)) continue;
      if (!isCollectionField(field)) {
        primitiveFieldsNames.add(field.getName());
      }
    }
    return primitiveFieldsNames;
  }

  public static boolean isCollectionField(Field field) {
    return Collection.class.isAssignableFrom(field.getType());
  }

  public static boolean isTransientField(Field field) {
    return field.getAnnotationsByType(Transient.class).length > 0;
  }

  public static Field getField(String fieldName, Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    getAllFields(fields, clazz);
    for (Field field : fields) {
      if (field.getName().compareTo(fieldName) == 0) {
        return field;
      }
    }
    return null;
  }

  public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    Collections.addAll(fields, type.getDeclaredFields());

    if (type.getSuperclass() != null) {
      fields = getAllFields(fields, type.getSuperclass());
    }

    return fields;
  }

  public static void main(String[] args) {
       /* List<String> res = ReflectionUtils.primitiveFieldNames(Permission.class);
        for (String s : res) {
            System.out.println(s);
        }*/
        /*ProgramRoleType programRoleType = new ProgramRoleType("NAME", "Description");
        ProgramRoleType programRoleType1 = new ProgramRoleType("NAME");
        Object res = diff(programRoleType1, programRoleType);
        System.out.println(res);*/

    //System.out.println(get100YrsFromNow());
  }

  public static Date get100YrsFromNow() {
    String f = "2114-02-27 14:32:01";
    //SimpleDateFormat sp = new SimpleDateFormat(GenericDTO.DATE_FORMAT);
    DateConverter sp = new DateConverter(true);
    try {
      return sp.parse(f);
    } catch (ParseException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return null;
  }

  /**
   * copy primitive fields from source to target
   */
  public static Object copyFields(Object target, Object source) {
    for (Class<?> obj = source.getClass();
         !obj.equals(Object.class);
         obj = obj.getSuperclass()) {
      Field[] fields = obj.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        try {
          //get the diff from target
          String name = fields[i].getName();
          if (name.compareTo("version") == 0 ) continue;
          Field targetField = Reflections.getField(target.getClass(), name);
          Object targetFieldValue = Reflections.get(targetField, target);
          Object markerFieldValue = Reflections.get(fields[i], source);
          if (targetFieldValue == null && markerFieldValue != null ) {
            //source has a new value set
            Reflections.setAndWrap(targetField, target, markerFieldValue);
          }else{
            //the field has been updated in source, so we need to update the target to the new value
            Reflections.setAndWrap(targetField, target, markerFieldValue);
          }
        } catch (Exception e) {
          e.printStackTrace();
        } 
      }
    }
    return target;
  }

  /**
   * get the diff between target and marker. if an attribute is null on target but on marker it has
   * a value, clone it, else continue
   */
  public static Object diff(Object target, Object marker) {
    for (Class<?> markerClass = marker.getClass();
        !markerClass.equals(Object.class);
         markerClass = markerClass.getSuperclass()) {
      Field[] fields = markerClass.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {

        try {
          //get the diff from target
          Field targetField = Reflections.getField(target.getClass(), fields[i].getName());
          Object targetFieldValue = Reflections.get(targetField, target);
          if (targetFieldValue == null) {
            //is marker field null too?
            Object markerFieldValue = Reflections.get(fields[i], marker);
            if (markerFieldValue == null) {
              //System.out.println(" marker field "+ fields[i] +" is null");
            } else {
              Reflections.setAndWrap(targetField, target, markerFieldValue);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return target;
  }

  public static Object newDiff(Object target, Object marker) {
    log.info("-----Diffing------");
    for (Class<?> markerClass = marker.getClass(); !markerClass.equals(Object.class); markerClass = markerClass.getSuperclass()) {
      Field[] fields = markerClass.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        try {
          //get the diff from target
          Field targetField = Reflections.getField(target.getClass(), fields[i].getName());
          //Object targetFieldValue = Reflections.get(targetField, target);
          Object markerFieldValue = Reflections.get(fields[i], marker);
          //get the marker field value and set it on the target
          log.info(String.format("---------Marker Field: [%s] Marker Field Value: [%s]", markerFieldValue, fields[i].getName()));
          Reflections.setAndWrap(targetField, target, markerFieldValue);
          log.info(String.format("---------Target Field: [%s], Target Field Value: [%s]", targetField.getName(), Reflections.get(targetField, target)));
          log.info(String.format("Target: [%s]", target));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return target;
  }

  public static Object clone(Object o) {
    Object clone = null;

    try {
      clone = o.getClass().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    } 

    // Walk up the superclass hierarchy
    for (Class<?> obj = o.getClass();
        !obj.equals(Object.class);
        obj = obj.getSuperclass()) {
      Field[] fields = obj.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        fields[i].setAccessible(true);
        try {
          // for each class/suerclass, copy all fields
          // from this object to the clone
          fields[i].set(clone, fields[i].get(o));
        } catch (IllegalArgumentException | IllegalAccessException e) {
        } 
      }
    }
    return clone;
  }

  /**
   * TODO1 haack!
   */
  public static void extractValueFromFieldToDTO(GenericDTO targetDTO, Object valueToAssign,
    String fieldName,
    Field sourceField) {
    try{
      log.info(" DTO " + targetDTO.getName() + "  , object : value " + valueToAssign + " class " + valueToAssign == null? "NULL" : valueToAssign.getClass().getName() + "  " +
          "Field name " + fieldName + "  field type " + sourceField.getType().getName());
    }catch (Exception e){
      //ignore
      log.info("smtg is null ");
    }


    Class<?> sourceFieldType = sourceField.getType();
    Mirror<?> m = valueToAssign == null ? Mirror.me(sourceFieldType) : Mirror.me(valueToAssign);
    if (m.is(Duration.class.getName())) {
      targetDTO.addDuration(fieldName, (Duration) valueToAssign);
    }else if (m.is(LocalTime.class.getName())) {
      targetDTO.addLocalTime(fieldName, (LocalTime) valueToAssign);
    }else if (m.is(MonetaryAmount.class.getName())) {
      targetDTO.addMonetaryAmount(fieldName, (MonetaryAmount) valueToAssign);
    }else if (m.is(Currency.class.getName())) {
      targetDTO.addCurrency(fieldName, (Currency) valueToAssign);
    } else if (m.is(CalendarDate.class.getName())) {
      targetDTO.addCalendarDate(fieldName, (CalendarDate) valueToAssign);
    } else if (m.is(Money.class.getName())) {
      targetDTO.addMoney(fieldName, (Money) valueToAssign);
    } else if (m.is(BigDecimal.class.getName())) {
      targetDTO.addBigDecimal(fieldName, (BigDecimal) valueToAssign);
    } else if (m.is(BigInteger.class.getName())) {
      targetDTO.addBigInteger(fieldName, (BigInteger) valueToAssign);
    } else if (m.isString()) {
      targetDTO.addString(fieldName, (String) valueToAssign);
    } else if (m.isStringLike()) {
      targetDTO.addString(fieldName, (String) valueToAssign);
    } else if (m.isChar()) {
      targetDTO.addChar(fieldName, (Character) valueToAssign);
    } else if (m.isEnum()) {
      if (valueToAssign == null) {
        targetDTO.addString(fieldName, null);
      } else {
        Enum<?> enums = (Enum) valueToAssign;
        targetDTO.addGenericEnum(fieldName,
            "{\"key\" : \"" + enums.name() + "\", \"name\" : \"" + enums.toString() + "\"}");
      }
    } else if (m.isBoolean()) {
      targetDTO.addBoolean(fieldName, (Boolean) valueToAssign);
    } else if (m.isFloat()) {
      targetDTO.addFloat(fieldName, (Float) valueToAssign);
    } else if (m.isDouble()) {
      targetDTO.addDouble(fieldName, (Double) valueToAssign);
    } else if (m.isInt()) {
      targetDTO.addInt(fieldName, (Integer) valueToAssign);
    } else if (m.isLong()) {
      targetDTO.addLong(fieldName, (Long) valueToAssign);
    } else if (m.isShort()) {
      targetDTO.addInt(fieldName, (Integer) valueToAssign);
    } else if (m.isByte()) {
      targetDTO.addInt(fieldName, (Integer) valueToAssign);
    } else if (m.isDateTimeLike()) {
      //handle the dates
      if (Calendar.class.isAssignableFrom(m.getType())) {
        Calendar v = (Calendar) valueToAssign;
        targetDTO.addDate(fieldName, v.getTime());
      } else if (Date.class.isAssignableFrom(m.getType())) {
        targetDTO.addDate(fieldName, (Date) valueToAssign);
      } else if (java.sql.Date.class.isAssignableFrom(m.getType())) {
        java.sql.Date v = (java.sql.Date) valueToAssign;
        targetDTO.addDate(fieldName, new Date(v.getTime()));
      } else if (Time.class.isAssignableFrom(m.getType())) {
        Time v = (Time) valueToAssign;
        targetDTO.addDate(fieldName, new Date(v.getTime()));
      } else {
        throw new IllegalStateException(
            "unknown date type " + sourceFieldType.getName() + " value is " + valueToAssign);
      }
    } else if (m.isPojo()) {
      // System.out.println(" its a pojo, class name " + valueToAssign.getClass().getName());
           /* GenericDTO relateddto = new GenericDTO(valueToAssign.getClass().getName());
            //populate the relateddto with values in the default fields == the last fieldName
            //get the valueToAsssign.invoke the method and return value,sat g
            try {
                GenericDTO res = new BaseEntityService() {
                }.toDTO((ModelBase) valueToAssign, new String[]{"firstName"}, valueToAssign.getClass());
                targetDTO.addRelation(fieldName,res);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }*/
           /* System.out.println("unknown field type " + sourceFieldType.getName() + " value is "
                    + valueToAssign + " the field name "+ fieldName + "source field " +sourceField.getName() +" the m " + m.toString());
            String[] tokens = fieldName.split(":");
            String lastParamName = tokens[tokens.length - 1].trim();
            System.out.println("Tokens "+Arrays.asList(tokens) +" Last name param " + lastParamName );
            Method getter = Reflections.getGetterMethod(valueToAssign.getClass(), lastParamName);
            Object object = Reflections.invokeAndWrap(getter, valueToAssign);;//Reflections.getAndWrap(lastParamNameField, valueToAssign);
            System.out.println("object "+ object);
            Field lastParamNameField = Reflections.getField(valueToAssign.getClass(),lastParamName);
            extractValueFromFieldToDTO(relateddto,object,lastParamName,lastParamNameField);
            System.out.println(" the extracted related entity " + relateddto);
            targetDTO.addRelation(fieldName,relateddto);*/

    } else {

      throw new IllegalStateException(
          "unknown field type "
              + sourceFieldType.getName()
              + " value is "
              + valueToAssign
              + " the field name "
              + fieldName
              + "source field "
              + sourceField.getName()
              + " the m "
              + m.toString());
    }
  }

  public static Calendar dateToCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }
}
