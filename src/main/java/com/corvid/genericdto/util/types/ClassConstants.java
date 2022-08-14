
package com.corvid.genericdto.util.types;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * INTERNAL:
 */
@SuppressWarnings("rawtypes")
public class ClassConstants {

    // Moved from ConversionManager
    public static final Class ABYTE = Byte[].class;

    public static final Class AOBJECT = Object[].class;

    public static final Class ACHAR = Character[].class;

    public static final Class APBYTE = byte[].class;

    public static final Class APCHAR = char[].class;

    public static final Class BIGDECIMAL = BigDecimal.class;

    public static final Class BIGINTEGER = BigInteger.class;

    public static final Class BOOLEAN = Boolean.class;

    public static final Class BYTE = Byte.class;

    public static final Class CLASS = Class.class;

    public static final Class CHAR = Character.class;

    public static final Class CALENDAR = Calendar.class;

    public static final Class DOUBLE = Double.class;

    public static final Class FLOAT = Float.class;

    public static final Class GREGORIAN_CALENDAR = GregorianCalendar.class;

    public static final Class INTEGER = Integer.class;

    public static final Class LONG = Long.class;

    public static final Class NUMBER = Number.class;

    public static final Class OBJECT = Object.class;

    public static final Class PBOOLEAN = boolean.class;

    public static final Class PBYTE = byte.class;

    public static final Class PCHAR = char.class;

    public static final Class PDOUBLE = double.class;

    public static final Class PFLOAT = float.class;

    public static final Class PINT = int.class;

    public static final Class PLONG = long.class;

    public static final Class PSHORT = short.class;

    public static final Class SHORT = Short.class;

    public static final Class SQLDATE = java.sql.Date.class;

    public static final Class STRING = String.class;

    public static final Class TIME = java.sql.Time.class;

    public static final Class TIMESTAMP = java.sql.Timestamp.class;

    public static final Class UTILDATE = Date.class;

    public static final Class QNAME = QName.class;

    public static final Class XML_GREGORIAN_CALENDAR = XMLGregorianCalendar.class;

    public static final Class DURATION = Duration.class;

    private ClassConstants() {
    }
}
