package commons.box.app.internal;

import commons.box.app.AppError;
import commons.box.app.bean.ClassAccess;
import commons.box.app.bean.ClassAccessAnnotation;
import commons.box.app.bean.ClassAccessMethod;
import commons.box.util.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalClassAccessMethod<T, M> implements ClassAccessMethod<T, M> {
    protected final InternalClassAccess<T> parent;
    protected final Class<T> parentType;
    protected final Method method;
    protected final String name;
    protected final Class<?>[] parameterTypes;
    protected final int parameterCount;
    protected final Class<M> returnType;
    protected final boolean invokable;
    protected final ClassAccessAnnotation annotations;

    @SuppressWarnings("unchecked")
    protected InternalClassAccessMethod(InternalClassAccess<T> parent, Method method) {
        this.parent = parent;
        this.parentType = (this.parent != null) ? this.parent.type : null;
        this.method = method;
        this.name = (this.method != null) ? this.method.getName() : "";
        this.parameterTypes = (this.method != null) ? this.method.getParameterTypes() : Types.EMPTY_CLASSES;
        this.parameterCount = (this.parameterTypes != null) ? this.parameterTypes.length : 0;
        this.returnType = (this.method != null) ? (Class<M>) this.method.getReturnType() : null;
        this.invokable = (this.method != null) && Modifier.isPublic(this.method.getModifiers());
        this.annotations = newAccessAnnotation(this.parentType, this.method);
    }

    @SuppressWarnings("Duplicates")
    protected static ClassAccessAnnotation newAccessAnnotation(Class<?> targetType, Method targetMethod) {
        List<Annotation> annosDeclared = new ArrayList<>();
        List<Annotation> annosAll = new ArrayList<>();

        if (targetType != null && targetMethod != null) {
            Annotation[] das = targetMethod.getDeclaredAnnotations();
            if (das != null) for (Annotation a : das) { // 直接声明的注解
                if (a == null) continue;
                annosDeclared.add(a);
                annosAll.add(a);
            }

            // 查找同名且类型匹配的字段 也作为注解可识别字段加入列表
            String mn = targetMethod.getName();
            Class<?> mt = targetMethod.getReturnType();
            Class<?>[] mps = targetMethod.getParameterTypes();
            if (mps == null) mps = Types.EMPTY_CLASSES;

            Class<?> cls = targetType.getSuperclass();
            while (cls != null && !Object.class.equals(cls) && mn != null) {
                try {
                    Method m = cls.getMethod(mn, mps);
                    if (m != null && Types.isType(m.getReturnType(), mt)) {
                        Annotation[] sas = m.getDeclaredAnnotations();
                        if (sas != null) for (Annotation aa : sas) {
                            if (aa != null && !annosAll.contains(aa)) annosAll.add(aa);
                        }
                    }
                } catch (Throwable ignored) {
                }
                cls = cls.getSuperclass();
            }

        }

        return InternalClassAccessAnnotation.create(annosDeclared, annosAll);
    }

    @Override
    public ClassAccess<T> parent() {
        return this.parent;
    }

    @Override
    public ClassAccessAnnotation accessAnno() {
        return this.annotations;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<?>[] parameterTypes() {
        return this.parameterTypes != null ? this.parameterTypes : Types.EMPTY_CLASSES;
    }

    @Override
    public boolean canInvoke() {
        return this.invokable;
    }

    @Override
    public int getParameterCount() {
        return this.parameterCount;
    }

    @Override
    public Class<M> getReturnType() {
        return this.returnType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M invoke(Object object, Object... parameters) throws AppError {
        if (object == null || this.method == null || !this.canInvoke())
            throw AppError.error(this.parentType + " 无法调用方法 " + this.name + " 参数 " + Arrays.toString(this.parameterTypes));
        try {
            return (M) this.method.invoke(object, parameters);
        } catch (Throwable e) {
            throw AppError.error(this.parentType + " 无法调用方法 " + this.name + " 参数 " + Arrays.toString(this.parameterTypes));
        }
    }
}

