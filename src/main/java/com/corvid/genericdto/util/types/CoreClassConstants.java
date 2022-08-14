
package com.corvid.genericdto.util.types;

import org.w3c.dom.Node;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class CoreClassConstants {

    public static final Class<?> ABYTE = Byte[].class;

    public static final Class<?> APBYTE = byte[].class;

    public static final Class<?> APCHAR = char[].class;

    public static final Class<?> ArrayList_class = ArrayList.class;

    public static final Class<?> BIGDECIMAL = BigDecimal.class;

    public static final Class<?> BIGINTEGER = BigInteger.class;

    public static final Class<?> BOOLEAN = Boolean.class;

    public static final Class<?> BYTE = Byte.class;

    public static final Class<?> CALENDAR = Calendar.class;

    public static final Class<?> CHAR = Character.class;

    public static final Class<?> CLASS = Class.class;

    public static final Class<?> Collection_Class = Collection.class;

    public static final Class<?> DOUBLE = Double.class;

    public static final Class<?> DURATION = Duration.class;

    public static final Class<?> FLOAT = Float.class;

    public static final Class<?> GREGORIAN_CALENDAR = GregorianCalendar.class;

    public static final Class<?> INTEGER = Integer.class;

    public static final Class<?> List_Class = List.class;

    public static final Class<?> LONG = Long.class;

    public static final Class<?> Map_Class = Map.class;

    public static final Class<?> NODE = Node.class;

    public static final Class<?> OBJECT = Object.class;

    public static final Class<?> PBOOLEAN = boolean.class;

    public static final Class<?> PBYTE = byte.class;

    public static final Class<?> PCHAR = char.class;

    public static final Class<?> PDOUBLE = double.class;

    public static final Class<?> PFLOAT = float.class;

    public static final Class<?> PINT = int.class;

    public static final Class<?> PLONG = long.class;

    public static final Class<?> PSHORT = short.class;

    public static final Class<?> Set_Class = Set.class;

    public static final Class<?> SHORT = Short.class;

    public static final Class<?> SQLDATE = java.sql.Date.class;

    public static final Class<?> STRING = String.class;

    public static final Class<?> TIME = Time.class;

    public static final Class<?> TIMESTAMP = Timestamp.class;

    public static final Class<?> URL_Class = URL.class;

    public static final Class<?> UTILDATE = Date.class;

    public static final Class<?> XML_GREGORIAN_CALENDAR = XMLGregorianCalendar.class;

    private CoreClassConstants() {
        throw new IllegalStateException("Utility class");
      }
}
