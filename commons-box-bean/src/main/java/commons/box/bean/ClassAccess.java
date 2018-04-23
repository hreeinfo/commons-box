package commons.box.bean;

import commons.box.app.AppError;
import commons.box.app.DataName;

import java.util.Map;
import java.util.Set;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/15 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface ClassAccess<T> extends DataName {
    public ClassAccessAnnotation accessAnnotation();

    public Map<String, ClassAccessField<T, ?>> accessFields();

    public Map<String, ClassAccessMethod<T, ?>[]> accessMethods();

    public Map<String, ClassAccessProperty<T, ?>> accessProperties();


    /**
     * 是否可以访问
     *
     * @return
     */
    public boolean canRead();


    /**
     * 获取类名
     *
     * @return
     */
    public String name();

    /**
     * 获取目标类
     *
     * @return
     */
    public Class<T> type();

    /**
     * 创建实例
     *
     * @param parameters
     * @return
     * @throws AppError
     */
    public T instance(Object... parameters) throws AppError;

    /**
     * 字段访问器
     *
     * @param name
     * @return
     */
    public <F> ClassAccessField<T, F> field(String name);

    /**
     * 方法访问器
     *
     * @param name
     * @param parameterTypes
     * @return
     */
    public <M> ClassAccessMethod<T, M> method(String name, Class<?>... parameterTypes);

    /**
     * 属性访问器
     *
     * @param name
     * @return
     */
    public <P> ClassAccessProperty<T, P> prop(String name);


    /**
     * 是否含有属性
     *
     * @param property
     * @return
     */
    public boolean has(String property);

    /**
     * 是否包含字段
     *
     * @param field
     * @return
     */
    public boolean hasField(String field);

    /**
     * 是否包含方法
     *
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public boolean hasMethod(String methodName, Class<?>... parameterTypes);

    /**
     * 获取属性值
     *
     * @param object
     * @param fieldName
     * @return
     * @throws AppError
     */
    public <F> F field(T object, String fieldName) throws AppError;

    /**
     * 设置属性值
     *
     * @param object
     * @param fieldName
     * @param value
     * @throws AppError
     */
    public <F> void field(T object, String fieldName, F value) throws AppError;

    /**
     * 调用方法
     *
     * @param object
     * @param name
     * @param parameters
     * @return
     * @throws AppError
     */
    public <M> M invoke(T object, String name, Object... parameters) throws Throwable;

    /**
     * 调用方法 如果类型未给定根据参数自动判断
     *
     * @param object
     * @param name
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws AppError
     */
    public <M> M invokeByTypes(T object, String name, Class<?>[] parameterTypes, Object... parameters) throws Throwable;

    /**
     * 获取属性
     *
     * @return
     */
    public Set<String> propNames();

    /**
     * 获取属性值
     *
     * @param object
     * @param property
     * @return
     * @throws AppError
     */
    public <P> P get(T object, String property) throws AppError;

    /**
     * 设置属性值
     *
     * @param object
     * @param property
     * @param value
     * @throws AppError
     */
    public <P> void set(T object, String property, P value) throws AppError;
}
