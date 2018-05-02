package commons.box.app;


/**
 * 数据验证器
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface DataValidator<T> extends DataObject {

    /**
     * 验证 如果验证不通过则抛出对应的验证异常
     *
     * @param object
     */
    public void validate(T object) throws AppError;
}
