package commons.box.app;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：15/10/21 </p>
 * <p>版权所属：南京恒立信息科技有限公司 </p>
 */
public interface TypeConverter<S, T> {
    /**
     * 源类型
     *
     * @return
     */
    public S sourceType();

    /**
     * 目标类型
     *
     * @return
     */
    public T targetType();

    /**
     * 类型转换器 自动转换源类型为定义的目标类型
     *
     * @param source
     * @return
     */
    public T convert(S source);
}
