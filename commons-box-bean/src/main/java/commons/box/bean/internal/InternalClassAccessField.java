package commons.box.bean.internal;

import commons.box.app.AppError;
import commons.box.bean.ClassAccess;
import commons.box.bean.ClassAccessAnnotation;
import commons.box.bean.ClassAccessField;
import commons.box.util.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于反射的字段访问
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalClassAccessField<T, F> implements ClassAccessField<T, F> {
    protected final InternalClassAccess<T> parent;
    protected final Class<T> parentType;
    protected final Field field;
    protected final String name;
    protected final Class<F> type;
    protected final boolean read;
    protected final boolean write;
    protected final ClassAccessAnnotation annotations;

    @SuppressWarnings("unchecked")
    public InternalClassAccessField(InternalClassAccess<T> parent, Field field) {
        this.parent = parent;
        this.parentType = (this.parent != null) ? this.parent.type() : null;
        this.field = field;
        this.name = (this.field != null) ? this.field.getName() : "";
        this.type = (this.field != null) ? (Class<F>) this.field.getType() : null;
        this.read = (this.field != null) && Modifier.isPublic(this.field.getModifiers());
        this.write = (this.field != null) && Modifier.isPublic(this.field.getModifiers()) && !Modifier.isFinal(this.field.getModifiers());

        this.annotations = newAccessAnnotation(this.parentType, this.field);
    }

    @SuppressWarnings("Duplicates")
    protected static ClassAccessAnnotation newAccessAnnotation(Class<?> targetType, Field targetField) {
        List<Annotation> annosDeclared = new ArrayList<>();
        List<Annotation> annosAll = new ArrayList<>();

        if (targetType != null && targetField != null) {
            Annotation[] das = targetField.getDeclaredAnnotations();
            if (das != null) for (Annotation a : das) { // 直接声明的注解
                if (a == null) continue;
                annosDeclared.add(a);
                annosAll.add(a);
            }

            // 查找同名且类型匹配的字段 也作为注解可识别字段加入列表
            String fn = targetField.getName();
            Class<?> ft = targetField.getType();
            Class<?> cls = targetType.getSuperclass();
            while (cls != null && !Object.class.equals(cls) && fn != null && ft != null) {
                try {
                    Field f = cls.getField(fn);
                    if (f != null && Types.isType(f.getType(), ft)) {
                        Annotation[] sas = f.getDeclaredAnnotations();
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
    public Class<F> type() {
        return this.type;
    }

    @Override
    public boolean canRead() {
        return this.read;
    }

    @Override
    public boolean canWrite() {
        return this.write;
    }

    @SuppressWarnings("unchecked")
    @Override
    public F get(T object) throws AppError {
        if (object == null || this.field == null || !this.canRead())
            throw AppError.error(this.parentType + " 无法获取字段 " + this.name + " 值");
        try {
            return (F) this.field.get(object);
        } catch (Throwable e) {
            throw AppError.error(this.parentType + " 无法获取字段 " + this.name + " 值");
        }
    }

    @Override
    public void set(T object, F value) throws AppError {
        if (object == null || this.field == null || !this.canWrite())
            throw AppError.error(this.parentType + " 无法设置字段 " + this.name + " 值");
        try {
            this.field.set(object, value);
        } catch (Throwable e) {
            throw AppError.error(this.parentType + " 无法设置字段 " + this.name + " 值");
        }
    }
}
