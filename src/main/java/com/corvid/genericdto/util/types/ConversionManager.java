package com.corvid.genericdto.util.types;

import com.corvid.genericdto.data.gdto.DateConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p/>
 * <b>Purpose</b>: Contains the conversion routines for some common classes in the system.
 * Primarily used to convert objects from a given database type to a different type in Java.
 * Uses a singleton instance, this is also used from the platform.
 * <p/>
 * <b>Responsibilities</b>:
 * <ul>
 * <li> Execute the appropriate conversion routine.
 * </ul>
 */
@SuppressWarnings("rawtypes")
public class ConversionManager {
    protected Map defaultNullValues;

    /**
     * This flag is here if the Conversion Manager should use the class loader on the
     * thread when loading classes.
     */
    protected boolean shouldUseClassLoaderFromCurrentThread = false;

    protected static ConversionManager defaultManager;

    /**
     * Allows the setting of a global default if no instance-level loader is set.
     */
    private static ClassLoader defaultLoader;

    protected ClassLoader loader;

    /**
     * Store the list of Classes that can be converted to from the key.
     */
    protected Hashtable dataTypesConvertedFromAClass;

    /**
     * Store the list of Classes that can be converted from to the key.
     */
    protected Hashtable dataTypesConvertedToAClass;

    public ConversionManager() {
        this.dataTypesConvertedFromAClass = new Hashtable();
        this.dataTypesConvertedToAClass = new Hashtable();
    }

    /**
     * Convert the object to the appropriate type by invoking the appropriate
     * ConversionManager method
     *
     * @param sourceObject - the object that must be converted
     * @param javaClass    - the class that the object must be converted to
     * @return - the newly converted object
     * @throws - ConversionException, all exceptions will be thrown as this type.
     */
    public Object convertObject(Object sourceObject, Class javaClass) throws ConversionException {
        if (sourceObject == null) {
            return null;
        }

        if ((sourceObject.getClass() == javaClass) || (javaClass == null) || (javaClass == ClassConstants.OBJECT)) {
            return sourceObject;
        }

        try {
            if (javaClass == ClassConstants.STRING) {
                return convertObjectToString(sourceObject);
            } else if (javaClass == ClassConstants.UTILDATE) {
                return convertObjectToUtilDate(sourceObject);
            } else if (javaClass == ClassConstants.SQLDATE) {
                return convertObjectToDate(sourceObject);
            } else if (javaClass == ClassConstants.TIME) {
                return convertObjectToTime(sourceObject);
            } else if (javaClass == ClassConstants.TIMESTAMP) {
                return convertObjectToTimestamp(sourceObject);
            } else if ((javaClass == ClassConstants.CALENDAR) || (javaClass == ClassConstants.GREGORIAN_CALENDAR)) {
                return convertObjectToCalendar(sourceObject);
            } else if ((javaClass == ClassConstants.CHAR) || (javaClass == ClassConstants.PCHAR && !(sourceObject instanceof Character))) {
                return convertObjectToChar(sourceObject);
            } else if ((javaClass == ClassConstants.INTEGER) || (javaClass == ClassConstants.PINT && !(sourceObject instanceof Integer))) {
                return convertObjectToInteger(sourceObject);
            } else if ((javaClass == ClassConstants.DOUBLE) || (javaClass == ClassConstants.PDOUBLE && !(sourceObject instanceof Double))) {
                return convertObjectToDouble(sourceObject);
            } else if ((javaClass == ClassConstants.FLOAT) || (javaClass == ClassConstants.PFLOAT && !(sourceObject instanceof Float))) {
                return convertObjectToFloat(sourceObject);
            } else if ((javaClass == ClassConstants.LONG) || (javaClass == ClassConstants.PLONG && !(sourceObject instanceof Long))) {
                return convertObjectToLong(sourceObject);
            } else if ((javaClass == ClassConstants.SHORT) || (javaClass == ClassConstants.PSHORT && !(sourceObject instanceof Short))) {
                return convertObjectToShort(sourceObject);
            } else if ((javaClass == ClassConstants.BYTE) || (javaClass == ClassConstants.PBYTE && !(sourceObject instanceof Byte))) {
                return convertObjectToByte(sourceObject);
            } else if (javaClass == ClassConstants.BIGINTEGER) {
                return convertObjectToBigInteger(sourceObject);
            } else if (javaClass == ClassConstants.BIGDECIMAL) {
                return convertObjectToBigDecimal(sourceObject);
            } else if (javaClass == ClassConstants.NUMBER) {
                return convertObjectToNumber(sourceObject);
            } else if ((javaClass == ClassConstants.BOOLEAN) || (javaClass == ClassConstants.PBOOLEAN && !(sourceObject instanceof Boolean))) {
                return convertObjectToBoolean(sourceObject);
            } else if (BasicTypeHelperImpl.getInstance().isEnumType(javaClass)) {
                return Enum.valueOf((Class<Enum>) javaClass, (String) sourceObject);//TODO
            }
        } catch (ConversionException ce) {
            throw ce;
        } catch (Exception e) {
            throw ConversionException.couldNotBeConverted(sourceObject, javaClass, e);
        }

        // Check if object is instance of the real class for the primitive class.
        if ((((javaClass == ClassConstants.PBOOLEAN) && (sourceObject instanceof Boolean)) ||
                ((javaClass == ClassConstants.PLONG) && (sourceObject instanceof Long)) ||
                ((javaClass == ClassConstants.PINT) && (sourceObject instanceof Integer)) ||
                ((javaClass == ClassConstants.PFLOAT) && (sourceObject instanceof Float)) ||
                ((javaClass == ClassConstants.PDOUBLE) && (sourceObject instanceof Double)) ||
                ((javaClass == ClassConstants.PBYTE) && (sourceObject instanceof Byte)) ||
                ((javaClass == ClassConstants.PCHAR) && (sourceObject instanceof Character)) ||
                ((javaClass == ClassConstants.PSHORT) && (sourceObject instanceof Short)))) {
            return sourceObject;
        }

        // Delay this check as poor performance.
        if (javaClass.isInstance(sourceObject)) {
            return sourceObject;
        }

        throw ConversionException.couldNotBeConverted(sourceObject, javaClass);
    }

