package commons.box.bean.internal;


import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.bean.BeanMapAccess;
import commons.box.util.Logs;
import commons.box.util.Types;

import java.util.*;

/**
 * 基于map的bean from 注意使用未指定类型的inst方法生成的实例总是非线程安全的
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
@SuppressWarnings("unchecked")
public class InternalBeanMapAccess implements BeanMapAccess {
    private static final AppLog LOG = Logs.get(BeanMapAccess.class);
    public static final InternalBeanMapAccess INSTANCE = new InternalBeanMapAccess();

    private InternalBeanMapAccess() {
    }

    @Override
    public <T> T inst(Class<T> type) throws AppError {
        if (type == null) return (T) inst2();
        else if (Types.equalsType(type, Map.class)) return (T) inst2();
        else if (Types.equalsType(type, HashMap.class)) return (T) inst2(false);
        else if (Types.equalsType(type, LinkedHashMap.class)) return (T) inst2(true);
        else if (Types.equalsType(type, SortedMap.class)) return (T) (new TreeMap());
        else if (Types.equalsType(type, NavigableMap.class)) return (T) (new TreeMap());
        else if (Types.equalsType(type, TreeMap.class)) return (T) (new TreeMap());

        try {
            return type.newInstance();
        } catch (Throwable e) {
            LOG.warn("无法创建给定类型的实例", e);
        }

        return (T) inst2();
    }

    @Override
    public Map<String, Object> inst2(String[] keys) {
        if (keys != null && keys.length > 0) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String k : keys) if (k != null) map.put(k, null);
            return map;
        } else return this.inst2();
    }

    @Override
    public Map<String, Object> inst2(boolean linked) {
        return linked ? new LinkedHashMap<>() : new HashMap<>();
    }

    @Override
    public Map<String, Object> inst2() {
        return inst2(true);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> toMap(Object bean) {
        if ((bean != null) && (bean instanceof Map)) return (Map<String, Object>) bean;
        throw AppError.error("bean无法转为map, 类型不符 :" + bean);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, O> O prop(T bean, String property) throws AppError {
        if (bean == null) return null;
        Map<String, Object> m = toMap(bean);
        return (O) m.get(property);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, O> void prop(T bean, String property, O value) throws AppError {
        if (bean == null) return;
        Map<String, Object> m = toMap(bean);
        m.put(property, value);
    }

    @Override
    public <T, O> O field(T bean, String name) throws AppError {
        return this.prop(bean, name);
    }

    @Override
    public <T, O> void field(T bean, String name, O value) throws AppError {
        this.prop(bean, name, value);
    }

    @Override
    public <T, R> R invoke(T bean, String method, Object... args) throws Throwable {
        throw AppError.error("map不支持方法调用");
    }

    @Override
    public <T> boolean has(T bean, String name) {
        if (bean == null || name == null) return false;
        Map<String, Object> m = toMap(bean);
        return m.containsKey(name);
    }

    @Override
    public <T> boolean hasField(T bean, String name) {
        return this.has(bean, name);
    }

    @Override
    public <T> boolean canInvoke(T bean, String method, Object... args) {
        return false;
    }

    @Override
    public <T> String[] props(T bean) {
        if (bean == null) return null;
        Map<String, Object> m = toMap(bean);
        List<String> ks = new ArrayList<>(m.keySet());
        return ks.toArray(new String[ks.size()]);
    }

    @Override
    public <T> String[] fields(T bean) {
        return this.props(bean);
    }

    @Override
    public <T> Class<?> type(T bean, String prop) {
        if (bean == null) return DEFAULT_TYPE;
        if (this.isThis(prop)) return bean.getClass();
        Object o = this.prop(bean, prop);
        return (o != null) ? o.getClass() : DEFAULT_TYPE;
    }
}
