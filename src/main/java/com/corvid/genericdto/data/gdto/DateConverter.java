package com.corvid.genericdto.data.gdto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mokua,kodero
 *         <p/>
 *         With fallback formats
 */
public class DateConverter {

    private final SimpleDateFormat defaultFormat;

    private final SimpleDateFormat[] acceptableFormats;

    /**
     * Construct a DateConverter with standard formats and lenient set off.
     */
    public DateConverter() {
        this(false);
    }

    /**
     * Construct a DateConverter with standard formats.
     *
     * @param lenient the lenient setting of {@link SimpleDateFormat#setLenient(boolean)}
     * @since 1.3
     */
    public DateConverter(boolean lenient) {
        this("yyyy-MM-dd HH:mm:ss",
                new String[]{
                        "dd-MM-yyyy",
                        "dd/MM/yyyy",
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm",
                        "yyyy-MM-dd HH",
                        "yyyy-MM-dd",
                        "yyyy-MM-dd HH:mm:ss.S a",
                        "yyyy-MM-dd HH:mm:ssz", "yyyy-MM-dd HH:mm:ss z", // JDK 1.3 needs both versions
                        "yyyy-MM-dd HH:mm:ssa",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"},  // backwards compatibility
                lenient);
    }

    /**
     * Construct a DateConverter with lenient set off.
     *
     * @param defaultFormat     the default format
     * @param acceptableFormats fallback formats
     */
    public DateConverter(String defaultFormat, String[] acceptableFormats) {
        this(defaultFormat, acceptableFormats, false);
    }

    /**
     * Construct a DateConverter.
     *
     * @param defaultFormat     the default format
     * @param acceptableFormats fallback formats
     * @param lenient           the lenient setting of {@link SimpleDateFormat#setLenient(boolean)}
     */
    public DateConverter(String defaultFormat, String[] acceptableFormats, boolean lenient) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultFormat);
        simpleDateFormat.setLenient(lenient);
        this.defaultFormat = simpleDateFormat;
        this.acceptableFormats = new SimpleDateFormat[acceptableFormats.length];
        for (int i = 0; i < acceptableFormats.length; i++) {
            SimpleDateFormat format = new SimpleDateFormat(acceptableFormats[i]);
            format.setLenient(lenient);
            this.acceptableFormats[i] = format;
        }
    }

    public Date parse(String str) throws ParseException {
        if(str.contains("T")){
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(str);
        }
        try {
            return defaultFormat.parse(str);
        } catch (ParseException e) {
            for (int i = 0; i < acceptableFormats.length; i++) {
                try {
                    return acceptableFormats[i].parse(str);
                } catch (ParseException e2) {
                    // no worries, let's try the next format.
                }
            }
            // no dateFormats left to try
            throw new ParseException("Cannot parse date " + str, 0);
        }
    }

    public String format(Date obj) {
        return defaultFormat.format(obj);
    }

    public String longDateformat(Date obj) {
        return obj.getTime() + "";
    }
}