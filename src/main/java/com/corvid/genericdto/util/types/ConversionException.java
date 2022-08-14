package com.corvid.genericdto.util.types;

import java.util.Calendar;

/**
 * <P><B>Purpose</B>: Conversion exceptions such as method or class not defined will raise this exception.
 */
@SuppressWarnings("rawtypes")
public class ConversionException extends RuntimeException {
    protected Class classToConvertTo;

    protected transient Object sourceObject;

    public final static int COULD_NOT_BE_CONVERTED = 3001;

    public final static int INCORRECT_DATE_FORMAT = 3003;

    public final static int INCORRECT_TIME_FORMAT = 3004;

    public final static int INCORRECT_TIMESTAMP_FORMAT = 3005;

    public final static int COULD_NOT_BE_CONVERTED_TO_CLASS = 3007;

    public final static int INCORRECT_DATE_TIME_FORMAT = 3008;

    private int errorCode;

    /**
     * INTERNAL:
     * TopLink exceptions should only be thrown by TopLink.
     * This constructor is only for error message scripting.
     */
    protected ConversionException() {
        super();
    }

    /**
     * INTERNAL:
     * TopLink exceptions should only be thrown by TopLink.
     */
    protected ConversionException(String message, Object sourceObject, Class classToConvertTo, Exception exception) {
        super(message, exception);
        setSourceObject(sourceObject);
        setClassToConvertTo(classToConvertTo);
    }


    public static ConversionException couldNotBeConverted(Object object, Class javaClass) {
        Class objectClass = null;
        if (object != null) {
            objectClass = object.getClass();
        }

        String message = "Could not be converted, Object class '" + objectClass.getName() + "' , the Target Class, '" + javaClass.getName() + "' ";
        ConversionException conversionException = new ConversionException(message, object, javaClass, null);
        conversionException.setErrorCode(COULD_NOT_BE_CONVERTED);
        return conversionException;
    }

    public static ConversionException couldNotBeConverted(Object object, Class javaClass, Exception exception) {
        Class objectClass = null;
        if (object != null) {
            objectClass = object.getClass();
        }
        String message = "Could not be converted, Object class '" + objectClass.getName() + "' , the Target Class, '" + javaClass.getName() + "' ";
        ConversionException conversionException = new ConversionException(message, object, javaClass, exception);
        conversionException.setErrorCode(COULD_NOT_BE_CONVERTED);
        return conversionException;
    }


    public static ConversionException incorrectDateFormat(String dateString) {
        String message = "Incorrect date format, '" + dateString + "'";
        ConversionException conversionException = new ConversionException(message, dateString, java.sql.Date.class, null);
        conversionException.setErrorCode(INCORRECT_DATE_FORMAT);
        return conversionException;
    }

    public static ConversionException incorrectTimeFormat(String timeString) {
        String message = "Incorrect time format, '" + timeString + "'";
        ConversionException conversionException = new ConversionException(message, timeString, java.sql.Time.class, null);
        conversionException.setErrorCode(INCORRECT_TIME_FORMAT);
        return conversionException;
    }

    public static ConversionException incorrectTimestampFormat(String timestampString) {
        String message = "Incorrect timestamp format, '" + timestampString + "'";
        ConversionException conversionException = new ConversionException(message, timestampString, java.sql.Timestamp.class, null);
        conversionException.setErrorCode(INCORRECT_TIMESTAMP_FORMAT);
        return conversionException;
    }

    public static ConversionException incorrectDateTimeFormat(String dateTimeString, Class classBeingConvertedTo) {
        String message = "Incorrect data-time format ,'" + dateTimeString + "'";
        ConversionException conversionException = new ConversionException(message, dateTimeString, classBeingConvertedTo, null);
        conversionException.setErrorCode(INCORRECT_DATE_TIME_FORMAT);
        return conversionException;
    }

    public static ConversionException incorrectDateTimeFormat(String dateTimeString) {
        return incorrectDateTimeFormat(dateTimeString, Calendar.class);
    }

    /**
     * PUBLIC:
     * Return the class to convert to.
     */
    public Class getClassToConvertTo() {
        return classToConvertTo;
    }

    /**
     * PUBLIC:
     * Return the object for which the problem was detected.
     */
    public Object getSourceObject() {
        return sourceObject;
    }

    /**
     * INTERNAL:
     * Set the class to convert to.
     */
    public void setClassToConvertTo(Class classToConvertTo) {
        this.classToConvertTo = classToConvertTo;
    }

    /**
     * INTERNAL:
     * Set the object for which the problem was detected.
     */
    public void setSourceObject(Object sourceObject) {
        this.sourceObject = sourceObject;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