    /**
     * Build a valid instance of BigDecimal from the given sourceObject
     *
     * @param sourceObject Valid instance of String, BigInteger, any Number
     */
    protected BigDecimal convertObjectToBigDecimal(Object sourceObject) throws ConversionException {
        BigDecimal bigDecimal = null;

        try {
            if (sourceObject instanceof String) {
                bigDecimal = new BigDecimal((String) sourceObject);
            } else if (sourceObject instanceof BigInteger) {
                bigDecimal = new BigDecimal((BigInteger) sourceObject);
            } else if (sourceObject instanceof Number) {
                // Doubles do not maintain scale, because of this it is 
                // impossible to distinguish between 1 and 1.0.  In order to
                // maintain backwards compatibility both 1 and 1.0 will be 
                // treated as BigDecimal(1).
                String numberString = String.valueOf(sourceObject);
                if (numberString.endsWith(".0") || numberString.contains(".0E+")) {
                    bigDecimal = new BigDecimal(((Number) sourceObject).doubleValue());
                } else {
                    bigDecimal = new BigDecimal(numberString);
                }
            } else {
                throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BIGDECIMAL);
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BIGDECIMAL, exception);
        }
        return bigDecimal;
    }

    /**
     * Build a valid instance of BigInteger from the provided sourceObject.
     *
     * @param sourceObject Valid instance of String, BigDecimal, or any Number
     */
    protected BigInteger convertObjectToBigInteger(Object sourceObject) throws ConversionException {
        BigInteger bigInteger = null;

        try {
            if (sourceObject instanceof BigInteger) {
                bigInteger = (BigInteger) sourceObject;
            } else if (sourceObject instanceof String) {
                bigInteger = new BigInteger((String) sourceObject);
            } else if (sourceObject instanceof BigDecimal) {
                bigInteger = ((BigDecimal) sourceObject).toBigInteger();
            } else if (sourceObject instanceof Number) {
                bigInteger = new BigInteger(String.valueOf(((Number) sourceObject).longValue()));
            } else if (sourceObject instanceof Byte[]) {
                Byte[] objectBytes = (Byte[]) sourceObject;
                byte[] bytes = new byte[objectBytes.length];
                for (int index = 0; index < objectBytes.length; index++) {
                    bytes[index] = objectBytes[index].byteValue();
                }
                bigInteger = new BigInteger(bytes);
            } else if (sourceObject instanceof byte[]) {
                bigInteger = new BigInteger((byte[]) sourceObject);
            } else {
                throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BIGINTEGER);
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BIGINTEGER, exception);
        }

        return bigInteger;
    }

    /**
     * Build a valid instance of Boolean from the source object.
     * 't', 'T', "true", "TRUE", 1,'1'             -> Boolean(true)
     * 'f', 'F', "false", "FALSE", 0 ,'0'        -> Boolean(false)
     */
    protected Boolean convertObjectToBoolean(Object sourceObject) {
        if (sourceObject instanceof Character) {
            switch (Character.toLowerCase(((Character) sourceObject).charValue())) {
                case '1':
                case 't':
                    return Boolean.TRUE;
                case '0':
                case 'f':
                    return Boolean.FALSE;
            }
        }
        if (sourceObject instanceof String) {
            String stringValue = ((String) sourceObject).toLowerCase();
            if (stringValue.equals("t") || stringValue.equals("true") || stringValue.equals("1")) {
                return Boolean.TRUE;
            } else if (stringValue.equals("f") || stringValue.equals("false") || stringValue.equals("0")) {
                return Boolean.FALSE;
            }
        }
        if (sourceObject instanceof Number) {
            int intValue = ((Number) sourceObject).intValue();
            if (intValue != 0) {
                return Boolean.TRUE;
            } else if (intValue == 0) {
                return Boolean.FALSE;
            }
        }
        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BOOLEAN);
    }

    /**
     * Build a valid instance of Byte from the provided sourceObject
     *
     * @param sourceObject Valid instance of String or any Number
     * @caught exception        The Byte(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable byte.
     */
    protected Byte convertObjectToByte(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Byte.valueOf((String) sourceObject);
            }
            if (sourceObject instanceof Number) {
                return Byte.valueOf(((Number) sourceObject).byteValue());
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BYTE, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.BYTE);
    }


    /**
     * Build a valid instance of java.util.Calendar from the given source object.
     *
     * @param sourceObject Valid instance of java.util.Date, String, java.sql.Timestamp, or Long
     */
    protected Calendar convertObjectToCalendar(Object sourceObject) throws ConversionException {
        if (sourceObject instanceof Calendar) {
            return (Calendar) sourceObject;
        } else if (sourceObject instanceof java.util.Date) {
            // PERF: Avoid double conversion for date subclasses.
            return Helper.calendarFromUtilDate((java.util.Date) sourceObject);
        }
        return Helper.calendarFromUtilDate(convertObjectToUtilDate(sourceObject));
    }

    /**
     * Build a valid instance of Character from the provided sourceObject.
     *
     * @param sourceObject Valid instance of String or any Number
     */
    protected Character convertObjectToChar(Object sourceObject) throws ConversionException {
        if (sourceObject instanceof String) {
            return Character.valueOf(((String) sourceObject).charAt(0));
        }

        if (sourceObject instanceof Number) {
            return Character.valueOf((char) ((Number) sourceObject).byteValue());
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.CHAR);
    }


    /**
     * Convert the object to an instance of java.sql.Date.
     *
     * @param sourceObject Object of type java.sql.Timestamp, java.util.Date, String or Long
     */
    protected java.sql.Date convertObjectToDate(Object sourceObject) throws ConversionException {
        java.sql.Date date = null;


        if (sourceObject instanceof java.sql.Date) {
            date = (java.sql.Date) sourceObject;//Helper date is not caught on class check.
        } else if (sourceObject instanceof Timestamp) {
            date = Helper.dateFromTimestamp((Timestamp) sourceObject);
        } else if (sourceObject.getClass() == ClassConstants.UTILDATE) {
            date = Helper.sqlDateFromUtilDate((java.util.Date) sourceObject);
        } else if (sourceObject instanceof Calendar) {
            return Helper.dateFromCalendar((Calendar) sourceObject);
        } else if (sourceObject instanceof String) {
            date = Helper.dateFromString((String) sourceObject);
        } else if (sourceObject instanceof Long) {
            date = Helper.dateFromLong((Long) sourceObject);
        } else {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.SQLDATE);
        }
        return date;
    }

    /**
     * Convert the object to an instance of Double.
     *
     * @param sourceObject Object of type String or Number.
     * @caught exception    The Double(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable double.
     */
    protected Double convertObjectToDouble(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Double.valueOf((String) sourceObject);
            }
            if (sourceObject instanceof Number) {
                return Double.valueOf(((Number) sourceObject).doubleValue());
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.DOUBLE, exception);
        }
        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.DOUBLE);
    }

    /**
     * Build a valid Float instance from a String or another Number instance.
     *
     * @caught exception    The Float(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable Float.
     */
    protected Float convertObjectToFloat(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Float.valueOf((String) sourceObject);
            }
            if (sourceObject instanceof Number) {
                return Float.valueOf(((Number) sourceObject).floatValue());
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.FLOAT, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.FLOAT);
    }

    /**
     * Build a valid Integer instance from a String or another Number instance.
     *
     * @caught exception    The Integer(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable integer.
     */
    protected Integer convertObjectToInteger(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Integer.valueOf((String) sourceObject);
            }

            if (sourceObject instanceof Number) {
                return Integer.valueOf(((Number) sourceObject).intValue());
            }

            if (sourceObject instanceof Boolean) {
                if (((Boolean) sourceObject).booleanValue()) {
                    return Integer.valueOf(1);
                } else {
                    return Integer.valueOf(0);
                }
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.INTEGER, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.INTEGER);
    }

    /**
     * Build a valid Long instance from a String or another Number instance.
     *
     * @caught exception    The Long(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable long.
     */
    protected Long convertObjectToLong(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Long.valueOf((String) sourceObject);
            }
            if (sourceObject instanceof Number) {
                return Long.valueOf(((Number) sourceObject).longValue());
            }
            if (sourceObject instanceof java.util.Date) {
                return Long.valueOf(((java.util.Date) sourceObject).getTime());
            }
            if (sourceObject instanceof Calendar) {
                return Long.valueOf(((Calendar) sourceObject).getTimeInMillis());
            }

            if (sourceObject instanceof Boolean) {
                if (((Boolean) sourceObject).booleanValue()) {
                    return Long.valueOf(1);
                } else {
                    return Long.valueOf(0);
                }
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.LONG, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.LONG);
    }

    /**
     * INTERNAL:
     * Build a valid BigDecimal instance from a String or another
     * Number instance.  BigDecimal is the most general type so is
     * must be returned when an object is converted to a number.
     *
     * @caught exception    The BigDecimal(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable BigDecimal.
     */
    protected BigDecimal convertObjectToNumber(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return new BigDecimal((String) sourceObject);
            }

            if (sourceObject instanceof Number) {
                return new BigDecimal(((Number) sourceObject).doubleValue());
            }

            if (sourceObject instanceof Boolean) {
                if (((Boolean) sourceObject).booleanValue()) {
                    return BigDecimal.valueOf(1);
                } else {
                    return BigDecimal.valueOf(0);
                }
            }
        } catch (NumberFormatException exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.NUMBER, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.NUMBER);
    }

    /**
     * INTERNAL:
     * Build a valid Short instance from a String or another Number instance.
     *
     * @caught exception    The Short(String) constructor throws a
     * NumberFormatException if the String does not contain a
     * parsable short.
     */
    protected Short convertObjectToShort(Object sourceObject) throws ConversionException {
        try {
            if (sourceObject instanceof String) {
                return Short.valueOf((String) sourceObject);
            }

            if (sourceObject instanceof Number) {
                return Short.valueOf(((Number) sourceObject).shortValue());
            }

            if (sourceObject instanceof Boolean) {
                if (((Boolean) sourceObject).booleanValue()) {
                    return Short.valueOf((short) 1);
                } else {
                    return Short.valueOf((short) 0);
                }
            }
        } catch (Exception exception) {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.SHORT, exception);
        }

        throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.SHORT);
    }

    /**
     * INTERNAL:
     * Converts objects to their string representations.  java.util.Date
     * is converted to a timestamp first and then to a string.  An array
     * of bytes is converted to a hex string.
     */
    protected String convertObjectToString(Object sourceObject) throws ConversionException {
        if (sourceObject.getClass() == ClassConstants.UTILDATE) {
            return Helper.printTimestamp(Helper.timestampFromDate((java.util.Date) sourceObject));
        } else if (sourceObject instanceof Calendar) {
            return Helper.printCalendar((Calendar) sourceObject);
        } else if (sourceObject instanceof Timestamp) {
            return Helper.printTimestamp((Timestamp) sourceObject);
        } else if (sourceObject instanceof java.sql.Date) {
            return Helper.printDate((java.sql.Date) sourceObject);
        } else if (sourceObject instanceof Time) {
            return Helper.printTime((Time) sourceObject);
        } else if (sourceObject instanceof char[]) {
            return new String((char[]) sourceObject);
        } else if (sourceObject instanceof Class) {
            return ((Class) sourceObject).getName();
        } else if (sourceObject instanceof Character) {
            return sourceObject.toString();
        }

        return sourceObject.toString();
    }

    /**
     * INTERNAL:
     * Build a valid instance of java.sql.Time from the given source object.
     *
     * @param sourceObject Valid instance of java.sql.Time, String, java.util.Date, java.sql.Timestamp, or Long
     */
    protected Time convertObjectToTime(Object sourceObject) throws ConversionException {
        Time time = null;

        if (sourceObject instanceof Time) {
            return (Time) sourceObject;//Helper timestamp is not caught on class check.
        }

        if (sourceObject instanceof String) {
            time = Helper.timeFromString((String) sourceObject);
        } else if (sourceObject.getClass() == ClassConstants.UTILDATE) {
            time = Helper.timeFromDate((java.util.Date) sourceObject);
        } else if (sourceObject instanceof Timestamp) {
            time = Helper.timeFromTimestamp((Timestamp) sourceObject);
        } else if (sourceObject instanceof Calendar) {
            return Helper.timeFromCalendar((Calendar) sourceObject);
        } else if (sourceObject instanceof Long) {
            time = Helper.timeFromLong((Long) sourceObject);
        } else {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.TIME);
        }
        return time;
    }

    /**
     * INTERNAL:
     * Build a valid instance of java.sql.Timestamp from the given source object.
     *
     * @param sourceObject Valid obejct of class java.sql.Timestamp, String, java.util.Date, or Long
     */
    protected Timestamp convertObjectToTimestamp(Object sourceObject) throws ConversionException {
        Timestamp timestamp = null;

        if (sourceObject instanceof Timestamp) {
            return (Timestamp) sourceObject;// Helper timestamp is not caught on class check.
        }

        if (sourceObject instanceof String) {
            timestamp = Helper.timestampFromString((String) sourceObject);
        } else if (sourceObject instanceof java.util.Date) {// This handles all date and subclasses, sql.Date, sql.Time conversions.
            timestamp = Helper.timestampFromDate((java.util.Date) sourceObject);
        } else if (sourceObject instanceof Calendar) {
            return Helper.timestampFromCalendar((Calendar) sourceObject);
        } else if (sourceObject instanceof Long) {
            timestamp = Helper.timestampFromLong((Long) sourceObject);
        } else {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.TIMESTAMP);
        }
        return timestamp;
    }


    /**
     * INTERNAL:
     * Build a valid instance of java.util.Date from the given source object.
     *
     * @param sourceObject Valid instance of java.util.Date, String, java.sql.Timestamp, or Long
     */
    protected java.util.Date convertObjectToUtilDate(Object sourceObject) throws ConversionException {
        java.util.Date date = null;

        if (sourceObject.getClass() == java.util.Date.class) {
            date = (java.util.Date) sourceObject;//used when converting util.Date to Calendar
        } else if (sourceObject instanceof java.sql.Date) {
            date = Helper.utilDateFromSQLDate((java.sql.Date) sourceObject);
        } else if (sourceObject instanceof Time) {
            date = Helper.utilDateFromTime((Time) sourceObject);
        } else if (sourceObject instanceof String) {
            //date = Helper.utilDateFromTimestamp(Helper.timestampFromString((String) sourceObject));
            try{
                date = new DateConverter(true).parse(sourceObject + "");
            }catch (Exception e){
                date = null;
                e.printStackTrace();
            }
        } else if (sourceObject instanceof Timestamp) {
            date = Helper.utilDateFromTimestamp((Timestamp) sourceObject);
        } else if (sourceObject instanceof Calendar) {
            return ((Calendar) sourceObject).getTime();
        } else if (sourceObject instanceof Long) {
            date = Helper.utilDateFromLong((Long) sourceObject);
        } else if (sourceObject instanceof java.util.Date) {
            date = new java.util.Date(((java.util.Date) sourceObject).getTime());
        } else {
            throw ConversionException.couldNotBeConverted(sourceObject, ClassConstants.UTILDATE);
        }
        return date;
    }


    /**
     * A singleton conversion manager is used to handle generic conversions.
     * This should not be used for conversion under the session context, these must go through the platform.
     * This allows for the singleton to be customized through setting the default to a user defined subclass.
     */
    public static ConversionManager getDefaultManager() {
        if (defaultManager == null) {
            defaultManager = new ConversionManager();
        }
        return defaultManager;
    }


}
