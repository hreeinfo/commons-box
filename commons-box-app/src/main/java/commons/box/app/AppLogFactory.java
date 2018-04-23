package commons.box.app;


/**
 * 日志工厂
 */
public interface AppLogFactory extends Priority {

    /**
     * 获取日志
     *
     * @param name
     * @return
     */
    public AppLog get(String name);
}
