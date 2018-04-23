package commons.box.bean;

import commons.box.app.AppError;
import commons.box.app.DataName;
import commons.box.app.DataValidator;

import java.util.List;


/**
 * 可访问属性
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：15/10/21 </p>
 * <p>版权所属：南京恒立信息科技有限公司 </p>
 */
public interface ClassAccessProperty<C, P> extends DataName {

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

    public ClassAccessField<C, P> accessField();

    public ClassAccessMethod<C, P> accessGetter();

    public ClassAccessMethod<C, ?> accessSetter();

    /**
     * 类型
     *
     * @return
     */
    public Class<P> type();

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
     * @param object
     * @return
     * @throws AppError
     */
    public P get(C object) throws AppError;

    /**
     * 设置值
     * @param object
     * @param value
     * @throws AppError
     */
    public void set(C object, P value) throws AppError;


    /**
     * 验证规则
     *
     * @return
     */
    public List<DataValidator<?>> getValidators();
}
