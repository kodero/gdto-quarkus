package com.corvid.genericdto.util.mirror;

import com.corvid.genericdto.util.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @param <T>
 */
public class Mirror<T> {

    private static class DefaultTypeExtractor implements TypeExtractor {

        @Override
        public Class<?>[] extract(Mirror<?> mirror) {
            Class<?> theType = mirror.getType();
            List<Class<?>> re = new ArrayList<>(5);


            if (theType.isPrimitive()) {
                re.add(mirror.getWrapperClass());
                if (theType != boolean.class && theType != char.class) {
                    re.add(Number.class);
                }
            } else if (mirror.isOf(Calendar.class)) {
                re.add(Calendar.class);
            } else {
                re.add(theType);
                if (mirror.klass.isEnum()) {
                    re.add(Enum.class);
                } else if (mirror.klass.isArray()) {
                    re.add(Array.class);
                } else if (mirror.isStringLike())
                    re.add(CharSequence.class);

                else if (mirror.isNumber()) {
                    re.add(Number.class);
                } else if (mirror.isOf(Map.class)) {
                    re.add(Map.class);
                } else if (mirror.isOf(List.class)) {
                    re.add(List.class);
                    re.add(Collection.class);
                } else if (mirror.isOf(Collection.class)) {
                    re.add(Collection.class);
                }
            }

            if (theType != Object.class)
                re.add(Object.class);

            return re.toArray(new Class<?>[re.size()]);
        }

    }

    private final static DefaultTypeExtractor defaultTypeExtractor = new DefaultTypeExtractor();


    public static <T> Mirror<T> me(Class<T> classOfT) {
        return null == classOfT ? null
                : new Mirror<>(classOfT).setTypeExtractor(defaultTypeExtractor);
    }


    @SuppressWarnings("unchecked")
    public static <T> Mirror<T> me(T obj) {
        if (obj == null)
            return null;
        if (obj instanceof Class<?>)
            return (Mirror<T>) me((Class<?>) obj);
        return (Mirror<T>) me(obj.getClass());
    }


    public static <T> Mirror<T> me(Class<T> classOfT,
                                   TypeExtractor typeExtractor) {
        return null == classOfT ? null
                : new Mirror<T>(classOfT).setTypeExtractor(typeExtractor == null ? (TypeExtractor) defaultTypeExtractor
                : typeExtractor);
    }


    private Class<T> klass;

    private Type type;

    private TypeExtractor typeExtractor;

    /**
     * @param typeExtractor
     * @return Mirror
     */
    public Mirror<T> setTypeExtractor(TypeExtractor typeExtractor) {
        this.typeExtractor = typeExtractor;
        return this;
    }

    private Mirror(Class<T> classOfT) {
        klass = classOfT;
    }


    public Method getGetter(String fieldName) throws NoSuchMethodException {
        String fn = Strings.upperFirst(fieldName);
        String _get = "get" + fn;
        String _is = "is" + fn;
        for (Method method : klass.getMethods()) {
            if (method.getParameterTypes().length != 0)
                continue;
            if (!method.isAccessible())
                method.setAccessible(true);
            if (_get.equals(method.getName()))
                return method;
            if (_is.equals(method.getName())) {
                if (!Mirror.me(method.getReturnType()).isBoolean())
                    throw new NoSuchMethodException();
                return method;
            }
            if (fieldName.equals(method.getName()))
                return method;
        }
        throw new NoSuchMethodException("");

    }


    public Method getGetter(Field field) throws NoSuchMethodException {
        return getGetter(field.getName());
    }


    public Method getSetter(Field field) throws NoSuchMethodException {
        return getSetter(field.getName(), field.getType());
    }

    public Method getSetter(String fieldName, Class<?> paramType)
            throws NoSuchMethodException {
        try {
            String setterName = "set" + Strings.upperFirst(fieldName);
            try {
                return klass.getMethod(setterName, paramType);
            } catch (Throwable e) {
                try {
                    return klass.getMethod(fieldName, paramType);
                } catch (Throwable e1) {
                    Mirror<?> type = Mirror.me(paramType);
                    for (Method method : klass.getMethods()) {
                        if (method.getParameterTypes().length == 1)
                            if (method.getName().equals(setterName)
                                    || method.getName().equals(fieldName)) {
                                if (null == paramType
                                        || type.canCastToDirectly(method.getParameterTypes()[0]))
                                    return method;
                            }
                    }

                    if (!paramType.isPrimitive()) {
                        Class<?> p = unWrapper();
                        if (null != p)
                            return getSetter(fieldName, p);
                    }
                    throw new RuntimeException();
                }
            }
        } catch (Throwable e) {
            throw new NoSuchMethodException();
        }
    }

