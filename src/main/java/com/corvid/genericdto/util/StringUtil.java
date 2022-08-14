package com.corvid.genericdto.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {

    public static final String NEW_LINE = System.getProperty("line.separator");

    private static final char[] PUNCTUATION
            = new char[]{'&', '\"', '\'', '{', '(', '[', '-', '|', '`',
            '_', '\\', '^', '@', ')', ']', '=', '+', '}', '?', ',', '.', ';', '/', ':', '!', '§',
            '%', '*', '$', '£', '€', '©', '²', '°', '¤'};

    private static final String PATTERN_START = "{";

    private static final String PATTERN_END = "}";

    private static final String TRUNCATED_TEXT_SUFFIX = "...";

    private static final String EMAIL_PATTERN
            = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

    private static final String HOUR_PATTERN = "^([0-1]?[0-9]|2[0-4]):([0-5][0-9])(:[0-5][0-9])?$";

    public static boolean isDefined(String parameter) {
        return (parameter != null && !parameter.trim().isEmpty() && !"null".equalsIgnoreCase(parameter));
    }

    public static boolean isNotDefined(String parameter) {
        return !isDefined(parameter);
    }


    /**
     * <p>Returns either the passed in String, or if the String is
     * {@code not defined}, an empty String ("").</p>
     * <p/>
     * <pre>
     * StringUtil.defaultStringIfNotDefined(null)   = ""
     * StringUtil.defaultStringIfNotDefined("")     = ""
     * StringUtil.defaultStringIfNotDefined("    ") = ""
     * StringUtil.defaultStringIfNotDefined("bat")  = "bat"
     * </pre>
     *
     * @param string the String to check, may be null, blank or filled by spaces
     *               if the input is {@code not defined}, may be null, blank or filled by spaces
     * @return the passed in String, or the default if it was {@code null}
     * @see com.corvid.inventory.util.StringUtil#isNotDefined(String)
     * @see StringUtils#defaultString(String, String)
     */
    public static String defaultStringIfNotDefined(String string) {
        return defaultStringIfNotDefined(string, EMPTY);
    }


    /**
     * <p>Returns either the passed in String, or if the String is
     * {@code not defined}, the value of {@code defaultString}.</p>
     * <p/>
     * <pre>
     * StringUtil.defaultStringIfNotDefined(null, "NULL")   = "NULL"
     * StringUtil.defaultStringIfNotDefined("", "NULL")     = "NULL"
     * StringUtil.defaultStringIfNotDefined("    ", "NULL") = "NULL"
     * StringUtil.defaultStringIfNotDefined("bat", "NULL")  = "bat"
     * </pre>
     *
     * @param string        the String to check, may be null, blank or filled by spaces
     * @param defaultString the default String to return
     *                      if the input is {@code not defined}, may be null, blank or filled by spaces
     * @return the passed in String, or the default if it was {@code null}
     * @see com.corvid.inventory.util.StringUtil#isNotDefined(String)
     * @see StringUtils#defaultString(String, String)
     */
    public static String defaultStringIfNotDefined(String string, String defaultString) {
        return defaultString((isDefined(string) ? string : null), defaultString);
    }

    public static boolean isInteger(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String id) {
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static float convertFloat(String value) {
        if (StringUtil.isFloat(value)) {
            return Float.valueOf(value);
        } else if (value != null) {
            String charge = value.replace(',', '.');
            if (StringUtil.isFloat(charge)) {
                return Float.valueOf(charge);
            }
        }
        return 0f;
    }

    public static boolean isFloat(String id) {
        try {
            Float.parseFloat(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param text
     * @return a String with all quotes replaced by spaces
     */
    public static String escapeQuote(String text) {
        return text.replace("'", " ");
    }

    /**
     * Replaces
     *
     * @param name
     * @return a String with all quotes replaced by spaces
     */
    public static String toAcceptableFilename(String name) {
        String fileName = name;
        fileName = fileName.replace('\\', '_');
        fileName = fileName.replace('/', '_');
        fileName = fileName.replace('$', '_');
        fileName = fileName.replace('%', '_');
        fileName = fileName.replace('?', '_');
        return fileName;
    }

    /**
     * Format a string by extending the principle of the the method format() of the class
     * java.text.MessageFormat to string arguments. For instance, the string '{key}' contained in the
     * original string to format will be replaced by the value corresponding to this key contained
     * into the values map.
     *
     * @param label  The string to format
     * @param values The values to insert into the string
     * @return The formatted string, filled with values of the map.
     */
    public static String format(String label, Map<String, ?> values) {
        StringBuilder sb = new StringBuilder();
        int startIndex = label.indexOf(PATTERN_START);
        int endIndex;
        String patternKey;
        Object value;
        while (startIndex != -1) {
            endIndex = label.indexOf(PATTERN_END, startIndex);
            if (endIndex != -1) {
                patternKey = label.substring(startIndex + 1, endIndex);
                if (values.containsKey(patternKey)) {
                    value = values.get(patternKey);
                    sb.append(label.substring(0, startIndex)).append(
                            value != null ? value.toString() : "");
                } else {
                    sb.append(label.substring(0, endIndex + 1));
                }
                label = label.substring(endIndex + 1);
                startIndex = label.indexOf(PATTERN_START);
            } else {
                sb.append(label);
                label = "";
                startIndex = -1;
            }
        }
        sb.append(label);
        return sb.toString();
    }

    /**
     * @param text      The string to truncate if its size is greater than the maximum length given as
     *                  parameter.
     * @param maxLength The maximum length required.
     * @return The truncated string followed by '...' if needed. Returns the string itself if its
     *         length is smaller than the required maximum length.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        } else if (maxLength <= 3) {
            return TRUNCATED_TEXT_SUFFIX;
        } else {
            return text.substring(0, maxLength - 3) + TRUNCATED_TEXT_SUFFIX;
        }
    }

    /**
     * Replace parts of a text by an one replacement string. The text to replace is specified by a
     * regex.
     *
     * @param source      the original text
     * @param regex       the regex that permits to identify parts of text to replace
     * @param replacement the replacement text
     * @return The source text modified
     */
    public static String regexReplace(String source, String regex, String replacement) {
        if (StringUtil.isNotDefined(source) || StringUtil.isNotDefined(regex)) {
            return source;
        }
        String replacementText = StringUtil.isDefined(replacement) ? replacement : "";
        return source.replaceAll(regex, replacementText);
    }

    /**
     * Validate the form of an email address. <P> Return <tt>true</tt> only if <ul> <li>
     * <tt>aEmailAddress</tt> can successfully construct an
     * {@link InternetAddress} <li>when parsed with "
     */
    public static boolean isValidEmailAddress(String aEmailAddress) {
        if (aEmailAddress == null) {
            return false;
        }
        boolean result;
        try {
            new InternetAddress(aEmailAddress);
            result = Pattern.matches(EMAIL_PATTERN, aEmailAddress);
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static boolean isValidHour(final String time) {
        if (!isDefined(time)) {
            return false;
        }
        return Pattern.matches(HOUR_PATTERN, time);
    }

    public static String convertToEncoding(String toConvert, String encoding) {
        try {
            return new String(toConvert.getBytes(Charset.defaultCharset()), encoding);
        } catch (UnsupportedEncodingException ex) {
            return toConvert;
        }
    }

    /**
     * Evaluate the expression and return true if expression equals "true", "yes", "y", "1" or "oui".
     *
     * @param expression the expression to be evaluated
     * @return true if expression equals "true", "yes", "y", "1" or "oui".
     */
    public static boolean getBooleanValue(final String expression) {
        return "true".equalsIgnoreCase(expression) || "yes".equalsIgnoreCase(expression)
                || "y".equalsIgnoreCase(expression) || "oui".equalsIgnoreCase(expression)
                || "1".equalsIgnoreCase(expression);
    }

    /**
     * Method for trying to detect encoding
     *
     * @param data             some data to try to detect the encoding.
     * @param declaredEncoding expected encoding.
     * @return
     */
    public static String detectEncoding(byte[] data, String declaredEncoding) {
        CharsetDetector detector = new CharsetDetector();
        if (!StringUtil.isDefined(declaredEncoding)) {
            detector.setDeclaredEncoding(CharEncoding.ISO_8859_1);
        } else {
            detector.setDeclaredEncoding(declaredEncoding);
        }
        detector.setText(data);
        CharsetMatch detectedEnc = detector.detect();
        return detectedEnc.getName();
    }

    /**
     * Method for trying to detect encoding
     *
     * @param data             some data to try to detect the encoding.
     * @param declaredEncoding expected encoding.
     * @return
     */
    public static String detectStringEncoding(byte[] data, String declaredEncoding) throws
            UnsupportedEncodingException {
        if (data != null) {
            String value = new String(data, declaredEncoding);
            if (!checkEncoding(value)) {
                Set<String> supportedEncodings;
                if (CharEncoding.UTF_8.equals(declaredEncoding)) {
                    supportedEncodings = StringUtil.detectMaybeEncoding(data, CharEncoding.ISO_8859_1);
                } else {
                    supportedEncodings = StringUtil.detectMaybeEncoding(data, CharEncoding.UTF_8);
                }
                return reencode(data, supportedEncodings, declaredEncoding);
            }
        }
        return declaredEncoding;
    }

    private static boolean checkEncoding(String value) {
        if (value != null) {
            char[] chars = value.toCharArray();
            for (char currentChar : chars) {
                if (!Character.isLetterOrDigit(currentChar) && !Character.isWhitespace(currentChar)
                        && !ArrayUtils.contains(PUNCTUATION, currentChar)) {
                    return false;
                }
            }
        }
        return true;

    }

    private static String reencode(byte[] data, Set<String> encodings, String declaredEncoding) throws
            UnsupportedEncodingException {
        if (!encodings.isEmpty()) {
            String encoding = encodings.iterator().next();
            String value = new String(data, encoding);
            if (!checkEncoding(value)) {
                encodings.remove(encoding);
                return reencode(data, encodings, declaredEncoding);
            }
            return encoding;
        }
        return declaredEncoding;
    }

    /**
     * Method for trying to detect encoding
     *
     * @param data             some data to try to detect the encoding.
     * @param declaredEncoding expected encoding.
     * @return
     */
    public static Set<String> detectMaybeEncoding(byte[] data, String declaredEncoding) {
        CharsetDetector detector = new CharsetDetector();
        if (!StringUtil.isDefined(declaredEncoding)) {
            detector.setDeclaredEncoding(CharEncoding.ISO_8859_1);
        } else {
            detector.setDeclaredEncoding(declaredEncoding);
        }
        detector.setText(data);
        CharsetMatch[] detectedEnc = detector.detectAll();
        Set<String> encodings = new LinkedHashSet<>(detectedEnc.length);
        for (CharsetMatch detectedEncoding : detectedEnc) {
            encodings.add(detectedEncoding.getName());
        }
        return encodings;
    }

    /**
     * Indicates if two Strings are equals, managing null.
     *
     * @param s1 the first String.
     * @param s2 the second String.
     * @return true ifthe two Strings are equals.
     */
    public static boolean areStringEquals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }


    private static final String SINGLE_QUOTE = "\'";

    private static final String DOUBLE_QUOTE = "\"";

    /**
     * Put quotes around the given String if necessary.
     * <p>
     * If the argument doesn't include spaces or quotes, return it as is. If it
     * contains double quotes, use single quotes - else surround the argument by
     * double quotes.
     * </p>
     *
     * @param argument the argument to be quoted
     * @return the quoted argument
     * @throws IllegalArgumentException If argument contains both types of quotes
     */
    public static String quoteArgument(final String argument) {
        String cleanedArgument = argument.trim();
        while (cleanedArgument.startsWith(SINGLE_QUOTE) || cleanedArgument.startsWith(DOUBLE_QUOTE)) {
            cleanedArgument = cleanedArgument.substring(1);
        }
        while (cleanedArgument.endsWith(SINGLE_QUOTE) || cleanedArgument.endsWith(DOUBLE_QUOTE)) {
            cleanedArgument = cleanedArgument.substring(0, cleanedArgument.length() - 1);
        }
        final StringBuilder buf = new StringBuilder();
        if (cleanedArgument.indexOf(DOUBLE_QUOTE) > -1) {
            if (cleanedArgument.indexOf(SINGLE_QUOTE) > -1) {
                throw new IllegalArgumentException(
                        "Can't handle single and double quotes in same argument");
            } else {
                return buf.append(SINGLE_QUOTE).append(cleanedArgument).append(
                        SINGLE_QUOTE).toString();
            }
        } else if (cleanedArgument.indexOf(SINGLE_QUOTE) > -1
                || cleanedArgument.indexOf(" ") > -1) {
            return buf.append(DOUBLE_QUOTE).append(cleanedArgument).append(
                    DOUBLE_QUOTE).toString();
        } else {
            return cleanedArgument;
        }
    }


    private StringUtil() {
    }
}