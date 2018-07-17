package commons.box.app.bean;


import commons.box.app.AppError;
import commons.box.app.DataName;

/**
 * 可访问方法
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：南京恒立信息科技有限公司 </p>
 */
public interface ClassAccessMethod<C, M> extends DataName {
    /**
     * 获取可访问类
     *
     * @return
     */
    public ClassAccess<C> parent();

    /**
     * 获取可访问注解
     *
     * @return
     */
    public ClassAccessAnnotation accessAnno();

    /**
     * 获取字段名
     *
     * @return
     */
    public String name();

    /**
     * 参数类型
     *
     * @return
     */
    public Class<?>[] parameterTypes();

    /**
     * 是否可读
     *
     * @return 是否可读
     */
    public boolean canInvoke();

    /**
     * 参数个数
     *
     * @return
     */
    public int getParameterCount();

    /**
     * @return
     */
    public Class<M> getReturnType();

    /**
     * 调用方法 错误使用AppError包围 如果需要获取实际异常 需要使用AppError.real()获取实际发生的异常
     *
     * @param parameters
     * @return
     * @throws AppError
     */
    public M invoke(Object object, Object... parameters) throws AppError;
}