    public Method[] findSetters(String fieldName) {
        String mName = "set" + Strings.upperFirst(fieldName);
        List<Method> ms = new ArrayList<Method>();
        for (Method m : this.klass.getMethods()) {
            if (!Modifier.isStatic(m.getModifiers())
                    && m.getParameterTypes().length == 1
                    && m.getName().equals(mName))
                ms.add(m);
        }
        return ms.toArray(new Method[ms.size()]);
    }


    public Field getField(String name) throws NoSuchFieldException {
        Class<?> cc = klass;
        while (null != cc && cc != Object.class) {
            try {
                return cc.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                cc = cc.getSuperclass();
            }
        }
        throw new NoSuchFieldException(String.format("Can NOT find field [%s] in class [%s] and it's parents classes",
                name,
                klass.getName()));
    }

    public <AT extends Annotation> Field getField(Class<AT> ann)
            throws NoSuchFieldException {
        for (Field field : this.getFields()) {
            if (field.isAnnotationPresent(ann))
                return field;
        }
        throw new NoSuchFieldException(String.format("Can NOT find field [@%s] in class [%s] and it's parents classes",
                ann.getName(),
                klass.getName()));
    }


    public <AT extends Annotation> Field[] getFields(Class<AT> ann) {
        List<Field> fields = new LinkedList<Field>();
        for (Field f : this.getFields()) {
            if (f.isAnnotationPresent(ann))
                fields.add(f);
        }
        return fields.toArray(new Field[fields.size()]);
    }


    public Field[] getFields() {
        return _getFields(true, false, true, true);
    }

    public Field[] getStaticField(boolean noFinal) {
        return _getFields(false, true, noFinal, true);
    }

