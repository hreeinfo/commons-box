package commons.box.bean;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/15 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface BeanObjectAccess extends BeanAccess {
    /**
     * 额外构建
     *
     * @param type
     * @param args
     * @param <T>
     * @return
     */
    public <T> T inst2(Class<T> type, Object... args);
}