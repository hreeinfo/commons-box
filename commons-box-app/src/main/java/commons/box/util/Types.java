package commons.box.util;

import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.app.internal.InternalSpringHelper;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * <p>提供基本类的解析机制，根据处理各种基本类型的判断和转换</p>
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Types {
    private static AppLog LOG = Logs.get(Types.class);

    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    public static final Annotation[] EMPTY_ANNO = new Annotation[0];
    public static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];
    public static final Field[] EMPTY_FIELDS = new Field[]{};
    public static final Object[] EMPTY_OBJS = new Object[]{};
    public static final Method[] EMPTY_METHODS = new Method[]{};

    public static final String CLASS_INNER_SEPARATOR = "$$";
    public static final String CLASS_GLIBC_SEPARATOR = "$$";


    /**
     * 判断第一个参数superType是否是第二个参数type的上级类或同一个类
     *
     * @param superType
     * @param type
     * @return
     */
    public static boolean isType(Class<?> superType, Class<?> type) {
        if (type == null || superType == null) return true;
        return superType.isAssignableFrom(type);
    }

    /**
     * 判断第一个参数superType是否是第二个参数type的上级类或同一个类
     *
     * @param superType
     * @param instance
     * @return
     */
    public static boolean isType(Class<?> superType, Object instance) {
        if (instance == null || superType == null) return true;
        if (instance instanceof Class) return superType.isAssignableFrom((Class<?>) instance);
        else return superType.isInstance(instance);
    }


    /**
     * 判断sources的类是否是targets中对应位置类的上级,举个例子:
     * <p>
     * 判断方法是否能够调用时,如果方法参数类型是 Map.class, String.class, Object.class
     * <p>
     * 那么给定的调用参数类型是符合要求的: HashMap.class, String.class, SomeBean.class
     * <p>
     * 此时,此方法返回true
     *
     * @param sources
     * @param targets
     * @return
     */
    @SuppressWarnings("Duplicates")
    public static boolean isTypesMatch(Class<?>[] sources, Class<?>[] targets) {
        if (sources == null) sources = EMPTY_CLASSES;
        int ssize = sources.length;

        if (targets == null) targets = EMPTY_CLASSES;
        int apsize = targets.length;

        if (ssize != apsize) return false;

        if (ssize == 0) return true;
        else for (int i = 0; i < ssize; i++) if (isType(sources[i], targets[i])) return true;

        return false;
    }

    @SuppressWarnings("Duplicates")
    public static boolean isTypesMatch(Class<?>[] sources, Object[] targets) {
        if (sources == null) sources = EMPTY_CLASSES;
        int ssize = sources.length;

        if (targets == null) targets = EMPTY_CLASSES;
        int apsize = targets.length;

        if (ssize != apsize) return false;

        if (ssize == 0) return true;
        else for (int i = 0; i < ssize; i++) if (isType(sources[i], targets[i])) return true;

        return false;
    }


    /**
     * 可编辑的类型
     *
     * @return 类型是否为可编辑的类型 一般是数字、字符串、布尔、日期或byte[]
     */
    public static boolean isEditableType(Class<?> cls) {
        // TODO 需要设置可编辑的类型
        return false;
    }

    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<Boolean> getValueBoolean(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        if (Strs.equalsIgnoreCase(str, "true") || Strs.equalsIgnoreCase(str, "yes") || Strs.equalsIgnoreCase(str, "t") || Strs.equalsIgnoreCase(str, "y") || Strs.equalsIgnoreCase(str, "1"))
            return Optional.of(Boolean.TRUE);
        else if (Strs.equalsIgnoreCase(str, "false") || Strs.equalsIgnoreCase(str, "no") || Strs.equalsIgnoreCase(str, "f") || Strs.equalsIgnoreCase(str, "n") || Strs.equalsIgnoreCase(str, "0"))
            return Optional.of(Boolean.FALSE);
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean getValueBoolean(final String str, boolean def) {
        if (Strs.isBlank(str)) return def;
        if (Strs.equalsIgnoreCase(str, "true") || Strs.equalsIgnoreCase(str, "yes") || Strs.equalsIgnoreCase(str, "t") || Strs.equalsIgnoreCase(str, "y") || Strs.equalsIgnoreCase(str, "1"))
            return true;
        else if (Strs.equalsIgnoreCase(str, "false") || Strs.equalsIgnoreCase(str, "no") || Strs.equalsIgnoreCase(str, "f") || Strs.equalsIgnoreCase(str, "n") || Strs.equalsIgnoreCase(str, "0"))
            return false;
        else return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<Integer> getValueInt(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static int getValueInt(final String str, int def) {
        if (Strs.isBlank(str)) return def;
        try {
            return Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<Long> getValueLong(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(Long.parseLong(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static long getValueLong(final String str, long def) {
        if (Strs.isBlank(str)) return def;
        try {
            return Long.parseLong(str);
        } catch (Exception ignored) {
        }
        return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<Float> getValueFloat(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(Float.parseFloat(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static float getValueFloat(final String str, float def) {
        if (Strs.isBlank(str)) return def;
        try {
            return Float.parseFloat(str);
        } catch (Exception ignored) {
        }
        return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<Double> getValueDouble(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(Double.parseDouble(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static double getValueDouble(final String str, double def) {
        if (Strs.isBlank(str)) return def;
        try {
            return Double.parseDouble(str);
        } catch (Exception ignored) {
        }
        return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<BigDecimal> getValueBigDecimal(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(new BigDecimal(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static BigDecimal getValueBigDecimal(final String str, BigDecimal def) {
        if (Strs.isBlank(str)) return def;
        try {
            return new BigDecimal(str);
        } catch (Exception ignored) {
        }
        return def;
    }


    /**
     * 获取值
     *
     * @param str
     * @return
     */
    @Nonnull
    public static Optional<BigInteger> getValueBigInteger(final String str) {
        if (Strs.isBlank(str)) return Optional.empty();
        try {
            return Optional.of(new BigInteger(str));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param def
     * @return
     */
    public static BigInteger getValueBigInteger(final String str, BigInteger def) {
        if (Strs.isBlank(str)) return def;
        try {
            return new BigInteger(str);
        } catch (Exception ignored) {
        }
        return def;
    }

    /**
     * 获取值
     *
     * @param str
     * @param enumClass
     * @return
     */
    @Nonnull
    public static <T extends Enum<T>> Optional<T> getValueEnum(final String str, final Class<T> enumClass) {
        if (Strs.isBlank(str) || enumClass == null) return Optional.empty();
        for (T en : enumClass.getEnumConstants()) {
            if (en.toString().equalsIgnoreCase(str))
                return Optional.of(en);
        }
        return Optional.empty();
    }

    /**
     * 获取值 空值时为定义的值
     *
     * @param str
     * @param enumClass
     * @param def
     * @return
     */
    public static <T extends Enum<T>> T getValueEnum(final String str, final Class<T> enumClass, T def) {
        if (Strs.isBlank(str) || enumClass == null) return def;
        for (T en : enumClass.getEnumConstants()) {
            if (en.toString().equalsIgnoreCase(str))
                return en;
        }
        return def;
    }


    /**
     * 是否是true 可选值包括:true(b) true(s) t(s) 1.0(d) 1(i) 字符忽略大小写
     *
     * @param object
     * @return
     */
    public static boolean isTrue(Object object) {
        if (object == null) return false;
        else if (object instanceof Boolean) return (Boolean) object;
        else if (object instanceof String) {
            String ot = (String) object;
            switch (ot) {
                case "t":
                    return true;
                case "T":
                    return true;
                case "true":
                    return true;
                case "TRUE":
                    return true;
                case "y":
                    return true;
                case "Y":
                    return true;
                case "yes":
                    return true;
                case "YES":
                    return true;
                case "1":
                    return true;
                default:
                    return false;
            }
        } else if (object instanceof Number) return ((Number) object).intValue() > 0;

        return false;
    }

    /**
     * 是否是false 可选值包括:false(b) false(s) f(s) 0(d) 0(i) 字符忽略大小写
     * <p>
     * 本方法表示的含义是如果目标对象 符合 false 判断标准（包括字符类型、布尔或者数字的 false） 则返回 TRUE
     *
     * @param object
     * @return
     */
    public static boolean isFalse(Object object) {
        if (object == null) return false;
        else if (object instanceof Boolean) return Boolean.FALSE.equals(object);
        else if (object instanceof String) {
            String ot = (String) object;
            switch (ot) {
                case "f":
                    return true;
                case "F":
                    return true;
                case "false":
                    return true;
                case "FALSE":
                    return true;
                case "n":
                    return true;
                case "N":
                    return true;
                case "no":
                    return true;
                case "NO":
                    return true;
                case "0":
                    return true;
                default:
                    return false;
            }
        } else if (object instanceof Number) return ((Number) object).intValue() <= 0;

        return false;
    }

    private static Object convertToType(Object value, Class<?> type) {
        if (value != null && !type.isInstance(value)) {
            // x -> y
            if (value instanceof String) {
                // string -> y
                String s = (String) value;
                if (Short.class.equals(type) || short.class.equals(type)) {
                    // string -> short
                    value = Short.parseShort(s);
                } else if (Integer.class.equals(type) || int.class.equals(type)) {
                    // string -> integer
                    value = Integer.parseInt(s);
                } else if (Long.class.equals(type) || long.class.equals(type)) {
                    // string -> long
                    value = Long.parseLong(s);
                } else if (Float.class.equals(type) || float.class.equals(type)) {
                    // string -> float
                    value = Float.parseFloat(s);
                } else if (Double.class.equals(type) || double.class.equals(type)) {
                    // string -> double
                    value = Double.parseDouble(s);
                }
            }
        }
        return value;
    }


    /**
     * 查找注解
     *
     * @param clazz
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (clazz == null || annotationType == null) return null;
        if (InternalSpringHelper.ENABLED) return InternalSpringHelper.findAnnotation(clazz, annotationType);
        else throw AppError.error("非 Spring 环境，无法调用本方法");
    }

    /**
     * 查找注解 注意此方法仅查询本级所对应的注解 而不会去搜索上级实现
     *
     * @param element
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        if (element == null || annotationType == null) return null;
        if (InternalSpringHelper.ENABLED) return InternalSpringHelper.findAnnotation(element, annotationType);
        else throw AppError.error("非 Spring 环境，无法调用本方法");
    }

    /**
     * 基于基础反射机制的方法获取字段值 字段必须是public的
     * <p>
     * 此方法与bean机制不同的是无bean映射缓存机制, 每次调用均通过反射接口查询
     *
     * @param obj
     * @param name
     * @return
     * @throws AppError
     */
    public static Object getFieldValue(final Object obj, final String name) throws AppError {
        return getFieldValue(obj, false, name);
    }

    /**
     * 基于基础反射机制的方法 获取字段值 如果accessible指定为true 则忽略java安全检查(private或protected等)
     * <p>
     * 此方法与bean机制不同的是无bean映射缓存机制, 每次调用均通过反射接口查询
     *
     * @param obj
     * @param accessible
     * @param name
     * @return
     * @throws AppError
     */
    public static Object getFieldValue(final Object obj, final boolean accessible, final String name) throws AppError {
        if (obj == null) throw AppError.error("对象为空 不能调用");
        Class<?> type = obj.getClass();
        Field f = findField(type, name);
        if (f == null) throw AppError.error(type + " 未找到字段 " + name);
        try {
            if (accessible) makeAccessible(f);
            return f.get(obj);
        } catch (Throwable e) {
            throw AppError.error(type + " 调用字段 " + name + " 发生错误 - " + e.getMessage(), e);
        }
    }

    public static Object invokeMethod(final Object obj, final boolean accessible, final String name, Class<?>[] parameterTypes, Object[] parameters) {
        if (obj == null) throw AppError.error("对象为空 不能调用");
        Class<?> type = obj.getClass();
        Method m = findMethod(type, name, parameterTypes, parameters);
        if (m == null)
            throw AppError.error(type + " 未找到方法 " + name + ((parameterTypes != null) ? (" 参数 " + Arrays.toString(parameterTypes)) : ""));
        try {
            if (accessible) makeAccessible(m);
            return m.invoke(obj, parameters);
        } catch (Throwable e) {
            throw AppError.error(type + " 调用方法 " + name + ((parameterTypes != null) ? (" 参数 " + Arrays.toString(parameterTypes)) : "") + " 发生错误 - " + e.getMessage(), e);
        }
    }

    public static Object invokeMethod(final Object obj, final boolean accessible, final String name, Object[] parameters) {
        return invokeMethod(obj, accessible, name, null, parameters);
    }


    public static Field[] findFields(final Class<?> cls) {
        if (cls == null) return EMPTY_FIELDS;
        final List<Field> allFields = new ArrayList<>();

        Class<?> currentClass = cls;
        while (currentClass != null) {
            try {
                final Field[] declaredFields = currentClass.getDeclaredFields();
                if (declaredFields != null) Collections.addAll(allFields, declaredFields);
            } catch (Throwable ignored) {
                // Field不在当前类定义,继续向上转型
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields.toArray(new Field[allFields.size()]);
    }

    public static Field findField(final Class<?> cls, String name) {
        Class<?> currentClass = cls;
        while (currentClass != null) {
            try {
                final Field[] declaredFields = currentClass.getDeclaredFields();
                for (final Field field : declaredFields) {
                    if (field == null) continue;
                    if (Strs.equals(field.getName(), name)) return field;
                }
            } catch (Throwable ignored) {
                // Field不在当前类定义,继续向上转型
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    /**
     * 返回所有声明的方法
     * <p>
     * 如果onlyPublics=false, 则包含公有 保护或私有方法
     *
     * @param cls
     * @param onlyPublics
     * @param withSuper
     * @return
     */
    public static Method[] findMethods(final Class cls, boolean onlyPublics, boolean withSuper) {
        if (cls == null) return EMPTY_METHODS;

        if (onlyPublics) {
            try {
                if (withSuper) return cls.getMethods();
                else {
                    List<Method> methodList = new ArrayList<>();
                    final Method[] declaredMethods = cls.getDeclaredMethods();
                    for (Method m : declaredMethods)
                        if (m != null && Modifier.isPublic(m.getModifiers())) methodList.add(m);
                    return methodList.toArray(new Method[methodList.size()]);
                }
            } catch (Throwable ignored) {
            }
        } else {
            List<Method> methodList = new ArrayList<>();
            if (withSuper) {
                Class<?> currentClass = cls;
                while (currentClass != null) {
                    try {
                        final Method[] declaredMethods = currentClass.getDeclaredMethods();
                        for (final Method method : declaredMethods) {
                            if (method == null) continue;
                            methodList.add(method);
                        }
                    } catch (Throwable ignored) {
                        // method不在当前类定义,继续向上转型
                    }
                    currentClass = currentClass.getSuperclass();
                }
            } else {
                try {
                    methodList.addAll(Collects.asList(cls.getMethods()));

                    final Method[] declaredMethods = cls.getDeclaredMethods();
                    for (Method m : declaredMethods)
                        if (m != null && !Modifier.isPublic(m.getModifiers())) methodList.add(m);
                } catch (Throwable ignored) {
                }
            }
            return methodList.toArray(new Method[methodList.size()]);
        }
        return EMPTY_METHODS;
    }

    /**
     * 获取所有方法 根据给定方法的名称和参数 获取所有存在定义的方法声明
     * <p>
     * 调用时,如果parameters为空那么使用parameterTypes(直接使用类型匹配方法参数), 如果parameterTypes为空那么使用parameters(根据参数值获取类型)
     * <p>
     * <p>
     * 循环向上转型, 获取对象的DeclaredMethod. 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型。
     * <p>
     * 注意 返回的数组中 第一个元素是本类定义的同名方法 然后依次为上一级所定义的方法
     *
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     */
    public static Method[] findMethods(final Class cls, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) {
        if (cls == null || methodName == null) return EMPTY_METHODS;

        List<Method> methodList = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            try {
                final Method[] declaredMethods = currentClass.getDeclaredMethods();
                for (final Method method : declaredMethods) {
                    if (method == null) continue;
                    if (Strs.equals(method.getName(), methodName)) {
                        if (parameterTypes != null) {
                            if (Types.isTypesMatch(method.getParameterTypes(), parameterTypes)) methodList.add(method);
                        } else if (parameters != null) {
                            if (Types.isTypesMatch(method.getParameterTypes(), parameters)) methodList.add(method);
                        }
                    }
                }
            } catch (Throwable ignored) {
                // method不在当前类定义,继续向上转型
            }
            currentClass = currentClass.getSuperclass();
        }
        return methodList.toArray(new Method[]{});
    }

    /**
     * 与findMethods相同, 本方法仅通过参数类型获取对应方法
     *
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method[] findMethods(final Class cls, final String methodName, final Class<?>[] parameterTypes) {
        return findMethods(cls, methodName, parameterTypes, null);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod. 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型。
     * <p>
     * 调用时,如果parameters为空那么使用parameterTypes(直接使用类型匹配方法参数), 如果parameterTypes为空那么使用parameters(根据参数值获取类型)
     * <p>
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method findMethod(final Class cls, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) {
        if (cls == null || methodName == null) return null;

        Class<?> currentClass = cls;
        while (currentClass != null) {
            try {
                final Method[] declaredMethods = currentClass.getDeclaredMethods();
                for (final Method method : declaredMethods) {
                    if (method == null) continue;
                    if (Strs.equals(method.getName(), methodName)) {
                        if (parameterTypes != null) {
                            if (Types.isTypesMatch(method.getParameterTypes(), parameterTypes)) return method;
                        } else if (parameters != null) {
                            if (Types.isTypesMatch(method.getParameterTypes(), parameters)) return method;
                        }
                    }
                }
            } catch (Throwable ignored) {
                // method不在当前类定义,继续向上转型
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }


    /**
     * 与findMethod相同, 本方法仅通过参数类型获取对应方法
     *
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method findMethod(final Class cls, final String methodName, final Class<?>[] parameterTypes) {
        return findMethod(cls, methodName, parameterTypes, null);
    }


    /**
     * 与findMethod相同, 本方法仅通过参数值获取对应方法
     *
     * @param cls
     * @param methodName
     * @param parameters
     * @return
     */
    public static Method findMethodByParameters(final Class cls, final String methodName, final Object[] parameters) {
        return findMethod(cls, methodName, null, parameters);
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if (method == null) return;
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if (field == null) return;
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg. public UserDao extends HibernateDao&lt;User&gt;
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao&lt;User,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index from the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            LOG.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            LOG.warn("Index: " + index + ", Size from " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOG.warn(clazz.getSimpleName() + " not prop the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取CGLib处理过后的实体的原类.
     */
    public static Class<?> getUserClass(Object instance) {
        if (instance == null) return null;
        Class clazz = (instance instanceof Class) ? (Class) instance : instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CLASS_GLIBC_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }


    /**
     * 获取上级声明,可以设置是否包含接口
     *
     * @return
     */
    public static List<Class<?>> getSupers(Class<?> type, boolean hasInterfaces) {
        if (type == null) return Collects.immlist();
        Set<Class<?>> classes = new LinkedHashSet<>();

        Class<?> sup = type.getSuperclass(); // 获取直接超类
        if (sup != null && !Object.class.equals(sup)) classes.add(sup);

        if (hasInterfaces) {
            Class<?>[] interfaces = type.getInterfaces();
            if (interfaces != null) classes.addAll(Collects.asList(interfaces));
        }

        while (sup != null && !Object.class.equals(sup)) {
            if (!classes.contains(sup)) classes.add(sup);

            if (hasInterfaces) {
                Class<?>[] interfaces = sup.getInterfaces();
                if (interfaces != null) classes.addAll(Collects.asList(interfaces));
            }

            sup = sup.getSuperclass();
        }


        return Collects.immlist(classes);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends HibernateDao&lt;UserDO&gt;
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao&lt;UserDO,Long&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index from the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            LOG.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            LOG.warn("Index: " + index + ", Size from " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOG.warn(clazz.getSimpleName() + " not prop the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取当前的 class loader
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            cl = Types.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }
        return cl;
    }

    /**
     * 对象相等比较 避免了NPE，普通对象直接判断是否相等；数组做深度判断；类型是否相等在java中是指针相等，
     * <p>
     * 不同的classloader载入的类实例无法做相等判断，安全的做法是通过本方法提供的机制，通过类名做判断
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) return true;
        else if (o1 == null || o2 == null) return false;
        else if (o1.equals(o2)) return true;
        else if (o1.getClass().isArray() && o2.getClass().isArray()) return equalsArray(o1, o2);
        else if (o1 instanceof Class && o2 instanceof Class) return equalsType((Class) o1, (Class) o2);
        return false;
    }


    /**
     * 比较两个类是否相等 类型是否相等在java中是指针相等，
     * <p>
     * 不同的classloader载入的类实例无法做相等判断，安全的做法是通过本方法提供的机制，通过类名做判断
     *
     * @param typeA
     * @param typeB
     * @return
     */
    public static boolean equalsType(Class<?> typeA, Class<?> typeB) {
        if (typeA == typeB) return true;
        if (typeA == null || typeB == null) return false;
        return typeA.getName().equals(typeB.getName());
    }

    /**
     * 数组深度比较
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equalsArray(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) return Arrays.equals((Object[]) o1, (Object[]) o2);
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        if (o1 instanceof byte[] && o2 instanceof byte[]) return Arrays.equals((byte[]) o1, (byte[]) o2);
        if (o1 instanceof char[] && o2 instanceof char[]) return Arrays.equals((char[]) o1, (char[]) o2);
        if (o1 instanceof double[] && o2 instanceof double[]) return Arrays.equals((double[]) o1, (double[]) o2);
        if (o1 instanceof float[] && o2 instanceof float[]) return Arrays.equals((float[]) o1, (float[]) o2);
        if (o1 instanceof int[] && o2 instanceof int[]) return Arrays.equals((int[]) o1, (int[]) o2);
        if (o1 instanceof long[] && o2 instanceof long[]) return Arrays.equals((long[]) o1, (long[]) o2);
        if (o1 instanceof short[] && o2 instanceof short[]) return Arrays.equals((short[]) o1, (short[]) o2);
        return false;
    }


    /**
     * Return as hash code for the given object; typically the value of
     * {@code Object#hashCode()}}. If the object is an array,
     * this method will delegate to any of the {@code hashCode}
     * methods for arrays in this class. If the object is {@code null},
     * this method returns 0.
     *
     * @see Object#hashCode()
     * @see #hc(Object[])
     * @see #hc(boolean[])
     * @see #hc(byte[])
     * @see #hc(char[])
     * @see #hc(double[])
     * @see #hc(float[])
     * @see #hc(int[])
     * @see #hc(long[])
     * @see #hc(short[])
     */
    public static int hc(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return hc((Object[]) obj);
            }
            if (obj instanceof boolean[]) {
                return hc((boolean[]) obj);
            }
            if (obj instanceof byte[]) {
                return hc((byte[]) obj);
            }
            if (obj instanceof char[]) {
                return hc((char[]) obj);
            }
            if (obj instanceof double[]) {
                return hc((double[]) obj);
            }
            if (obj instanceof float[]) {
                return hc((float[]) obj);
            }
            if (obj instanceof int[]) {
                return hc((int[]) obj);
            }
            if (obj instanceof long[]) {
                return hc((long[]) obj);
            }
            if (obj instanceof short[]) {
                return hc((short[]) obj);
            }
        }
        return obj.hashCode();
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : array) {
            hash = MULTIPLIER * hash + hc(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : array) {
            hash = MULTIPLIER * hash + hc(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : array) {
            hash = MULTIPLIER * hash + hc(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : array) {
            hash = MULTIPLIER * hash + hc(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : array) {
            hash = MULTIPLIER * hash + hc(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     */
    public static int hc(short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return the same value as {@link Boolean#hashCode()}}.
     *
     * @see Boolean#hashCode()
     */
    public static int hc(boolean bool) {
        return (bool ? 1231 : 1237);
    }

    /**
     * Return the same value as {@link Double#hashCode()}}.
     *
     * @see Double#hashCode()
     */
    public static int hc(double dbl) {
        return hc(Double.doubleToLongBits(dbl));
    }

    /**
     * Return the same value as {@link Float#hashCode()}}.
     *
     * @see Float#hashCode()
     */
    public static int hc(float flt) {
        return Float.floatToIntBits(flt);
    }

    /**
     * Return the same value as {@link Long#hashCode()}}.
     *
     * @see Long#hashCode()
     */
    public static int hc(long lng) {
        return (int) (lng ^ (lng >>> 32));
    }


}