    private Field[] _getFields(boolean noStatic,
                               boolean noMember,
                               boolean noFinal,
                               boolean noInner) {
        Class<?> cc = klass;
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        while (null != cc && cc != Object.class) {
            Field[] fs = cc.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                int m = f.getModifiers();
                if (noStatic && Modifier.isStatic(m))
                    continue;
                if (noFinal && Modifier.isFinal(m))
                    continue;
                if (noInner && f.getName().startsWith("this$"))
                    continue;
                if (noMember && !Modifier.isStatic(m))
                    continue;
                if (map.containsKey(fs[i].getName()))
                    continue;

                map.put(fs[i].getName(), fs[i]);
            }
            cc = cc.getSuperclass();
        }
        return map.values().toArray(new Field[map.size()]);
    }


    public <A extends Annotation> A getAnnotation(Class<A> annType) {
        Class<?> cc = klass;
        A ann;
        do {
            ann = cc.getAnnotation(annType);
            cc = cc.getSuperclass();
        } while (null == ann && cc != Object.class);
        return ann;
    }


    public Type[] getGenericsTypes() {
        if (type instanceof ParameterizedType) {
            return getGenericsTypes(type);
        }
        return null;
    }

    public static Type[] getGenericsTypes(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return pt.getActualTypeArguments();
        }
        return null;
    }

    public static Type getGenericsType(Mirror<?> me, Type type) {
        Type[] types = me.getGenericsTypes();
        if (type instanceof TypeVariable && types != null && types.length > 0) {
            Type[] tvs = me.getType().getTypeParameters();
            for (int i = 0; i < tvs.length; i++) {
                if (type.equals(tvs[i])) {
                    type = me.getGenericsType(i);
                    break;
                }
            }
        }
        return type;
    }

    public Type getGenericsType(int index) {
        Type[] ts = getGenericsTypes();
        return ts == null ? null : (ts.length <= index ? null : ts[index]);
    }


    public Method[] getMethods() {
        Class<?> cc = klass;
        List<Method> list = new LinkedList<>();
        while (null != cc && cc != Object.class) {
            Method[] ms = cc.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                list.add(ms[i]);
            }
            cc = cc.getSuperclass();
        }
        return list.toArray(new Method[list.size()]);
    }

    public Method[] getAllDeclaredMethods(Class<?> top) {
        Class<?> cc = klass;
        Map<String, Method> map = new LinkedHashMap<>();
        while (null != cc && cc != Object.class) {
            Method[] fs = cc.getDeclaredMethods();
            for (int i = 0; i < fs.length; i++) {
                String key = fs[i].getName()
                        + Mirror.getParamDescriptor(fs[i].getParameterTypes());
                if (!map.containsKey(key))
                    map.put(key, fs[i]);
            }
            cc = cc.getSuperclass() == top ? null : cc.getSuperclass();
        }
        return map.values().toArray(new Method[map.size()]);
    }


    public Method[] getAllDeclaredMethodsWithoutTop() {
        return getAllDeclaredMethods(Object.class);
    }


    public Method[] getStaticMethods() {
        List<Method> list = new LinkedList<Method>();
        for (Method m : klass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && Modifier.isPublic(m.getModifiers()))
                list.add(m);
        }
        return list.toArray(new Method[list.size()]);
    }

    public void setValue(Object obj, Field field, Object value)
            throws FailToSetValueException {
        if (!field.isAccessible())
            field.setAccessible(true);
        Class<?> ft = field.getType();

        if (null != value) {
            if (!field.getType().isAssignableFrom(value.getClass()))
                value = field.getType().cast(value);
        } else if (ft.isPrimitive()) {
            if (boolean.class == ft) {
                value = false;
            } else if (char.class == ft) {
                value = (char) 0;
            } else {
                value = (byte) 0;
            }
        }
        try {
            this.getSetter(field).invoke(obj, value);
        } catch (Exception e1) {
            try {
                field.set(obj, value);
            } catch (Exception e) {
                throw makeSetValueException(obj.getClass(),
                        field.getName(),
                        value,
                        e);
            }
        }
    }


    public void setValue(Object obj, String fieldName, Object value)
            throws FailToSetValueException {
        if (null == value) {
            try {
                setValue(obj, this.getField(fieldName), null);
            } catch (Exception e1) {
                throw makeSetValueException(obj.getClass(), fieldName, null, e1);
            }
        } else {
            try {
                this.getSetter(fieldName, value.getClass()).invoke(obj, value);
            } catch (Exception e) {
                try {
                    setValue(obj, this.getField(fieldName), value);
                } catch (Exception e1) {
                    throw makeSetValueException(obj.getClass(),
                            fieldName,
                            value,
                            e1);
                }
            }
        }
    }

    private static RuntimeException makeSetValueException(Class<?> type,
                                                          String name,
                                                          Object value,
                                                          Exception e) {
        if (e instanceof FailToSetValueException) {
            return (FailToSetValueException) e;
        }
        return new FailToSetValueException(String.format("Fail to set value [%s] to [%s]->[%s] because '%s'",
                value,
                type.getName(),
                name,
                e.getMessage()),
                e);
    }


    private static RuntimeException makeGetValueException(Class<?> type,
                                                          String name,
                                                          Throwable e) {
        return new FailToGetValueException(String.format("Fail to get value for [%s]->[%s]",
                type.getName(),
                name),
                e);
    }


    public Object getValue(Object obj, Field f) throws FailToGetValueException {
        if (!f.isAccessible())
            f.setAccessible(true);
        try {
            return f.get(obj);
        } catch (Exception e) {
            throw makeGetValueException(obj.getClass(), f.getName(), e);
        }
    }


    public Object getValue(Object obj, String name) throws FailToGetValueException {
        try {
            return this.getGetter(name).invoke(obj);
        } catch (Exception e) {
            try {
                return getValue(obj, getField(name));
            } catch (NoSuchFieldException e1) {
                if (obj != null
                        && obj.getClass().isArray()
                        && "length".equals(name)) {
                    return length(obj);
                }
                throw makeGetValueException(obj.getClass(), name, e);
            }
        }
    }

    public static int length(Object obj) {
        if (null == obj)
            return 0;
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        } else if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).size();
        } else if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).size();
        }
        /*try {
            return (Integer) Mirror.me(obj.getClass()).invoke(obj, "length");
        }
        catch (Exception e) {}*/
        return 1;
    }


    public Class<T> getType() {
        return klass;
    }

    private String _type_id;

    public String getTypeId() {
        if (null == _type_id) {
            if (null != type && type instanceof ParameterizedType) {
                ParameterizedType pmType = (ParameterizedType) type;
                List<Type> list = new ArrayList<Type>(pmType.getActualTypeArguments().length);
                for (Type pmA : pmType.getActualTypeArguments()) {
                    list.add(pmA);
                }
                _type_id = String.format("%s<%s>",
                        klass.getName(),
                        concat(",", list));
            } else {
                _type_id = klass.getName();
            }
            _type_id += "_" + klass.getClassLoader();
        }
        return _type_id;
    }

    public static <T> StringBuilder concat(Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || 0 == objs.length)
            return sb;

        sb.append(objs[0]);
        for (int i = 1; i < objs.length; i++)
            sb.append(c).append(objs[i]);

        return sb;
    }

    public static <T> StringBuilder concat(Object c, Collection<T> coll) {
        StringBuilder sb = new StringBuilder();
        if (null == coll || coll.isEmpty())
            return sb;
        return concat(c, coll.iterator());
    }

    public static <T> StringBuilder concat(Object c, Iterator<T> it) {
        StringBuilder sb = new StringBuilder();
        if (it == null || !it.hasNext())
            return sb;
        sb.append(it.next());
        while (it.hasNext())
            sb.append(c).append(it.next());
        return sb;
    }


    public Type getActuallyType() {
        return type == null ? klass : type;
    }


    public Class<?>[] extractTypes() {
        return typeExtractor.extract(this);
    }


    public Class<?> getWrapperClass() {
        if (!klass.isPrimitive()) {
            if (this.isPrimitiveNumber()
                    || this.is(Boolean.class)
                    || this.is(Character.class))
                return klass;
            throw makeThrow("Class '%s' should be a primitive class",
                    klass.getName());
        }

        if (is(int.class))
            return Integer.class;
        if (is(char.class))
            return Character.class;
        if (is(boolean.class))
            return Boolean.class;
        if (is(long.class))
            return Long.class;
        if (is(float.class))
            return Float.class;
        if (is(byte.class))
            return Byte.class;
        if (is(short.class))
            return Short.class;
        if (is(double.class))
            return Double.class;

        throw makeThrow("Class [%s] has no wrapper class!",
                klass.getName());
    }

    public Class<?> getWrapper() {
        if (klass.isPrimitive())
            return getWrapperClass();
        return klass;
    }


    public Class<?> getOuterClass() {
        if (Modifier.isStatic(klass.getModifiers()))
            return null;
        String name = klass.getName();
        int pos = name.lastIndexOf('$');
        if (pos == -1)
            return null;
        name = name.substring(0, pos);
        try {
            return loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    private static boolean doMatchMethodParamsType(Class<?>[] paramTypes,
                                                   Class<?>[] methodArgTypes) {
        if (paramTypes.length == 0 && methodArgTypes.length == 0)
            return true;
        if (paramTypes.length == methodArgTypes.length) {
            for (int i = 0; i < paramTypes.length; i++)
                if (!Mirror.me(paramTypes[i])
                        .canCastToDirectly((methodArgTypes[i])))
                    return false;
            return true;
        } else if (paramTypes.length + 1 == methodArgTypes.length) {
            if (!methodArgTypes[paramTypes.length].isArray())
                return false;
            for (int i = 0; i < paramTypes.length; i++)
                if (!Mirror.me(paramTypes[i])
                        .canCastToDirectly((methodArgTypes[i])))
                    return false;
            return true;
        }
        return false;
    }


    public Method findMethod(String name, Class<?>... paramTypes)
            throws NoSuchMethodException {
        try {
            return klass.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            for (Method m : klass.getMethods()) {
                if (m.getName().equals(name))
                    if (doMatchMethodParamsType(paramTypes,
                            m.getParameterTypes()))
                        return m;
            }
        }
        throw new NoSuchMethodException();
    }


    public Method[] findMethods(String name, int argNumber) {
        List<Method> methods = new LinkedList<Method>();
        for (Method m : klass.getMethods())
            if (m.getName().equals(name))
                if (argNumber < 0)
                    methods.add(m);
                else if (m.getParameterTypes().length == argNumber)
                    methods.add(m);
        return methods.toArray(new Method[methods.size()]);
    }


    public Method findMethod(Class<?> returnType, Class<?>... paramTypes)
            throws NoSuchMethodException {
        for (Method m : klass.getMethods()) {
            if (returnType == m.getReturnType())
                if (paramTypes.length == m.getParameterTypes().length) {
                    boolean noThisOne = false;
                    for (int i = 0; i < paramTypes.length; i++) {
                        if (paramTypes[i] != m.getParameterTypes()[i]) {
                            noThisOne = true;
                            break;
                        }
                    }
                    if (!noThisOne)
                        return m;
                }
        }
        throw new NoSuchMethodException();

    }


    public static MatchType matchParamTypes(Class<?>[] methodParamTypes,
                                            Object... args) {
        return matchParamTypes(methodParamTypes, evalToTypes(args));
    }


    public static Class<?>[] evalToTypes(Object... args) {
        Class<?>[] types = new Class[args.length];
        int i = 0;
        for (Object arg : args)
            types[i++] = null == arg ? Object.class : arg.getClass();
        return types;
    }

    public static Object evalArgToSameTypeRealArray(Object... args) {
        Object array = evalArgToRealArray(args);
        return array == args ? null : array;
    }


    public static Object evalArgToRealArray(Object... args) {
        if (null == args || args.length == 0 || null == args[0])
            return null;
        Object re = null;
        /*
         * Check inside the arguments list, to see if all element is in same
         * type
         */
        Class<?> type = null;
        for (Object arg : args) {
            if (null == arg)
                break;
            if (null == type) {
                type = arg.getClass();
                continue;
            }
            if (arg.getClass() != type) {
                type = null;
                break;
            }
        }
        /*
         * If all argument elements in same type, make a new Array by the Type
         */
        if (type != null) {
            re = Array.newInstance(type, args.length);
            for (int i = 0; i < args.length; i++) {
                Array.set(re, i, args[i]);
            }
            return re;
        }
        return args;

    }

    public enum MatchType {

        YES,

        LACK,

        NO,

        NEED_CAST
    }

    public static MatchType matchParamTypes(Class<?>[] paramTypes,
                                            Class<?>[] argTypes) {
        int len = argTypes == null ? 0 : argTypes.length;
        if (len == 0 && paramTypes.length == 0)
            return MatchType.YES;
        if (paramTypes.length == len) {
            for (int i = 0; i < len; i++)
                if (!Mirror.me(argTypes[i]).canCastToDirectly((paramTypes[i])))
                    return MatchType.NO;
            return MatchType.YES;
        } else if (len + 1 == paramTypes.length) {
            if (!paramTypes[len].isArray())
                return MatchType.NO;
            for (int i = 0; i < len; i++)
                if (!Mirror.me(argTypes[i]).canCastToDirectly((paramTypes[i])))
                    return MatchType.NO;
            return MatchType.LACK;
        }
        return MatchType.NO;
    }


    public boolean is(Class<?> type) {
        return null != type && klass == type;
    }


    public boolean is(String className) {
        return klass.getName().equals(className);
    }


    public boolean isOf(Class<?> type) {
        return type.isAssignableFrom(klass);
    }


    public boolean isString() {
        return is(String.class);
    }


    public boolean isStringLike() {
        return CharSequence.class.isAssignableFrom(klass);
    }


    public boolean isSimple() {
        return isStringLike()
                || isBoolean()
                || isChar()
                || isNumber()
                || isDateTimeLike();
    }


    public boolean isChar() {
        return is(char.class) || is(Character.class);
    }

    public boolean isEnum() {
        return klass.isEnum();
    }


    public boolean isBoolean() {
        return is(boolean.class) || is(Boolean.class);
    }


    public boolean isFloat() {
        return is(float.class) || is(Float.class);
    }


    public boolean isDouble() {
        return is(double.class) || is(Double.class);
    }


    public boolean isInt() {
        return is(int.class) || is(Integer.class);
    }

    public boolean isLocalTime() {
        return is(LocalTime.class);
    }


    public boolean isIntLike() {
        return isInt()
                || isLong()
                || isShort()
                || isByte()
                || is(BigDecimal.class);
    }

    public boolean isInterface() {
        return klass.isInterface();
    }


    public boolean isDecimal() {
        return isFloat() || isDouble();
    }

    public boolean isLong() {
        return is(long.class) || is(Long.class);
    }


    public boolean isShort() {
        return is(short.class) || is(Short.class);
    }


    public boolean isByte() {
        return is(byte.class) || is(Byte.class);
    }


    public boolean isWrapperOf(Class<?> type) {
        try {
            return Mirror.me(type).getWrapperClass() == klass;
        } catch (Exception e) {
        }
        return false;
    }


    public boolean canCastToDirectly(Class<?> type) {
        if (klass == type || type.isAssignableFrom(klass))
            return true;
        if (klass.isPrimitive() && type.isPrimitive()) {
            if (this.isPrimitiveNumber() && Mirror.me(type).isPrimitiveNumber())
                return true;
        }
        try {
            return Mirror.me(type).getWrapperClass() == this.getWrapperClass();
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isPrimitiveNumber() {
        return isInt()
                || isLong()
                || isFloat()
                || isDouble()
                || isByte()
                || isShort();
    }


    public boolean isObj() {
        return isContainer() || isPojo();
    }


    public boolean isPojo() {
        if (this.klass.isPrimitive() || this.isEnum())
            return false;

        if (this.isStringLike() || this.isDateTimeLike())
            return false;

        if (this.isPrimitiveNumber() || this.isBoolean() || this.isChar())
            return false;

        return !isContainer();
    }


    public boolean isContainer() {
        return isColl() || isMap();
    }


    public boolean isArray() {
        return klass.isArray();
    }


    public boolean isCollection() {
        return isOf(Collection.class);
    }


    public boolean isColl() {
        return isArray() || isCollection();
    }


    public boolean isMap() {
        return isOf(Map.class);
    }


    public boolean isNumber() {
        return Number.class.isAssignableFrom(klass)
                || klass.isPrimitive()
                && !is(boolean.class)
                && !is(char.class);
    }


    public boolean isDateTimeLike() {
        return Calendar.class.isAssignableFrom(klass)
                || Date.class.isAssignableFrom(klass)
                || java.sql.Date.class.isAssignableFrom(klass)
                || java.sql.Time.class.isAssignableFrom(klass);
    }

    public String toString() {
        return klass.getName();
    }


    public static Object blankArrayArg(Class<?>[] pts) {
        return Array.newInstance(pts[pts.length - 1].getComponentType(), 0);
    }


    public static Type[] getTypeParams(Class<?> klass) {
        // TODO
        if (klass == null || "java.lang.Object".equals(klass.getName()))
            return null;

        Type superclass = klass.getGenericSuperclass();
        if (null != superclass && superclass instanceof ParameterizedType)
            return ((ParameterizedType) superclass).getActualTypeArguments();


        Type[] interfaces = klass.getGenericInterfaces();
        for (Type inf : interfaces) {
            if (inf instanceof ParameterizedType) {
                return ((ParameterizedType) inf).getActualTypeArguments();
            }
        }
        return getTypeParams(klass.getSuperclass());
    }

    private static final Pattern PTN = Pattern.compile("(<)(.+)(>)");


    public static Class<?>[] getGenericTypes(Field f) {
        String gts = f.toGenericString();
        Matcher m = PTN.matcher(gts);
        if (m.find()) {
            String s = m.group(2);
            String[] ss = Strings.splitIgnoreBlank(s);
            if (ss.length > 0) {
                Class<?>[] re = new Class<?>[ss.length];
                try {
                    for (int i = 0; i < ss.length; i++) {
                        String className = ss[i];
                        if (className.length() > 0
                                && className.charAt(0) == '?')
                            re[i] = Object.class;
                        else {
                            int pos = className.indexOf('<');
                            if (pos < 0)
                                re[i] = loadClass(className);
                            else
                                re[i] = loadClass(className.substring(0,
                                        pos));
                        }
                    }
                    return re;
                } catch (ClassNotFoundException e) {
                    throw wrapThrow(e);
                }
            }
        }
        return new Class<?>[0];
    }

    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        if (e instanceof InvocationTargetException)
            return wrapThrow(((InvocationTargetException) e).getTargetException());
        return new RuntimeException(e);
    }

    public static Class<?> loadClass(String className)
            throws ClassNotFoundException {
        try {
            return Thread.currentThread()
                    .getContextClassLoader()
                    .loadClass(className);
        } catch (ClassNotFoundException e) {
            return Class.forName(className);
        }
    }

    public static Class<?> getGenericTypes(Field f, int index) {
        Class<?>[] types = getGenericTypes(f);
        if (null == types || types.length <= index)
            return null;
        return types[index];
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTypeParam(Class<?> klass, int index) {
        Type[] types = getTypeParams(klass);
        if (index >= 0 && index < types.length) {
            Type t = types[index];
            Class<T> clazz = (Class<T>) getTypeClass(t);
            if (clazz == null)
                throw makeThrow("Type '%s' is not a Class", t.toString());
            return clazz;
        }
        throw makeThrow("Class type param out of range %d/%d",
                index,
                types.length);
    }

    public static Class<?> getTypeClass(Type type) {
        Class<?> clazz = null;
        if (type instanceof Class<?>) {
            clazz = (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            clazz = (Class<?>) pt.getRawType();
        } else if (type instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) type;
            Class<?> typeClass = getTypeClass(gat.getGenericComponentType());
            return Array.newInstance(typeClass, 0).getClass();
        } else if (type instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) type;
            Type[] ts = tv.getBounds();
            if (ts != null && ts.length > 0)
                return getTypeClass(ts[0]);
        } else if (type instanceof WildcardType) {
            WildcardType wt = (WildcardType) type;
            Type[] t_low = wt.getLowerBounds();
            if (t_low.length > 0)
                return getTypeClass(t_low[0]);
            Type[] t_up = wt.getUpperBounds();
            return getTypeClass(t_up[0]);
        }
        return clazz;
    }

    public static RuntimeException makeThrow(String format, Object... args) {
        return new RuntimeException(String.format(format, args));
    }


    public static String getPath(Class<?> klass) {
        return klass.getName().replace('.', '/');
    }

    public static String getParamDescriptor(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> pt : parameterTypes)
            sb.append(getTypeDescriptor(pt));
        sb.append(')');
        String s = sb.toString();
        return s;
    }

    public static String getMethodDescriptor(Method method) {
        return getParamDescriptor(method.getParameterTypes())
                + getTypeDescriptor(method.getReturnType());
    }


    public static String getConstructorDescriptor(Constructor<?> c) {
        return getParamDescriptor(c.getParameterTypes()) + "V";
    }

    public static String getTypeDescriptor(Class<?> klass) {
        if (klass.isPrimitive()) {
            if (klass == void.class)
                return "V";
            else if (klass == int.class)
                return "I";
            else if (klass == long.class)
                return "J";
            else if (klass == byte.class)
                return "B";
            else if (klass == short.class)
                return "S";
            else if (klass == float.class)
                return "F";
            else if (klass == double.class)
                return "D";
            else if (klass == char.class)
                return "C";
            else
                /* if(klass == boolean.class) */
                return "Z";
        }
        StringBuilder sb = new StringBuilder();
        if (klass.isArray()) {
            return sb.append('[')
                    .append(getTypeDescriptor(klass.getComponentType()))
                    .toString();
        }
        return sb.append('L')
                .append(Mirror.getPath(klass))
                .append(';')
                .toString();
    }

    public static Field findField(Class<?> type, Class<? extends Annotation> ann) {
        Mirror<?> mirror = Mirror.me(type);
        for (Field f : mirror.getFields())
            if (f.isAnnotationPresent(ann))
                return f;
        return null;
    }

    public Class<?> unWrapper() {
        return TypeMapping2.get(klass);
    }

    private static final Map<Class<?>, Class<?>> TypeMapping2 = new HashMap<>();

    static {

        TypeMapping2.put(Short.class, short.class);
        TypeMapping2.put(Integer.class, int.class);
        TypeMapping2.put(Long.class, long.class);
        TypeMapping2.put(Double.class, double.class);
        TypeMapping2.put(Float.class, float.class);
        TypeMapping2.put(Byte.class, byte.class);
        TypeMapping2.put(Character.class, char.class);
        TypeMapping2.put(Boolean.class, boolean.class);
    }
}