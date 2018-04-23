package commons.box.bean.internal;

import commons.box.app.AppError;
import commons.box.app.DataValidator;
import commons.box.bean.*;
import commons.box.util.Collects;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性的实现类
 * <p>
 * 注意注解声明的解析顺序
 * <p>
 * 首先获取本级类定义的值 然后依次按方法字段获取是setter优先,getter其次,field最后,获取值时应该首先查找直接声明的值,然后再获取上级定义的值
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/16 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalClassAccessProperty<T, P> implements ClassAccessProperty<T, P> {
    public final static List<DataValidator<?>> EMPTY_VALIDATORS = Collects.immlist();
    protected final InternalClassAccess<T> parent;
    protected final Class<T> parentType;
    protected final PropertyDescriptor pd;
    protected final ClassAccessField<T, P> accessField;
    protected final ClassAccessMethod<T, P> accessGetter;
    protected final ClassAccessMethod<T, ?> accessSetter;

    protected final String name;
    protected final Class<P> type;
    protected final boolean read;
    protected final boolean write;
    protected final ClassAccessAnnotation annotations;
    protected final List<DataValidator<?>> validators;

    public InternalClassAccessProperty(InternalClassAccess<T> parent, String name, Class<P> type, PropertyDescriptor pd, ClassAccessField<T, P> accessField, ClassAccessMethod<T, P> accessGetter, ClassAccessMethod<T, ?> accessSetter) {
        this.parent = parent;
        this.parentType = (this.parent != null) ? this.parent.type() : null;
        this.pd = pd;
        this.accessField = accessField;
        this.accessGetter = accessGetter;
        this.accessSetter = accessSetter;
        this.name = name;
        this.type = type;

        this.read = (this.accessGetter != null && this.accessGetter.canInvoke()) || (this.accessField != null && this.accessField.canRead());
        this.write = (this.accessSetter != null && this.accessSetter.canInvoke()) || (this.accessField != null && this.accessField.canWrite());

        this.annotations = newAccessAnnotation(this.accessField, this.accessGetter, this.accessSetter);
        this.validators = newDataValidators(this.annotations);
    }

    protected static ClassAccessAnnotation newAccessAnnotation(ClassAccessField<?, ?> accessField, ClassAccessMethod<?, ?> accessGetter, ClassAccessMethod<?, ?> accessSetter) {
        List<Annotation> annoDeclareds = new ArrayList<>();
        List<Annotation> annoAll = new ArrayList<>();

        List<ClassAccessAnnotation> aas = new ArrayList<>();
        if (accessSetter != null && accessSetter.accessAnno() != null) aas.add(accessSetter.accessAnno());
        if (accessGetter != null && accessGetter.accessAnno() != null) aas.add(accessGetter.accessAnno());
        if (accessField != null && accessField.accessAnno() != null) aas.add(accessField.accessAnno());

        for (ClassAccessAnnotation aa : aas) {
            List<Annotation> das = aa.declaredAnnotations();
            if (das != null) for (Annotation a : das) {
                if (a == null) continue;
                if (!annoDeclareds.contains(a)) annoDeclareds.add(a);
                if (!annoAll.contains(a)) annoAll.add(a);
            }
        }

        for (ClassAccessAnnotation aa : aas) {
            List<Annotation> as = aa.annotations();
            if (as != null) for (Annotation a : as) {
                if (a == null) continue;
                if (!annoAll.contains(a)) annoAll.add(a);
            }
        }

        return InternalClassAccessAnnotation.create(annoDeclareds, annoAll);
    }

    protected static List<DataValidator<?>> newDataValidators(ClassAccessAnnotation annotations) {
        if (annotations == null) return EMPTY_VALIDATORS;

        List<DataValidator<?>> validators = new ArrayList<>();
        // TODO 需实现验证器解析逻辑

        return Collects.immlist(validators);
    }

    @Override
    public final ClassAccess<T> parent() {
        return this.parent;
    }

    @Override
    public final ClassAccessAnnotation accessAnno() {
        return this.annotations;
    }

    @Override
    public final ClassAccessField<T, P> accessField() {
        return this.accessField;
    }

    @Override
    public final ClassAccessMethod<T, P> accessGetter() {
        return this.accessGetter;
    }

    @Override
    public final ClassAccessMethod<T, ?> accessSetter() {
        return this.accessSetter;
    }

    public PropertyDescriptor pd() {
        return pd;
    }

    @Override
    public final List<DataValidator<?>> getValidators() {
        return this.validators;
    }

    @Override
    public final String name() {
        return this.name;
    }

    @Override
    public final Class<P> type() {
        return this.type;
    }

    @Override
    public final boolean canRead() {
        return this.read;
    }

    @Override
    public final boolean canWrite() {
        return this.write;
    }

    @Override
    public P get(T object) throws AppError {
        try {
            if (this.accessGetter != null && this.accessGetter.canInvoke()) return this.accessGetter.invoke(object);
            else if (this.accessField != null && this.accessField.canRead()) return this.accessField.get(object);
            else throw AppError.error("调用类 " + this.parentType + " 的属性 " + this.name + " 发生错误 属性不可读取");
        } catch (Throwable e) {
            throw AppError.error("调用类 " + this.parentType + " 的属性 " + this.name + " 获取值发生错误 ", e);
        }
    }

    @Override
    public void set(T object, P value) throws AppError {

        try {
            if (this.accessSetter != null && this.accessSetter.canInvoke()) this.accessSetter.invoke(object, value);
            else if (this.accessField != null && this.accessField.canWrite()) this.accessField.set(object, value);
            else throw AppError.error("调用类 " + this.parentType + " 的属性 " + this.name + " 发生错误 属性不可写入");
        } catch (Throwable e) {
            throw AppError.error("调用类 " + this.parentType + " 的属性 " + this.name + " 设置值发生错误 ", e);
        }
    }
}
