package commons.box.app;

import commons.box.util.Orders;

/**
 * 优先级 根据 priority() 方法返回所定义对象的优先级 值越小优先级越高
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/7/3 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface Priority extends Comparable<Priority> {
    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

    /**
     * 获取优先级 值越小优先级越高
     * <p>
     * 一般系统内置默认服务为 Short.MAX_VALUE
     *
     * @return
     */
    public int priority();

    @Override
    default int compareTo(Priority o) {
        if (o == null) return 1;
        return Orders.compare(this.priority(), o.priority());
    }
}
