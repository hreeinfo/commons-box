package commons.box.app.internal;


import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.app.bean.*;
import commons.box.util.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalClassAccess<T> implements ClassAccess<T> {
    protected static final AppLog LOG = Logs.get(ClassAccess.class);
    protected static final Class<?>[] EMPTY_CLASSES = new Class<?>[]{};

    protected final String accname;
    protected final Class<T> type;
    protected final Constructor<T>[] constructors;
    protected final boolean readable;
    protected final ClassAccessAnnotation accessAnnotation;
    protected final Map<String, ClassAccessField<T, ?>> accessFields;
    protected final Map<String, ClassAccessMethod<T, ?>[]> accessMethods;
    protected final Map<String, ClassAccessProperty<T, ?>> accessProperties;
    protected final Set<String> propertyNames;

    // 其余所用变量
    private final Constructor<T> _constructorDefault;

    @SuppressWarnings("unchecked")
    public InternalClassAccess(String name, Class<T> type) throws AppError {
        if (type == null) throw AppError.error("ClassInfo构造发现空值[type], 必须定义type");
        this.accname = (Strs.isBlank(name)) ? type.getName() : name;
        this.type = type;
        this.constructors = (Constructor<T>[]) this.type.getConstructors();
        this.readable = this.detectAccessClassPublic();
        this.accessAnnotation = this.createAccessAnnotation();
        this.accessFields = Maps.immmap(this.searchAccessFields());
        this.accessMethods = Maps.immmap(this.searchAccessMethods());
        this.accessProperties = Maps.immmap(this.searchAccessProperties());
        this.propertyNames = Collects.immset(this.accessProperties.keySet());

        this._constructorDefault = this.detectAccessConstructDefault();
    }

    public InternalClassAccess(Class<T> type) throws AppError {
        this((type != null) ? type.getName() : null, type);
    }

    protected boolean detectAccessClassPublic() {
        if (!Modifier.isPublic(this.type.getModifiers())) return false;
        if (this.constructors == null || this.constructors.length < 1) return false;
        for (Constructor<T> c : this.constructors) if (c != null && Modifier.isPublic(c.getModifiers())) return true;
        return false;
    }

    protected Constructor<T> detectAccessConstructDefault() {
        if (!this.readable || this.constructors == null) return null;

        for (Constructor<T> c : this.constructors) if (c != null && Modifier.isPublic(c.getModifiers()) && c.getParameterCount() < 1) return c;

        return null;
    }

    protected ClassAccessAnnotation createAccessAnnotation() {
        return InternalClassAccessAnnotation.create(this.type);
    }


    protected final Map<String, ClassAccessField<T, ?>> searchAccessFields() {
        if (this.type == null) return null;

        Map<String, ClassAccessField<T, ?>> fieldMap = new LinkedHashMap<>();
        try {
            Field[] fields = Types.findFields(this.type);
            if (fields != null) for (Field f : fields) {
                if (f == null || !Modifier.isPublic(f.getModifiers())) continue;
                ClassAccessField<T, ?> af = this.newAccessField(f);
                if (af != null && Strs.isNotBlank(af.name())) fieldMap.put(af.name(), af);
            }
        } catch (Throwable e) {
            LOG.warn("类型 " + this.type.getName() + " 无法列出字段", e);
        }

        return fieldMap;
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, ClassAccessMethod<T, ?>[]> searchAccessMethods() {
        if (this.type == null) return null;

        Map<String, Set<ClassAccessMethod<T, ?>>> mmap = new LinkedHashMap<>();

        try {
            Method[] ms = Types.findMethods(this.type, true, true);
            if (ms != null) for (Method m : ms) {
                if (m == null || !Modifier.isPublic(m.getModifiers())) continue;
                String mn = m.getName();
                if (mn == null) continue;

                Set<ClassAccessMethod<T, ?>> mset = mmap.computeIfAbsent(mn, k -> new LinkedHashSet<>());
                ClassAccessMethod<T, ?> am = this.newAccessMethod(m);
                if (am != null) mset.add(am);
            }

            Map<String, ClassAccessMethod<T, ?>[]> mvs = new LinkedHashMap<>();
            for (Map.Entry<String, Set<ClassAccessMethod<T, ?>>> me : mmap.entrySet()) {
                String mn = me.getKey();
                Set<ClassAccessMethod<T, ?>> mas = me.getValue();

                if (mn != null && mas != null) {
                    ClassAccessMethod<T, ?>[] mmv = mas.toArray(new ClassAccessMethod[]{});
                    mvs.put(mn, mmv);
                }
            }
            return mvs;
        } catch (Throwable e) {
            LOG.warn("类型 " + this.type.getName() + " 无法列出方法", e);
        }

        return null;
    }

    protected final Map<String, ClassAccessProperty<T, ?>> searchAccessProperties() {
        if (this.type == null) return null;

        Map<String, ClassAccessProperty<T, ?>> aps = new LinkedHashMap<>();

        try {
            BeanInfo bi = Introspector.getBeanInfo(this.type);
            if (bi == null) return aps;
            PropertyDescriptor[] pds = bi.getPropertyDescriptors(); // 此处使用spring的beanutils以兼容spring bean的特殊机制
            if (pds != null) for (PropertyDescriptor pd : pds) {
                if (pd == null || pd.getName() == null) continue;
                String pn = pd.getName();
                Class<?> pt = pd.getPropertyType();
                Method getter = pd.getReadMethod();
                Method setter = pd.getWriteMethod();
                Field field = Types.findField(this.type, pn);
                if (field == null || !Types.isType(pt, field.getType())) field = null;

                Types.makeAccessible(getter);
                Types.makeAccessible(setter);
                Types.makeAccessible(field);

                ClassAccessProperty<T, ?> ap = this.newAccessProperty(pn, pt, pd, field, getter, setter);
                if (ap != null) aps.put(pn, ap);
            }
        } catch (Throwable e) {
            LOG.warn("类型 " + this.type.getName() + " 无法列出属性", e);
        }

        return aps;
    }

    protected <F> InternalClassAccessField<T, F> newAccessField(Field field) {
        if (field == null) return null;
        return new InternalClassAccessField<>(this, field);
    }

    protected <F> InternalClassAccessMethod<T, F> newAccessMethod(Method method) {
        if (method == null) return null;
        return new InternalClassAccessMethod<>(this, method);
    }

    @SuppressWarnings("unchecked")
    protected <P> InternalClassAccessProperty<T, P> newAccessProperty(String pname, Class<P> ptype, PropertyDescriptor pd, final Field field, final Method getter, final Method setter) {
        if (pname == null || ptype == null) return null;

        final ClassAccessField<T, P> afield = this.newAccessField(field);
        final ClassAccessMethod<T, P> agetter = this.newAccessMethod(getter);
        final ClassAccessMethod<T, ?> asetter = this.newAccessMethod(setter);

        return new InternalClassAccessProperty<>(this, pname, ptype, pd, afield, agetter, asetter);
    }

    @Override
    public final String name() {
        return this.accname;
    }

    @Override
    public final ClassAccessAnnotation accessAnnotation() {
        return this.accessAnnotation;
    }

    @Override
    public final Map<String, ClassAccessField<T, ?>> accessFields() {
        return this.accessFields;
    }

    @Override
    public final Map<String, ClassAccessMethod<T, ?>[]> accessMethods() {
        return this.accessMethods;
    }

    @Override
    public Map<String, ClassAccessProperty<T, ?>> accessProperties() {
        return this.accessProperties;
    }

    @Override
    public final boolean canRead() {
        return this.readable;
    }

    @Override
    public final Class<T> type() {
        return this.type;
    }

    @Override
    public T instance(Object... parameters) throws AppError {
        if (!this.readable) throw AppError.error("类无法生成实例 " + this.name() + " 没有找到对应构造方法适配参数:" + Strs.toString(parameters));
        try {
            if ((parameters == null || parameters.length < 1) && this._constructorDefault != null) return this._constructorDefault.newInstance();
            if (this.constructors != null) for (Constructor<T> con : this.constructors) {
                if (con == null) continue;
                Class<?>[] parameterTypes = con.getParameterTypes();
                if (Types.isTypesMatch(parameterTypes, parameters)) return con.newInstance(parameters);
            }
        } catch (Throwable e) {
            throw AppError.error("类创建实例发生错误 " + this.name() + " 参数:" + Strs.toString(parameters), e);
        }

        throw AppError.error("类无法生成实例 " + this.name() + " 没有找到对应构造方法适配参数:" + Strs.toString(parameters));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <F> ClassAccessField<T, F> field(String name) {
        if (name == null || this.accessFields == null) return null;
        return (ClassAccessField<T, F>) this.accessFields.get(name);
    }


    @SuppressWarnings("unchecked")
    @Override
    public final <P> ClassAccessProperty<T, P> prop(String name) {
        if (name == null || this.accessProperties == null) return null;
        return (ClassAccessProperty<T, P>) this.accessProperties.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <M> ClassAccessMethod<T, M> method(String name, Class<?>... parameterTypes) {
        return this.searchMethodByTypes(name, parameterTypes);
    }

    @SuppressWarnings("unchecked")
    protected final <M> ClassAccessMethod<T, M> searchMethodByTypes(String name, Class<?>... parameterTypes) {
        if (name == null || this.accessMethods == null) return null;
        ClassAccessMethod<T, ?>[] methods = this.accessMethods.get(name);
        if (methods == null) return null;

        if (parameterTypes == null) parameterTypes = Types.EMPTY_CLASSES;
        for (ClassAccessMethod<T, ?> method : methods) {
            if (method == null) continue;
            if (Types.isTypesMatch(method.parameterTypes(), parameterTypes)) return (ClassAccessMethod<T, M>) method;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected final <M> ClassAccessMethod<T, M> searchMethodByParameters(String name, Object... parameters) {
        if (name == null || this.accessMethods == null) return null;
        ClassAccessMethod<T, ?>[] methods = this.accessMethods.get(name);
        if (methods == null) return null;

        for (ClassAccessMethod<T, ?> method : methods) {
            if (method == null) continue;
            if (Types.isTypesMatch(method.parameterTypes(), parameters)) return (ClassAccessMethod<T, M>) method;
        }

        return null;
    }

    @Override
    public final boolean hasField(String name) {
        if (name == null || this.accessFields == null) return false;
        return this.accessFields.containsKey(name);
    }

    @Override
    public final boolean hasMethod(String name, Class<?>... parameterTypes) {
        return this.method(name, parameterTypes) != null;
    }

    @Override
    public final boolean has(String name) {
        if (name == null || this.accessProperties == null) return false;
        return this.accessProperties.containsKey(name);
    }

    @Override
    public final <F> F field(T object, String name) throws AppError {
        if (object == null || name == null || this.accessFields == null) return null;
        ClassAccessField<T, F> field = this.field(name);

        if (field != null && field.canRead()) return field.get(object);

        LOG.debug("类 " + this.name() + " 字段 " + name + " 未找到或不允许访问");
        return null;
    }

    @Override
    public final <F> void field(T object, String name, F value) throws AppError {
        if (object == null || name == null || this.accessFields == null) return;
        ClassAccessField<T, F> field = this.field(name);

        if (field != null && field.canWrite()) field.set(object, value);
        else LOG.debug("类 " + this.name() + " 字段 " + name + " 未找到或不允许访问");
    }

    @Override
    public final <M> M invoke(T object, String name, Object... parameters) throws Throwable {
        return this.invokeByTypes(object, name, null, parameters);
    }

    @Override
    public final <M> M invokeByTypes(T object, String name, Class<?>[] parameterTypes, Object... parameters) throws Throwable {
        if (object == null || name == null || this.accessMethods == null) return null;

        ClassAccessMethod<T, M> method = (parameterTypes == null) ? this.searchMethodByParameters(name, parameters) : this.searchMethodByTypes(name, parameterTypes);

        if (method != null && method.canInvoke()) return method.invoke(parameters);
        else throw AppError.error("类 " + this.name() + " 方法名 " + name + " 参数 " + Strs.toString(parameterTypes) + " 未找到匹配的方法");
    }

    @Override
    public final Set<String> propNames() {
        return this.propertyNames;
    }

    @Override
    public final <P> P get(T object, String name) throws AppError {
        if (object == null || name == null || this.accessProperties == null) return null;
        ClassAccessProperty<T, P> property = this.prop(name);

        if (property != null && property.canRead()) return property.get(object);

        LOG.debug("类 " + this.name() + " 属性 " + name + " 未找到或不允许访问");
        return null;
    }

    @Override
    public final <P> void set(T object, String name, P value) throws AppError {
        if (object == null || name == null || this.accessProperties == null) return;
        ClassAccessProperty<T, P> property = this.prop(name);

        if (property != null && property.canWrite()) property.set(object, value);
        else LOG.debug("类 " + this.name() + " 属性 " + name + " 未找到或不允许访问");
    }


    public static List<Method> findMethods(final Class<?> cls) {
        final List<Method> allMethods = new ArrayList<>();
        if (cls == null) return allMethods;

        Class<?> currentClass = cls;
        currentClass.getMethods();
        while (currentClass != null && currentClass != Object.class) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
            }
            Object.class.getSuperclass();
            currentClass = currentClass.getSuperclass();
        }
        return allMethods;
    }
}
