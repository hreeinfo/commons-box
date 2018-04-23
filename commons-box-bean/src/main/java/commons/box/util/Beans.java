package commons.box.util;

import com.google.common.collect.Ordering;
import commons.box.app.AppError;
import commons.box.bean.AppBean;
import commons.box.bean.AppClass;
import commons.box.bean.BeanOrder;
import commons.box.bean.ClassAccess;

import java.util.Map;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Beans {

    private final static AppBean BEAN_OP = new AppBean(true);
    private final static AppBean BEAN_SAFE_OP = new AppBean(false);

    private Beans() {
    }

    /**
     * 返回Bean操作器
     *
     * @return
     */
    public static AppBean bean() {
        return AppBean.inst();
    }

    @SuppressWarnings("unchecked")
    public static <T> ClassAccess<T> access(Class<T> type) {
        return AppClass.from(type).access();
    }

    @SuppressWarnings("unchecked")
    public static <T> ClassAccess<T> access(T object) {
        Class<T> cls = (object != null) ? (Class<T>) object.getClass() : null;
        return AppClass.from(cls).access();
    }


    /**
     * 返回Bean操作器
     * <p>
     * throwError表示在操作时发生异常是否抛出 如果不抛出则仅记录error日志
     *
     * @param throwError
     * @return
     */
    public static AppBean bean(boolean throwError) {
        return AppBean.inst(throwError);
    }


    /**
     * Bean排序 本排序器空值认为是最小值 支持内联属性
     *
     * @param props
     * @param <T>
     * @return
     */
    public static <T> Ordering<T> orderByProps(String... props) {
        return new BeanOrder<T>(props);
    }

    /**
     * Bean排序 本排序器空值认为是最小值 支持内联属性
     * <p>
     * 注意:本方法在不包含内联属性时,与byProps(String... props)相比速度会略快
     *
     * @param type
     * @param props
     * @param <T>
     * @return
     */
    public static <T> Ordering<T> orderByProps(Class<T> type, String... props) {
        return new BeanOrder<T>(type, props);
    }


    /**
     * 按map排序的Builder
     *
     * @return
     */
    public static BeanOrder.Builder<Map> orderMap() {
        return BeanOrder.Builder.build(false);
    }

    /**
     * 生成Builder  适用于任意类型的bean 按属性进行排序
     *
     * @param <B>
     * @return
     */
    public static <B> BeanOrder.Builder<B> order() {
        return BeanOrder.Builder.build(false);
    }

    /**
     * 生成Builder 适用于指定类型的bean 速度稍快
     *
     * @param type
     * @param <B>
     * @return
     */
    public static <B> BeanOrder.Builder<B> order(Class<B> type) {
        return BeanOrder.Builder.build(type);
    }

    /**
     * 获取属性
     *
     * @param obj
     * @param property
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object obj, String property) throws AppError {
        if (obj == null) return null;
        return bean().get(obj, property);
    }

    /**
     * 设置属性
     *
     * @param obj
     * @param property
     * @param value
     */
    public static void set(Object obj, String property, Object value) throws AppError {
        if (obj == null) return;
        bean().set(obj, property, value);
    }


    /**
     * 获取属性 无异常输出 仅记录日志
     *
     * @param obj
     * @param property
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T sget(Object obj, String property) {
        if (obj == null) return null;
        return bean(false).get(obj, property);
    }

    /**
     * 设置属性 无异常输出 仅记录日志
     *
     * @param obj
     * @param property
     * @param value
     */
    public static void sset(Object obj, String property, Object value) {
        if (obj == null) return;
        bean(false).set(obj, property, value);
    }
}
