package commons.box.bean.internal;


import commons.box.app.AppError;
import commons.box.bean.*;
import commons.box.util.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * // TODO
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
@SuppressWarnings("unchecked")
public class InternalBeanObjectAccess implements BeanObjectAccess {
    protected static final String[] EMPTY_STRS = new String[]{};
    public static final InternalBeanObjectAccess INSTANCE = new InternalBeanObjectAccess();

    private InternalBeanObjectAccess() {
    }

    protected <T> ClassAccess<T> access(Class<T> type) throws AppError {
        if (type == null) throw AppError.error("未找到AppClass");
        AppClass<T> ac = (AppClass<T>) AppClass.from(type);
        if (ac == null) throw AppError.error("未找到AppClass [" + type + "]");
        ClassAccess<T> acc = ac.access();
        if (acc == null) throw AppError.error("未找到AccessClass [" + type + "]");
        return acc;
    }

    protected <T> ClassAccess<T> access(T object) throws AppError {
        if (object == null) throw AppError.error("未找到AppClass");
        return (ClassAccess<T>) access(object.getClass());
    }

    @Override
    public <T> T inst2(Class<T> type, Object... args) {
        return access(type).instance(args);
    }

    @Override
    public <T> T inst(Class<T> type) throws AppError {
        return access(type).instance();
    }

    @Override
    public <T, O> O prop(T bean, String property) throws AppError {
        ClassAccessProperty<T, O> ap = access(bean).prop(property);
        if (ap == null) throw AppError.error("未找到类 [" + bean.getClass() + "] 的属性 " + property);
        return ap.get(bean);
    }

    @Override
    public <T, O> void prop(T bean, String property, O value) throws AppError {
        ClassAccessProperty<T, O> ap = access(bean).prop(property);
        if (ap == null) throw AppError.error("未找到类 [" + bean.getClass() + "] 的属性 " + property);
        ap.set(bean, value);
    }

    @Override
    public <T, O> O field(T bean, String name) throws AppError {
        ClassAccessField<T, O> af = access(bean).field(name);
        if (af == null) throw AppError.error("未找到类 [" + bean.getClass() + "] 的字段 " + name);
        return af.get(bean);
    }

    @Override
    public <T, O> void field(T bean, String name, O value) throws AppError {
        ClassAccessField<T, O> af = access(bean).field(name);
        if (af == null) throw AppError.error("未找到类 [" + bean.getClass() + "] 的字段 " + name);
        af.set(bean, value);
    }

    @Override
    public <T, R> R invoke(T bean, String method, Object... args) throws Throwable {
        return access(bean).invoke(bean, method, args);
    }

    @Override
    public <T> boolean has(T bean, String name) {
        try {
            return access(bean).has(name);
        } catch (Throwable ignored) {
        }
        return false;
    }

    @Override
    public <T> boolean hasField(T bean, String name) {
        try {
            return access(bean).hasField(name);
        } catch (Throwable ignored) {
        }
        return false;
    }

    @Override
    public <T> boolean canInvoke(T bean, String method, Object... args) {
        try {
            ClassAccess<T> ac = access(bean);
            if (ac == null) return false;
            ClassAccessMethod<T, ?>[] ams = ac.accessMethods().get(method);
            if (ams == null) return false;
            int size = (args != null) ? args.length : 0;
            for (ClassAccessMethod<T, ?> am : ams) {
                if (am == null) continue;
                Class<?>[] pts = am.parameterTypes();
                if (pts == null) pts = Types.EMPTY_CLASSES;
                if (size == pts.length && Types.isTypesMatch(pts, args)) return am.canInvoke();
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    @Override
    public <T> String[] props(T bean) {
        try {
            List<String> ps = new ArrayList<>();
            ps.addAll(access(bean).accessProperties().keySet());
            return ps.toArray(new String[ps.size()]);
        } catch (Throwable ignored) {
        }
        return EMPTY_STRS;
    }

    @Override
    public <T> String[] fields(T bean) {
        try {
            List<String> ps = new ArrayList<>();
            ps.addAll(access(bean).accessFields().keySet());
            return ps.toArray(new String[ps.size()]);
        } catch (Throwable ignored) {
        }
        return EMPTY_STRS;
    }

    /**
     * 使用AppClass机制来获取下级所属类型
     *
     * @param bean
     * @param property
     * @param <T>
     * @return
     */
    @Override
    public <T> Class<?> type(T bean, String property) {
        ClassAccessProperty<T, ?> ap = access(bean).prop(property);
        if (ap == null) throw AppError.error("未找到类 [" + bean.getClass().getName() + "] 的属性 " + property);
        return ap.type();
    }
}
