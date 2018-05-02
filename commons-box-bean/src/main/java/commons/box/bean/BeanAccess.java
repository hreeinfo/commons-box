package commons.box.bean;


import commons.box.app.AppError;
import commons.box.util.Strs;

/**
 * Bean操作,封装了针对metaBean,map和object的统一访问机制
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface BeanAccess {
    public static final Class<?> DEFAULT_TYPE = Object.class;

    /**
     * @param type
     * @param <T>
     * @return
     * @throws AppError
     */
    public <T> T inst(Class<T> type) throws AppError;

    /**
     * 获取属性
     *
     * @param bean
     * @param property
     * @param <T>
     * @param <O>
     * @return
     * @throws AppError
     */
    public <T, O> O prop(T bean, String property) throws AppError;

    /**
     * 设置属性
     *
     * @param bean
     * @param property
     * @param value
     * @param <T>
     * @param <O>
     * @throws AppError
     */
    public <T, O> void prop(T bean, String property, O value) throws AppError;

    /**
     * 获取字段值
     *
     * @param bean
     * @param name
     * @param <T>
     * @param <O>
     * @return
     * @throws AppError
     */
    public <T, O> O field(T bean, String name) throws AppError;

    /**
     * 设置字段值
     *
     * @param bean
     * @param name
     * @param value
     * @param <T>
     * @param <O>
     * @throws AppError
     */
    public <T, O> void field(T bean, String name, O value) throws AppError;

    /**
     * 调用方法
     *
     * @param bean
     * @param method
     * @param args
     * @param <T>
     * @param <R>
     * @return
     * @throws AppError
     */
    public <T, R> R invoke(T bean, String method, Object... args) throws Throwable;

    /**
     * 是否包含属性
     *
     * @param bean
     * @param name
     * @param <T>
     * @return
     */
    public <T> boolean has(T bean, String name);

    /**
     * 是否包含字段
     *
     * @param bean
     * @param name
     * @param <T>
     * @return
     */
    public <T> boolean hasField(T bean, String name);

    /**
     * 是否包含方法
     *
     * @param bean
     * @param method
     * @param args
     * @param <T>
     * @return
     */
    public <T> boolean canInvoke(T bean, String method, Object... args);

    /**
     * 属性名
     * <p>
     * 对于Object返回Bean Property名, 包含通过getter或setter访问的属性和field
     * <p>
     * 对于map返回keys
     *
     * @param bean
     * @param <T>
     * @return
     */
    public <T> String[] props(T bean);

    /**
     * 字段名
     * <p>
     * 对于Object来说此方法只返回可公共访问的Field,不包括通常意义的property(包含getter或setter)
     * <p>
     * 对于map返回keys,与property一致
     *
     * @param bean
     * @param <T>
     * @return
     */
    public <T> String[] fields(T bean);

    /**
     * 获取属性对应的类型 如果属性为空 返回bean当前类型 属性不存在返回Object 注意此方法返回永远不为空
     *
     * @param bean
     * @param prop
     * @param <T>
     * @return
     */
    public <T> Class<?> type(T bean, String prop);

    /**
     * 是否表示本对象的属性 用于type判断过程
     * 以下值表示本对象:
     * 空 空字符串 $ .
     *
     * @param prop
     * @return
     */
    default boolean isThis(String prop) {
        return (Strs.isBlank(prop) || Strs.equals(prop, "$") || Strs.equals(prop, "."));
    }
}
