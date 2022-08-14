
package com.corvid.genericdto.util;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**

 */
public class Strings {


    public static String unqualify(String name) {
        return unqualify(name, '.');
    }

    public static String unqualify(String name, char sep) {
        return name.substring(name.lastIndexOf(sep) + 1, name.length());
    }

    public static boolean isEmpty(String string) {
        int len;
        if (string == null || (len = string.length()) == 0) {
            return true;
        }

        for (int i = 0; i < len; i++) {
            if ((Character.isWhitespace(string.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static String nullIfEmpty(String string) {
        return isEmpty(string) ? null : string;
    }

    public static String emptyIfNull(String string) {
        return string == null ? "" : string;
    }

    public static String toString(Object component) {
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(component.getClass())
                    .getPropertyDescriptors();
            StringBuilder builder = new StringBuilder();
            for (PropertyDescriptor descriptor : props) {
                builder.append(descriptor.getName())
                        .append('=')
                        .append(descriptor.getReadMethod().invoke(component))
                        .append("; ");
            }
            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String[] split(String strings, String delims) {
        if (strings == null) {
            return new String[0];
        } else {
            StringTokenizer tokens = new StringTokenizer(strings, delims);
            String[] result = new String[tokens.countTokens()];
            int i = 0;
            while (tokens.hasMoreTokens()) {
                result[i++] = tokens.nextToken();
            }
            return result;
        }
    }

    public static String toString(Object... objects) {
        return toString(" ", objects);
    }

    public static String toString(String sep, Object... objects) {
        if (objects.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(sep).append(object);
        }
        return builder.substring(sep.length());
    }

    public static String toClassNameString(String sep, Object... objects) {
        if (objects.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(sep);
            if (object == null) {
                builder.append("null");
            } else {
                builder.append(object.getClass().getName());
            }
        }
        return builder.substring(sep.length());
    }

    public static String toString(String sep, Class... classes) {
        if (classes.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (Class clazz : classes) {
            builder.append(sep).append(clazz.getName());
        }
        return builder.substring(sep.length());
    }

    public static String toString(InputStream in) throws IOException {
        final StringBuilder out = new StringBuilder();
        final byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * ******************************************************************
     */


    public static String dup(CharSequence cs, int num) {
        if (isEmpty(cs) || num <= 0)
            return "";
        StringBuilder sb = new StringBuilder(cs.length() * num);
        for (int i = 0; i < num; i++)
            sb.append(cs);
        return sb.toString();
    }


    public static String dup(char c, int num) {
        if (c == 0 || num < 1)
            return "";
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < num; i++)
            sb.append(c);
        return sb.toString();
    }


    public static String capitalize(CharSequence s) {
        return upperFirst(s);
    }


    public static String lowerFirst(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char c = s.charAt(0);
        if (Character.isLowerCase(c))
            return s.toString();
        return new StringBuilder(len).append(Character.toLowerCase(c))
                .append(s.subSequence(1, len))
                .toString();
    }


    public static String upperFirst(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char c = s.charAt(0);
        if (Character.isUpperCase(c))
            return s.toString();
        return new StringBuilder(len).append(Character.toUpperCase(c))
                .append(s.subSequence(1, len))
                .toString();
    }


    public static boolean equalsIgnoreCase(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    public static boolean equals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }


    public static boolean startsWithChar(String s, char c) {
        return null != s ? (s.length() == 0 ? false : s.charAt(0) == c) : false;
    }

    public static boolean endsWithChar(String s, char c) {
        return null != s ? (s.length() == 0 ? false
                : s.charAt(s.length() - 1) == c)
                : false;
    }

    public static boolean isEmpty(CharSequence cs) {
        return null == cs || cs.length() == 0;
    }

    public static boolean isBlank(CharSequence cs) {
        if (null == cs)
            return true;
        int length = cs.length();
        for (int i = 0; i < length; i++) {
            if (!(Character.isWhitespace(cs.charAt(i))))
                return false;
        }
        return true;
    }

    public static String trim(CharSequence cs) {
        if (null == cs)
            return null;
        int length = cs.length();
        if (length == 0)
            return cs.toString();
        int l = 0;
        int last = length - 1;
        int r = last;
        for (; l < length; l++) {
            if (!Character.isWhitespace(cs.charAt(l)))
                break;
        }
        for (; r > l; r--) {
            if (!Character.isWhitespace(cs.charAt(r)))
                break;
        }
        if (l > r)
            return "";
        else if (l == 0 && r == last)
            return cs.toString();
        return cs.subSequence(l, r + 1).toString();
    }


    public static String brief(String str, int len) {
        if (Strings.isBlank(str) || (str.length() + 3) <= len)
            return str;
        int w = len / 2;
        int l = str.length();
        return str.substring(0, len - w) + " ... " + str.substring(l - w);
    }


    public static String[] splitIgnoreBlank(String s) {
        return Strings.splitIgnoreBlank(s, ",");
    }


    public static String[] splitIgnoreBlank(String s, String regex) {
        if (null == s)
            return null;
        String[] ss = s.split(regex);
        List<String> list = new LinkedList<>();
        for (String st : ss) {
            if (isBlank(st))
                continue;
            list.add(trim(st));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String fillDigit(int d, int width) {
        return Strings.alignRight(String.valueOf(d), width, '0');
    }

    public static String fillHex(int d, int width) {
        return Strings.alignRight(Integer.toHexString(d), width, '0');
    }

    public static String fillBinary(int d, int width) {
        return Strings.alignRight(Integer.toBinaryString(d), width, '0');
    }


    public static String toDigit(int d, int width) {
        return Strings.cutRight(String.valueOf(d), width, '0');
    }

    public static String toHex(int d, int width) {
        return Strings.cutRight(Integer.toHexString(d), width, '0');
    }


    public static String toBinary(int d, int width) {
        return Strings.cutRight(Integer.toBinaryString(d), width, '0');
    }


    public static String cutRight(String s, int width, char c) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == width)
            return s;
        if (len < width)
            return Strings.dup(c, width - len) + s;
        return s.substring(len - width, len);
    }

    public static String cutLeft(String s, int width, char c) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == width)
            return s;
        if (len < width)
            return s + Strings.dup(c, width - len);
        return s.substring(0, width);
    }


    public static String alignRight(Object o, int width, char c) {
        if (null == o)
            return null;
        String s = o.toString();
        int len = s.length();
        if (len >= width)
            return s;
        return new StringBuilder().append(dup(c, width - len))
                .append(s)
                .toString();
    }


    public static String alignLeft(Object o, int width, char c) {
        if (null == o)
            return null;
        String s = o.toString();
        int length = s.length();
        if (length >= width)
            return s;
        return new StringBuilder().append(s)
                .append(dup(c, width - length))
                .toString();
    }


    public static boolean isQuoteByIgnoreBlank(CharSequence cs, char lc, char rc) {
        if (null == cs)
            return false;
        int len = cs.length();
        if (len < 2)
            return false;
        int l = 0;
        int last = len - 1;
        int r = last;
        for (; l < len; l++) {
            if (!Character.isWhitespace(cs.charAt(l)))
                break;
        }
        if (cs.charAt(l) != lc)
            return false;
        for (; r > l; r--) {
            if (!Character.isWhitespace(cs.charAt(r)))
                break;
        }
        return l < r && cs.charAt(r) == rc;
    }

    public static boolean isQuoteBy(CharSequence cs, char lc, char rc) {
        if (null == cs)
            return false;
        int length = cs.length();
        return length > 1 && cs.charAt(0) == lc && cs.charAt(length - 1) == rc;
    }


    public static boolean isQuoteBy(String str, String l, String r) {
        if (null == str || null == l || null == r)
            return false;
        return str.startsWith(l) && str.endsWith(r);
    }


    public static int maxLength(Collection<? extends CharSequence> coll) {
        int re = 0;
        if (null != coll)
            for (CharSequence s : coll)
                if (null != s)
                    re = Math.max(re, s.length());
        return re;
    }


    public static <T extends CharSequence> int maxLength(T[] array) {
        int re = 0;
        if (null != array)
            for (CharSequence s : array)
                if (null != s)
                    re = Math.max(re, s.length());
        return re;
    }

    public static String sNull(Object obj) {
        return sNull(obj, "");
    }


    public static String sNull(Object obj, String def) {
        return obj != null ? obj.toString() : def;
    }


    public static String sBlank(Object obj) {
        return sBlank(obj, "");
    }


    public static String sBlank(Object obj, String def) {
        if (null == obj)
            return def;
        String s = obj.toString();
        return Strings.isBlank(s) ? def : s;
    }


    public static String removeFirst(CharSequence str) {
        if (str == null)
            return null;
        if (str.length() > 1)
            return str.subSequence(1, str.length()).toString();
        return "";
    }


    public static String removeFirst(String str, char c) {
        return (Strings.isEmpty(str) || c != str.charAt(0)) ? str
                : str.substring(1);
    }

    public static boolean isin(String[] ss, String s) {
        if (null == ss || ss.length == 0 || Strings.isBlank(s))
            return false;
        for (String w : ss)
            if (s.equals(w))
                return true;
        return false;
    }


    public static String lowerWord(CharSequence cs, char c) {
        StringBuilder sb = new StringBuilder();
        int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0)
                    sb.append(c);
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    public static String upperWord(CharSequence cs, char c) {
        StringBuilder sb = new StringBuilder();
        int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            if (ch == c) {
                do {
                    i++;
                    if (i >= len)
                        return sb.toString();
                    ch = cs.charAt(i);
                } while (ch == c);
                sb.append(Character.toUpperCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    public static String escapeHtml(CharSequence cs) {
        if (null == cs)
            return null;
        char[] cas = cs.toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cas) {
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&#x27;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String num2hex(int n) {
        String s = Integer.toHexString(n);
        return n <= 15 ? "0" + s : s;
    }

    public static int hex2num(String hex) {
        return Integer.parseInt(hex, 16);
    }

    private Strings(){
        
    }
}


