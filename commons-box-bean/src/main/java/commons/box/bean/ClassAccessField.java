package commons.box.bean;


import commons.box.app.AppError;
import commons.box.app.DataName;

/**
 * 可访问字段
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：15/10/21 </p>
 * <p>版权所属：南京恒立信息科技有限公司 </p>
 */
public interface ClassAccessField<C, F> extends DataName {

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
     * 类型
     *
     * @return
     */
    public Class<F> type();

    /**
     * 是否可读
     *
     * @return 是否可读
     */
    public boolean canRead();

    /**
     * 是否可写
     *
     * @return 是否可写
     */
    public boolean canWrite();

    /**
     * 获取值
     *
     * @return
     * @throws AppError
     */
    public F get(C object) throws AppError;

    /**
     * 设置值
     *
     * @param value
     */
    public void set(C object, F value) throws AppError;
}
